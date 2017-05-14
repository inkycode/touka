package com.inkycode.touka.core.bootstrap.impl;

import static com.inkycode.touka.core.bootstrap.impl.ComponentImpl.COMPONENT_PROPERTY_NAME_BOOTSTRAP_IMPLEMENTATION_CLASS;
import static com.inkycode.touka.core.bootstrap.impl.ComponentImpl.COMPONENT_PROPERTY_NAME_BOOTSTRAP_INTERFACE_CLASS;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
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

public class ComponentFactoryImpl implements ComponentFactory {

    public static final String DEFAULT_CONFIGURATION_PATH = "BOOTSTRAP-INF/configuration";

    private final Map<Class<?>, Map<Class<?>, Map<String, Component>>> components = new HashMap<Class<?>, Map<Class<?>, Map<String, Component>>>();

    public static void createComponents(final String configurationPath, ComponentFactory componentFactory) {
        Map<Integer, List<Path>> sortedComponentConfigurationFilePaths = new TreeMap<Integer, List<Path>>();

        putAllConfigurationFilePaths(configurationPath, sortedComponentConfigurationFilePaths);

        for (List<Path> componentConfigurationPaths : sortedComponentConfigurationFilePaths.values()) {
            for (Path componentConfigurationPath : componentConfigurationPaths) {
                try {
                    String[] fileNameParts = componentConfigurationPath.getFileName().toString().split("-");
                    String componentImplementationClassName = fileNameParts[0].replaceAll(".json$", "");
                    String componentInstanceName = (fileNameParts.length == 2 ? fileNameParts[1] : fileNameParts[0]).replaceAll(".json$", "");
                    Class<?> componentImplementationClass = ClassLoader.getSystemClassLoader().loadClass(componentImplementationClassName);
                    Class<?> componentInterfaceClass = componentImplementationClass.getInterfaces()[0];

                    Map<String, Object> componentProperties = new HashMap<String, Object>();

                    componentProperties.put(COMPONENT_PROPERTY_NAME_BOOTSTRAP_INTERFACE_CLASS, componentInterfaceClass);
                    componentProperties.put(COMPONENT_PROPERTY_NAME_BOOTSTRAP_IMPLEMENTATION_CLASS, componentImplementationClass);

                    JsonReader jsonReader = null;
                    try {
                        Gson gson = new Gson();

                        jsonReader = new JsonReader(new FileReader(componentConfigurationPath.toString()));

                        Map<String, String> componentConfigurationProperties = gson.fromJson(jsonReader, ComponentConfigurationImpl.class);

                        componentProperties.putAll(componentConfigurationProperties);
                    } finally {
                        if (jsonReader != null) {
                            jsonReader.close();
                        }
                    }
                    
                    componentFactory.createComponent(componentInstanceName, componentProperties);
                } catch (IOException | ClassNotFoundException e) {

                }
            }
        }

        for (Component component : componentFactory.getAllComponents()) {
            component.inject(componentFactory.getAllComponentInstances(Injector.class), componentFactory);
        }
    }

    public static void putAllConfigurationFilePaths(String basePath, Map<Integer, List<Path>> sortedComponentConfigurationFilePaths) {
        putAllConfigurationFilePaths(basePath, sortedComponentConfigurationFilePaths, Integer.MAX_VALUE);
    }

    public static void putAllConfigurationFilePaths(String basePath, Map<Integer, List<Path>> sortedComponentConfigurationFilePaths, int level) {
        try {
            InputStream inputStream =  ClassLoader.getSystemResourceAsStream(basePath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            List<Path> componentConfigurationPaths = new ArrayList<Path>();
            String resourceName = "";
            
            while ((resourceName = bufferedReader.readLine()) != null) {
                Path resourcePath = Paths.get(basePath, resourceName);
                Path filePath = Paths.get(ClassLoader.getSystemResource(resourcePath.toString()).toURI());

                if (Files.exists(filePath)) {
                    if (Files.isDirectory(filePath)) {
                        putAllConfigurationFilePaths(Paths.get(basePath, resourceName).toString(), sortedComponentConfigurationFilePaths, Integer.valueOf(resourceName));
                    } else if (Files.isRegularFile(filePath)) {
                        componentConfigurationPaths.add(filePath);
                    }
                } else {
                    // No such file found
                }
            }

            bufferedReader.close();
            inputStream.close();

            sortedComponentConfigurationFilePaths.put(level, componentConfigurationPaths);
        } catch (IOException | URISyntaxException e) {

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
    public void activateComponent(final Class<?> interfaceClass, final Class<?> implementationClass) {
        final Component component = this.getComponent(interfaceClass, implementationClass);

        if (component != null) {
            component.activate();
        } else {
            // No such component
        }
    }

    @Override
    public Component getComponent(final Class<?> interfaceClass, final Class<?> implementationClass) {
        if (this.components.containsKey(interfaceClass)) {
            if (this.components.get(interfaceClass).containsKey(implementationClass)) {
                return this.components.get(interfaceClass)
                    .get(implementationClass)
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
    public List<Component> getAllComponents() {
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
    public List<Component> getAllComponents(final Class<?> interfaceClass) {
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
    public <T> List<T> getAllComponentInstances(final Class<T> interfaceClass) {
        List<Component> components = this.getAllComponents(interfaceClass);

        if (components == null) return null;

        List<T> componentInstances = new ArrayList<T>();
        
        for (Component component : this.getAllComponents(interfaceClass)) {
            componentInstances.add(component.getInstance(interfaceClass));
        }

        return componentInstances;
    }

}
