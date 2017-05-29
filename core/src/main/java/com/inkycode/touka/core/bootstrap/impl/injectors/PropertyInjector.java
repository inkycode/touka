package com.inkycode.touka.core.bootstrap.impl.injectors;

import java.lang.reflect.Field;

import com.inkycode.touka.core.bootstrap.Component;
import com.inkycode.touka.core.bootstrap.ComponentFactory;
import com.inkycode.touka.core.bootstrap.Injector;
import com.inkycode.touka.core.bootstrap.annotations.Default;
import com.inkycode.touka.core.bootstrap.annotations.Named;

public class PropertyInjector implements Injector {

    public String getName() {
        return "property";
    }

    public Object getValue(Field field, Component component, ComponentFactory componentFactory) {
        String propertyName = field.getName();
        if (field.isAnnotationPresent(Named.class)) {
            propertyName = field.getDeclaredAnnotation(Named.class).value();
        }

        Class<?> fieldTypeClass = field.getType();
        if (component.getProperties().containsKey(propertyName)) {
            Object propertyValue = component.getProperties().get(propertyName);
            Class<?> propertyClass = propertyValue.getClass();

            if (propertyClass == Double.class) {
                Double doubleValue = (Double) propertyValue;

                if (fieldTypeClass == Integer.class || fieldTypeClass == int.class) {
                    return doubleValue.intValue();
                } else if (fieldTypeClass == Long.class || fieldTypeClass == long.class) {
                    return doubleValue.longValue();
                } else if (fieldTypeClass == Float.class || fieldTypeClass == float.class) {
                    return doubleValue.floatValue();
                } else if (fieldTypeClass == Double.class || fieldTypeClass == double.class) {
                    return doubleValue.doubleValue();
                } else if (fieldTypeClass == Boolean.class || fieldTypeClass == boolean.class) {
                    return doubleValue > 0;
                } else if (fieldTypeClass == String.class) {
                    return String.valueOf(doubleValue);
                }
            }

            return propertyValue;
        } else {
            if (field.isAnnotationPresent(Default.class)) {
                Default defaultAnnotation = field.getDeclaredAnnotation(Default.class);

                if (fieldTypeClass == Integer.class || fieldTypeClass == int.class) {
                    return defaultAnnotation.intValue();
                } else if (fieldTypeClass == Long.class || fieldTypeClass == long.class) {
                    return defaultAnnotation.longValue();
                } else if (fieldTypeClass == Float.class || fieldTypeClass == float.class) {
                    return defaultAnnotation.floatValue();
                } else if (fieldTypeClass == Double.class || fieldTypeClass == double.class) {
                    return defaultAnnotation.doubleValue();
                } else if (fieldTypeClass == Boolean.class || fieldTypeClass == boolean.class) {
                    return defaultAnnotation.booleanValue();
                } else if (fieldTypeClass == String.class) {
                    return defaultAnnotation.stringValue();
                }
                
                return defaultAnnotation.value();
            }

            return null;
        }
    }

}