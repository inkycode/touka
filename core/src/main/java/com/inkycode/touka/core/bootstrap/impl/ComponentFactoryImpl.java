package com.inkycode.touka.core.bootstrap.impl;

import static com.inkycode.touka.core.bootstrap.impl.ComponentImpl.COMPONENT_PROPERTY_NAME_BOOTSTRAP_IMPLEMENTATION_CLASS;
import static com.inkycode.touka.core.bootstrap.impl.ComponentImpl.COMPONENT_PROPERTY_NAME_BOOTSTRAP_INTERFACE_CLASS;
import static com.inkycode.touka.core.bootstrap.impl.ComponentImpl.COMPONENT_PROPERTY_NAME_BOOTSTRAP_INSTANCE_NAME;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.inkycode.touka.core.bootstrap.Component;
import com.inkycode.touka.core.bootstrap.ComponentFactory;
import com.inkycode.touka.core.bootstrap.Injector;
import com.inkycode.touka.core.bootstrap.impl.models.ComponentConfiguration;
import com.inkycode.touka.core.bootstrap.impl.models.ConfigurationProperties;

public class ComponentFactoryImpl implements ComponentFactory {

    public static final String DEFAULT_CONFIGURATION_PATH = "BOOTSTRAP-INF/configuration";

    public static final String DEFAULT_PROPERTIES_FILE_NAME = ".properties.json";

    public static final String COMPONENT_INSTANCE_NAME_DELIMITER = "-";

    public static final String COMPONENT_CONFIGURATION_FILE_EXTENSION = "json";

    public static final String COMPONENT_CONFIGURATION_FILE_EXTENSION_REGEX = "." + COMPONENT_CONFIGURATION_FILE_EXTENSION + "$";

    private final Map<Class<?>, Map<Class<?>, Map<String, Component>>> components = new HashMap<Class<?>, Map<Class<?>, Map<String, Component>>>();

    @Override
    public void initialise(String configurationPath) {
        this.createComponents(configurationPath);

        this.injectComponents();

        this.activateComponents();
    }

    @Override
    public void createComponents(String configurationPath) {
        Map<Integer, Set<String>> sortedComponentConfigurationPaths = getComponentConfigurationPaths(configurationPath);

        for (Set<String> componentConfigurationPaths : sortedComponentConfigurationPaths.values()) {

            for (String componentConfigurationPath : componentConfigurationPaths) {
                try {
                    String[] fileNameParts = Paths.get(componentConfigurationPath).getFileName().toString().split(COMPONENT_INSTANCE_NAME_DELIMITER);
                    String componentImplementationClassName = fileNameParts[0].replaceAll(COMPONENT_CONFIGURATION_FILE_EXTENSION_REGEX, "");
                    String componentInstanceName = (fileNameParts.length == 2 ? fileNameParts[1] : fileNameParts[0]).replaceAll(COMPONENT_CONFIGURATION_FILE_EXTENSION_REGEX, "");
                    Class<?> componentImplementationClass = getClass().getClassLoader().loadClass(componentImplementationClassName);
                    Class<?> componentInterfaceClass = componentImplementationClass.getInterfaces()[0]; // Assume the first interface is what this component is an instance of

                    this.createComponent(componentInstanceName, populateComponentProperties(componentConfigurationPath, componentInterfaceClass, componentImplementationClass, componentInstanceName));
                } catch (ClassNotFoundException e) {
                    // TODO: Handle class loader errors
                }
            }
        }
    }

    @Override
    public void injectComponents() {
        for (Component component : this.getComponents()) {
            component.inject(this.getComponentInstances(Injector.class), this);
        }
    }

    @Override
    public void activateComponents() {
        for (Component component : this.getComponents()) {
            component.activate();
        }
    }

    @Override
    public void createComponent(final String instanceName, final Map<String, Object> properties) {
        this.createComponent((Class<?>) properties.get(COMPONENT_PROPERTY_NAME_BOOTSTRAP_INTERFACE_CLASS), (Class<?>) properties.get(COMPONENT_PROPERTY_NAME_BOOTSTRAP_IMPLEMENTATION_CLASS), instanceName, properties);
    }

    @Override
    public void createComponent(final Class<?> interfaceClass, final Class<?> implementationClass, final String instanceName, final Map<String, Object> properties) {
        if (!this.components.containsKey(interfaceClass)) {
            this.components.put(interfaceClass, new HashMap<Class<?>, Map<String, Component>>());
        }

        final Component component = new ComponentImpl(instanceName, interfaceClass, implementationClass, properties != null ? properties : new HashMap<String, Object>());

        component.create();

        if (this.components.get(interfaceClass).get(implementationClass) == null) {
            this.components.get(interfaceClass).put(implementationClass, new HashMap<String, Component>());
        }

        this.components.get(interfaceClass).get(implementationClass).put(instanceName, component);
    }

    @Override
    public Component getComponent(final Class<?> interfaceClass) {
        if (this.components.containsKey(interfaceClass)) {
            if (!this.components.get(interfaceClass).isEmpty()) {
                return this.components.get(interfaceClass)
                    .values()
                    .stream()
                    .findFirst()
                    .get()
                        .values()
                        .stream()
                        .findFirst()
                        .get();
            } else {
                // No component for given implementation class found
            }
        } else {
            // No component for given interface class found
        }

        return null;
    }

    @Override
    public List<Component> getComponents() {
        final List<Component> components = new ArrayList<Component>();

        for (final Map<Class<?>, Map<String, Component>> componentEntry : this.components.values()) {
            for (final Map<String, Component> componentMap : componentEntry.values()) {
                for (final Component component : componentMap.values()) {
                    components.add(component);
                }
            }
        }

        return components;
    }

    @Override
    public List<Component> getComponents(final Class<?> interfaceClass) {
        if (this.components.containsKey(interfaceClass)) {
            if (!this.components.get(interfaceClass).isEmpty()) {
                List<Component> components = new ArrayList<Component>();

                for (Map<String, Component> componentMap : this.components.get(interfaceClass).values()) {
                    components.addAll(componentMap.values());
                }

                return components;
            } else {
                // No component for given implementation class found
            }
        } else {
            // No component for given interface class found
        }

        return null;
    }

    @Override
    public <T> List<T> getComponentInstances(final Class<T> interfaceClass) {
        List<Component> components = this.getComponents(interfaceClass);

        if (components == null) return null;

        List<T> componentInstances = new ArrayList<T>();
        
        for (Component component : this.getComponents(interfaceClass)) {
            componentInstances.add(component.getInstance(interfaceClass));
        }

        return componentInstances;
    }

    private Map<String, Object> populateComponentProperties(String componentConfigurationPath, Class<?> componentInterfaceClass, Class<?> componentImplementationClass, String componentInstanceName) {
        Map<String, Object> componentProperties = new HashMap<String, Object>();

        componentProperties.put(COMPONENT_PROPERTY_NAME_BOOTSTRAP_INTERFACE_CLASS, componentInterfaceClass);
        componentProperties.put(COMPONENT_PROPERTY_NAME_BOOTSTRAP_IMPLEMENTATION_CLASS, componentImplementationClass);
        componentProperties.put(COMPONENT_PROPERTY_NAME_BOOTSTRAP_INSTANCE_NAME, componentInstanceName);
        
        try (InputStream resourceInputStream = getClass().getClassLoader().getResourceAsStream(componentConfigurationPath)) {
            if (resourceInputStream != null) {
                try (InputStreamReader resourceInputStreamReader = new InputStreamReader(resourceInputStream, "UTF-8")) {
                    try (JsonReader resourceJsonReader = new JsonReader(resourceInputStreamReader)) {
                        componentProperties.putAll(new Gson().fromJson(resourceJsonReader, ComponentConfiguration.class));
                    } catch (IOException e) {
                        // TODO: Handle errors
                    }
                } catch (IOException e) {
                    // TODO: Handle errors
                }
            }
        } catch (IOException e) {
            // TODO: Handle errors
        }

        return componentProperties;
    }

    private Map<Integer, Set<String>> getComponentConfigurationPaths(String basePath) {
        Set<String> sortedResourcePaths = new TreeSet<String>();
        Map<Integer, Set<String>> sortedComponentConfigurationPaths = new TreeMap<Integer, Set<String>>();

        // Get all resources that start with basePath and place them into a sorted list
        File executionFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        if (executionFile.isFile()) {
            try (JarFile jarFile = new JarFile(executionFile)) {
                Enumeration<JarEntry> entries = jarFile.entries();

                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();

                    if (entry.getName().startsWith(basePath + "/")) {
                        sortedResourcePaths.add(entry.getName());
                    }
                }

            } catch (IOException e) {
                // TODO: Handle errors
            }
        }

        // Iterate through all the resource paths in the sorted list and generate entries into the sorted component configuration paths map
        int loadLevel = Integer.MAX_VALUE;
        for (String resourcePath : sortedResourcePaths) {
            if (resourcePath.endsWith("/")) {
                loadLevel = Integer.MAX_VALUE;

                try (InputStream resourceInputStream = getClass().getClassLoader().getResourceAsStream(resourcePath + DEFAULT_PROPERTIES_FILE_NAME)) {
                    if (resourceInputStream != null) {
                        try (InputStreamReader resourceInputStreamReader = new InputStreamReader(resourceInputStream, "UTF-8")) {
                            try (JsonReader resourceJsonReader = new JsonReader(resourceInputStreamReader)) {
                                ConfigurationProperties configurationProperties = new Gson().fromJson(resourceJsonReader, ConfigurationProperties.class);
                            
                                loadLevel = configurationProperties.getLoadLevel();
                            } catch (IOException e) {
                                // TODO: Handle errors
                            }
                        } catch (IOException e) {
                            // TODO: Handle errors
                        }
                    }
                } catch (IOException e) {
                    // TODO: Handle errors
                }
            } else {
                if (!DEFAULT_PROPERTIES_FILE_NAME.equalsIgnoreCase(Paths.get(resourcePath).getFileName().toString())) {
                    if (!sortedComponentConfigurationPaths.containsKey(loadLevel)) {
                        sortedComponentConfigurationPaths.put(loadLevel, new TreeSet<String>());
                    }

                    sortedComponentConfigurationPaths.get(loadLevel).add(resourcePath);
                }
            }
        }

        return sortedComponentConfigurationPaths;
    }

}
