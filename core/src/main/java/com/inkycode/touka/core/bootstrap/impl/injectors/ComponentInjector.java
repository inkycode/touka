package com.inkycode.touka.core.bootstrap.impl.injectors;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inkycode.touka.core.bootstrap.Component;
import com.inkycode.touka.core.bootstrap.ComponentFactory;
import com.inkycode.touka.core.bootstrap.Injector;
import com.inkycode.touka.core.bootstrap.annotations.Named;

public class ComponentInjector implements Injector {

    private static final Logger LOG = LoggerFactory.getLogger(ComponentInjector.class);

    public String getName() {
        return "component";
    }

    public Object getValue(Field field, Component component, ComponentFactory componentFactory) {
        LOG.info("Getting value for component injector");

        if (Map.class.isAssignableFrom(field.getType()) && field.getGenericType() instanceof ParameterizedType) {
            LOG.info("Target field '{}' is Map assignable", field.getName());

            ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
            Class<?> componentInterface = (Class<?>) parameterizedType.getActualTypeArguments()[1];
            Map<String, Object> componentInstanceMap = new HashMap<String, Object>();

            for (Component componentToInject : componentFactory.getComponents(componentInterface)) {
                componentToInject.activate();

                LOG.info("Putting component instance '{}' into map", componentToInject.getInstanceName());
                componentInstanceMap.put(componentToInject.getInstanceName(), componentToInject.getInstance());
            }

            LOG.info("Returning map value to target field '{}'", field.getName());
            return componentInstanceMap;
        } else {
            Component componentToInject = null;

            LOG.info("Checking for named annotation", field.getName());
            String instanceName = field.getName();
            if (field.isAnnotationPresent(Named.class)) {
                instanceName = field.getDeclaredAnnotation(Named.class).value();

                LOG.info("Named annotation present, obtaining component instance with name '{}'", instanceName);
                componentToInject = componentFactory.getComponent(field.getType(), instanceName);
            } else {
                LOG.info("No named annotation present, obtaining default component instance", instanceName);
                componentToInject = componentFactory.getComponent(field.getType());
            }

            if (componentToInject != null) {
                componentToInject.activate();

                LOG.info("Returning component value to target field '{}'", field.getName());
                return componentToInject.getInstance();
            }

            return null;
        }
    }

}