package com.inkycode.touka.core.bootstrap;

import java.util.List;
import java.util.Map;

public interface ComponentFactory {

    void initialise(String configurationPath);

    void createComponents(String configurationPath);

    void injectComponents();

    void activateComponents();

    void createComponent(String instanceName, Map<String, Object> properties);

    void createComponent(Class<?> interfaceClass, Class<?> implementationClass, String instanceName, Map<String, Object> properties);

    Component getComponent(Class<?> interfaceClass);

    Component getComponent(Class<?> interfaceClass, String instanceName);

    List<Component> getComponents();

    List<Component> getComponents(Class<?> interfaceClass);

    <T> List<T> getComponentInstances(Class<T> interfaceClass);
}
