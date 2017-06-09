package com.inkycode.touka.core.bootstrap.impl.injectors;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

import com.inkycode.touka.core.bootstrap.Component;
import com.inkycode.touka.core.bootstrap.ComponentFactory;
import com.inkycode.touka.core.bootstrap.Injector;
import com.inkycode.touka.core.bootstrap.annotations.Named;

public class ComponentInjector implements Injector {

    public String getName() {
        return "component";
    }

    public Object getValue(Field field, Component component, ComponentFactory componentFactory) {
        if (Map.class.isAssignableFrom(field.getType())) {
            if (field.getGenericType() instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                Class<?> componentInterface = (Class<?>) parameterizedType.getActualTypeArguments()[1];

                Map<String, Object> componentInstanceMap = new HashMap<String, Object>();

                for (Component componentToInject : componentFactory.getComponents(componentInterface)) {
                    componentToInject.activate();

                    componentInstanceMap.put(componentToInject.getInstanceName(), componentToInject.getInstance());
                }

                return componentInstanceMap;
            }
        } else {
            Component componentToInject = null;
            String instanceName = field.getName();

            if (field.isAnnotationPresent(Named.class)) {
                instanceName = field.getDeclaredAnnotation(Named.class).value();

                componentToInject = componentFactory.getComponent(field.getType(), instanceName);
            } else {
                componentToInject = componentFactory.getComponent(field.getType());
            }
            
            componentToInject.activate();

            return componentToInject.getInstance();
        }

        return null;
    }

}