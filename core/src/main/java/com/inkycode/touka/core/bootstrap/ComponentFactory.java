package com.inkycode.touka.core.bootstrap;

import java.util.List;
import java.util.Map;

public interface ComponentFactory {

    void createComponent(String instanceName, Map<String, Object> properties);

    void createComponent(Class<?> interfaceClass, Class<?> implementationClass, String instanceName, Map<String, Object> properties);

    void activateComponent(Class<?> interfaceClass, Class<?> implementationClass);

    Component getComponent(Class<?> interfaceClass, Class<?> implementationClass);

    Component getComponent(Class<?> interfaceClass);

    List<Component> getAllComponents();

    List<Component> getAllComponents(Class<?> interfaceClass);

    <T> List<T> getAllComponentInstances(Class<T> interfaceClass);
}
