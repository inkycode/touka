package com.inkycode.touka.core.bootstrap.impl.injectors;

import java.lang.reflect.Field;

import com.inkycode.touka.core.bootstrap.Component;
import com.inkycode.touka.core.bootstrap.ComponentFactory;
import com.inkycode.touka.core.bootstrap.Injector;
import com.inkycode.touka.core.bootstrap.annotations.Default;

public class PropertyInjector implements Injector {

    public String getName() {
        return "property";
    }

    public Object getValue(Field field, Component component, ComponentFactory componentFactory) {
        if (component.getProperties().containsKey(field.getName())) {
            return component.getProperties().get(field.getName());
        } else {
            if (field.isAnnotationPresent(Default.class)) {
                Default defaultAnnotation = field.getDeclaredAnnotation(Default.class);

                if (field.getType() == Integer.class || field.getType() == int.class) {
                    return defaultAnnotation.intValue();
                } else if (field.getType() == Long.class || field.getType() == long.class) {
                    return defaultAnnotation.longValue();
                } else if (field.getType() == Float.class || field.getType() == float.class) {
                    return defaultAnnotation.floatValue();
                } else if (field.getType() == Double.class || field.getType() == double.class) {
                    return defaultAnnotation.doubleValue();
                } else if (field.getType() == Boolean.class || field.getType() == boolean.class) {
                    return defaultAnnotation.booleanValue();
                } else if (field.getType() == String.class) {
                    return defaultAnnotation.stringValue();
                } else {
                    return defaultAnnotation.value();
                }
            }

            return null;
        }
    }

}