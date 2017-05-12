package com.inkycode.silverzemni.core.bootstrap.impl.injectors;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.inkycode.silverzemni.core.bootstrap.Component;
import com.inkycode.silverzemni.core.bootstrap.ComponentFactory;
import com.inkycode.silverzemni.core.bootstrap.Injector;

public class ComponentInjector implements Injector {

    public String getName() {
        return "component";
    }

    public Object getValue(Field field, Component component, ComponentFactory componentFactory) {
        if (Map.class.isAssignableFrom(field.getType())) {
            if (field.getGenericType() instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                Type componentType = parameterizedType.getActualTypeArguments()[1];
                Class<?> componentClass = (Class<?>) componentType;
                Map<String, Object> componentInstanceMap = new HashMap<String, Object>();
                for (Component componentToInject : componentFactory.getAllComponents(componentClass)) {
                    componentInstanceMap.put(componentToInject.getInstanceName(), componentToInject.getInstance());
                }
                return componentInstanceMap;
            }
        } else {
            return componentFactory.getComponent(field.getType()).getInstance();
        }

        return null;
    }

}