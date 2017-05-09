package com.inkycode.silverzemni.core.bootstrap;

import java.util.List;
import java.util.Map;

public interface ComponentFactory {

    void createComponent(Map<String, Object> properties);

    void createComponent(Class<?> interfaceClass, Class<?> implementationClass);

    void createComponent(Class<?> interfaceClass, Class<?> implementationClass, Map<String, Object> properties);

    void activateComponent(Class<?> interfaceClass, Class<?> implementationClass);

    Component getComponent(Class<?> interfaceClass, Class<?> implementationClass);

    Component getComponent(Class<?> interfaceClass);

    List<Component> getAllComponents();

    List<Component> getAllComponents(Class<?> interfaceClass);

    <T> List<T> getAllComponentInstances(Class<T> interfaceClass);
}
