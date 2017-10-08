package com.inkycode.touka.core.bootstrap;

import java.util.List;
import java.util.Map;

public interface ComponentFactory {

    public static final String DEFAULT_CONFIGURATION_PATH = "BOOTSTRAP-INF/configuration";

    void initialise(final String configurationPath);

    void createComponents(final String configurationPath);

    void injectComponents();

    void injectComponent(final Component component);

    void activateComponents();

    void activateComponent(final Component component);

    void createComponent(final String instanceName, final Map<String, Object> properties);

    void createComponent(final Class<?> interfaceClass, final Class<?> implementationClass, final String instanceName, final Map<String, Object> properties);

    Component getComponent(final Class<?> interfaceClass);

    Component getComponent(final Class<?> interfaceClass, final String instanceName);

    Component getComponent(final Class<?> interfaceClass, final Class<?> implementationClass);

    List<Component> getComponents();

    List<Component> getComponents(final Class<?> interfaceClass);

    <T> List<T> getComponentInstances(final Class<T> interfaceClass);
}
