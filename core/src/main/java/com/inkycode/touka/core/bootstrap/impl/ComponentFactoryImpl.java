package com.inkycode.touka.core.bootstrap.impl;

import static com.inkycode.touka.core.bootstrap.impl.ComponentImpl.COMPONENT_PROPERTY_NAME_BOOTSTRAP_IMPLEMENTATION_CLASS;
import static com.inkycode.touka.core.bootstrap.impl.ComponentImpl.COMPONENT_PROPERTY_NAME_BOOTSTRAP_INTERFACE_CLASS;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.inkycode.touka.core.bootstrap.Component;
import com.inkycode.touka.core.bootstrap.ComponentFactory;
import com.inkycode.touka.core.bootstrap.Injector;
import com.inkycode.touka.core.bootstrap.impl.models.ComponentConfiguration;
import com.inkycode.touka.core.bootstrap.impl.models.ConfigurationProperties;

public class ComponentFactoryImpl implements ComponentFactory {

    public static final String DEFAULT_CONFIGURATION_PATH = "BOOTSTRAP-INF/configuration";

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
        Map<Integer, List<Path>> sortedComponentConfigurationFilePaths = new TreeMap<Integer, List<Path>>();

        putAllConfigurationFilePaths(configurationPath, sortedComponentConfigurationFilePaths);

        for (List<Path> componentConfigurationPaths : sortedComponentConfigurationFilePaths.values()) {

            for (Path componentConfigurationPath : componentConfigurationPaths) {
                try {
                    String[] fileNameParts = componentConfigurationPath.getFileName().toString().split(COMPONENT_INSTANCE_NAME_DELIMITER);
                    String componentImplementationClassName = fileNameParts[0].replaceAll(COMPONENT_CONFIGURATION_FILE_EXTENSION_REGEX, "");
                    String componentInstanceName = (fileNameParts.length == 2 ? fileNameParts[1] : fileNameParts[0]).replaceAll(COMPONENT_CONFIGURATION_FILE_EXTENSION_REGEX, "");
                    Class<?> componentImplementationClass = ClassLoader.getSystemClassLoader().loadClass(componentImplementationClassName);
                    Class<?> componentInterfaceClass = componentImplementationClass.getInterfaces()[0];

                    this.createComponent(componentInstanceName, getComponentProperties(componentConfigurationPath, componentInterfaceClass, componentImplementationClass));
                } catch (ClassNotFoundException e) {

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

    private Map<String, Object> getComponentProperties(Path componentConfigurationPath, Class<?> componentInterfaceClass, Class<?> componentImplementationClass) {
        Map<String, Object> componentProperties = new HashMap<String, Object>();

        componentProperties.put(COMPONENT_PROPERTY_NAME_BOOTSTRAP_INTERFACE_CLASS, componentInterfaceClass);
        componentProperties.put(COMPONENT_PROPERTY_NAME_BOOTSTRAP_IMPLEMENTATION_CLASS, componentImplementationClass);

        try (JsonReader jsonReader = new JsonReader(new FileReader(componentConfigurationPath.toString()))) {
            componentProperties.putAll(new Gson().fromJson(jsonReader, ComponentConfiguration.class));
        } catch (IOException e) {
            // Unable to read properties file
        }

        return componentProperties;
    }

    private void putAllConfigurationFilePaths(String basePath, Map<Integer, List<Path>> sortedComponentConfigurationFilePaths) {
        putAllConfigurationFilePaths(basePath, sortedComponentConfigurationFilePaths, Integer.MAX_VALUE);
    }

    private void putAllConfigurationFilePaths(String basePath, Map<Integer, List<Path>> sortedComponentConfigurationFilePaths, int loadLevel) {
        try (InputStream inputStream =  ClassLoader.getSystemResourceAsStream(basePath)) {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
                List<Path> componentConfigurationPaths = new ArrayList<Path>();
                String resourceName = "";
                
                while ((resourceName = bufferedReader.readLine()) != null) {
                    Path resourcePath = Paths.get(basePath, resourceName);
                    Path filePath = Paths.get(ClassLoader.getSystemResource(resourcePath.toString()).toURI());

                    if (Files.exists(filePath)) {
                        if (Files.isDirectory(filePath)) {
                            Path directoryPath = Paths.get(basePath, resourceName);
                            Path propertiesPath = Paths.get(basePath, resourceName, ".properties.json");
                            URL propertiesSystemResource = ClassLoader.getSystemResource(propertiesPath.toString());

                            if (propertiesSystemResource != null) {
                                Path propertiesFilePath = Paths.get(propertiesSystemResource.toURI());

                                if (Files.exists(propertiesFilePath)) {
                                    ConfigurationProperties configurationProperties = new Gson().fromJson(new FileReader(propertiesFilePath.toString()), ConfigurationProperties.class);

                                    putAllConfigurationFilePaths(directoryPath.toString(), sortedComponentConfigurationFilePaths, configurationProperties.getLoadLevel());
                                }
                            } else {
                                putAllConfigurationFilePaths(directoryPath.toString(), sortedComponentConfigurationFilePaths, loadLevel);
                            }

                        } else if (!resourceName.startsWith(".") && Files.isRegularFile(filePath)) {
                            componentConfigurationPaths.add(filePath);
                        }
                    } else {
                        // No such file found
                    }
                }

                if (sortedComponentConfigurationFilePaths.containsKey(loadLevel)) {
                    sortedComponentConfigurationFilePaths.get(loadLevel).addAll(componentConfigurationPaths);
                } else {
                    sortedComponentConfigurationFilePaths.put(loadLevel, componentConfigurationPaths);
                }
            }
        } catch (IOException | URISyntaxException e) {
            // TODO: Error handling
        }
    }

}
