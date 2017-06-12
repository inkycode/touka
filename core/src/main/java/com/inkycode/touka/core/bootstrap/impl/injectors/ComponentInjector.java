package com.inkycode.touka.core.bootstrap.impl.injectors;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inkycode.touka.core.bootstrap.Component;
import com.inkycode.touka.core.bootstrap.ComponentFactory;
import com.inkycode.touka.core.bootstrap.Injector;
import com.inkycode.touka.core.bootstrap.annotations.Named;
import com.inkycode.touka.core.bootstrap.annotations.Via;

public class ComponentInjector implements Injector {

    private static final Logger LOG = LoggerFactory.getLogger(ComponentInjector.class);

    public String getName() {
        return "component";
    }

    public Object getValue(final Field field, final Component component, final ComponentFactory componentFactory) {
        LOG.info("Getting value for component injector");

        if (Map.class.isAssignableFrom(field.getType()) && field.getGenericType() instanceof ParameterizedType) {
            LOG.info("Target field '{}' is Map assignable", field.getName());

            final ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
            final Class<?> componentInterface = (Class<?>) parameterizedType.getActualTypeArguments()[1];
            final Map<String, Object> componentInstanceMap = new HashMap<String, Object>();

            for (final Component componentToInject : componentFactory.getComponents(componentInterface)) {
                componentToInject.activate();

                LOG.info("Putting component instance '{}' into map", componentToInject.getInstanceName());
                componentInstanceMap.put(componentToInject.getInstanceName(), componentToInject.getInstance());
            }

            LOG.info("Returning map value to target field '{}'", field.getName());
            return componentInstanceMap;
        } else if (Set.class.isAssignableFrom(field.getType()) && field.getGenericType() instanceof ParameterizedType) {
            LOG.info("Target field '{}' is Set assignable", field.getName());

            final ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
            final Class<?> componentInterface = (Class<?>) parameterizedType.getActualTypeArguments()[0];
            final Set<Object> componentInstanceSet = new LinkedHashSet<Object>();

            LOG.info("Checking for via and named annotation", field.getName());
            if (field.isAnnotationPresent(Via.class) && field.isAnnotationPresent(Named.class)) {
                final String via = field.getDeclaredAnnotation(Via.class).value();
                final String propertyName = field.getDeclaredAnnotation(Named.class).value();

                LOG.info("Via and named annotation present, checking for via source");
                if ("property".equals(via)) {
                    final List<String> names = (List<String>) component.getProperties().get(propertyName);

                    LOG.info("Via source is property, using property value for name");

                    for (final String name : names) {
                        final Component componentToInject = componentFactory.getComponent(componentInterface, name);

                        if (componentToInject != null) {
                            componentToInject.activate();

                            LOG.info("Adding component instance '{}' to set", componentToInject.getInstanceName());
                            componentInstanceSet.add(componentToInject.getInstance());
                        }
                    }
                }
            }

            return componentInstanceSet;
        } else {
            Component componentToInject = null;

            LOG.info("Checking for named annotation", field.getName());
            String name = field.getName();
            if (field.isAnnotationPresent(Named.class)) {
                name = field.getDeclaredAnnotation(Named.class).value();

                LOG.info("Checking for via annotation", field.getName());
                if (field.isAnnotationPresent(Via.class)) {
                    final String via = field.getDeclaredAnnotation(Via.class).value();

                    LOG.info("Via annotation present, checking for via source");
                    if ("property".equals(via)) {
                        name = (String) component.getProperties().get(name);

                        LOG.info("Via source is property, using property value for name");
                    }
                }

                LOG.info("Named annotation present, obtaining component instance with name '{}'", name);
                componentToInject = componentFactory.getComponent(field.getType(), name);

                if (componentToInject == null) {
                    LOG.info("Unable to inject using instance name, trying class name '{}'", name);
                    try {
                        final Class<?> implementationClass = Class.forName(name);

                        componentToInject = componentFactory.getComponent(field.getType(), implementationClass);
                    } catch (ClassNotFoundException e) {
                        // TODO: Handle error
                    }
                }
            } else {
                LOG.info("No named annotation present, obtaining default component instance", name);
                componentToInject = componentFactory.getComponent(field.getType());
            }

            if (componentToInject != null) {
                componentToInject.activate();

                LOG.info("Returning component value to target field '{}'", field.getName());
                return componentToInject.getInstance();
            }
        }

        LOG.warn("Unable to return non-null value for '{}'", field.getName());
        return null;
    }

}