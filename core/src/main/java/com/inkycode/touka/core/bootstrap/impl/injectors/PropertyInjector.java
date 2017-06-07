package com.inkycode.touka.core.bootstrap.impl.injectors;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inkycode.touka.core.bootstrap.Component;
import com.inkycode.touka.core.bootstrap.ComponentFactory;
import com.inkycode.touka.core.bootstrap.Injector;
import com.inkycode.touka.core.bootstrap.annotations.Default;
import com.inkycode.touka.core.bootstrap.annotations.Named;

public class PropertyInjector implements Injector {

    private static final Logger LOG = LoggerFactory.getLogger(PropertyInjector.class);
    
    public String getName() {
        return "property";
    }

    public Object getValue(Field field, Component component, ComponentFactory componentFactory) {
        LOG.info("Getting value for component injector");

        String propertyName = field.getName();
        if (field.isAnnotationPresent(Named.class)) {
            propertyName = field.getDeclaredAnnotation(Named.class).value();

            LOG.info("Named annotation found, using value as property name '{}' for value look-up", propertyName);
        }

        Class<?> fieldTypeClass = field.getType();
        if (component.getProperties().containsKey(propertyName)) {
            Object propertyValue = component.getProperties().get(propertyName);
            Class<?> propertyClass = propertyValue.getClass();

            // By default, gson treats all numbers as doubles, using reflection we get the target class type and cast.
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

            LOG.info("Property named '{}' found with value '{}' and of class '{}', returning property value", propertyName, propertyValue, propertyClass);

            return propertyValue;
        } else if (field.isAnnotationPresent(Default.class)) {
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
            
            LOG.info("Default for property named '{}' found with value '{}', returning default value", propertyName, defaultAnnotation.value());
            return defaultAnnotation.value();
        }

        LOG.warn("No property with name '{}' found and no default annotation present so returning null", propertyName);
        return null;
    }

}