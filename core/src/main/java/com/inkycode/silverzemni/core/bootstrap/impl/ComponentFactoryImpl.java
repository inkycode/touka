package com.inkycode.silverzemni.core.bootstrap.impl;

import static com.inkycode.silverzemni.core.bootstrap.impl.ComponentImpl.COMPONENT_PROPERTY_NAME_BOOTSTRAP_IMPLEMENTATION_CLASS;
import static com.inkycode.silverzemni.core.bootstrap.impl.ComponentImpl.COMPONENT_PROPERTY_NAME_BOOTSTRAP_INTERFACE_CLASS;
import static java.lang.ClassLoader.getSystemResource;

import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.inkycode.silverzemni.core.bootstrap.Component;
import com.inkycode.silverzemni.core.bootstrap.ComponentFactory;
import com.inkycode.silverzemni.core.bootstrap.Injector;
import com.inkycode.silverzemni.core.bootstrap.impl.injectors.ComponentInjector;
import com.inkycode.silverzemni.core.bootstrap.impl.injectors.PropertyInjector;

public class ComponentFactoryImpl implements ComponentFactory {

    public static final String DEFAULT_CONFIGURATION_PATH = "BOOTSTRAP-INF/configuration";

    private final Map<Class<?>, Map<Class<?>, Component>> components = new HashMap<Class<?>, Map<Class<?>, Component>>();

    public static void createComponentFromJson(final String configurationPath, final String classname, final ComponentFactory componentFactory) {

        try {
            final String filepath = Paths.get(configurationPath, classname) + ".json";

            final Class<?> implementationClass = ClassLoader.getSystemClassLoader().loadClass(classname);
            
            final Path path = Paths.get(getSystemResource(filepath).toURI());

            if (implementationClass != null) {
                final Class<?> interfaceClass = implementationClass.getInterfaces()[0];

                if (interfaceClass != null) {
                    final Map<String, Object> componentProperties = new HashMap<String, Object>();

                    componentProperties.put(COMPONENT_PROPERTY_NAME_BOOTSTRAP_INTERFACE_CLASS, interfaceClass);
                    componentProperties.put(COMPONENT_PROPERTY_NAME_BOOTSTRAP_IMPLEMENTATION_CLASS, implementationClass);

                    JsonReader jsonReader = null;
                    try {
                        Gson gson = new Gson();

                        jsonReader = new JsonReader(new FileReader(path.toString()));

                        Map<String, String> componentConfigurationProperties = gson.fromJson(jsonReader, ComponentConfigurationImpl.class);

                        componentProperties.putAll(componentConfigurationProperties);
                    } finally {
                        if (jsonReader != null) {
                            jsonReader.close();
                        }
                    }
                    
                    componentFactory.createComponent(componentProperties);
                }
            }
        } catch (final IOException | URISyntaxException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ComponentFactoryImpl() {
        ComponentFactoryImpl.createComponentFromJson(DEFAULT_CONFIGURATION_PATH, PropertyInjector.class.getName(), this);
        ComponentFactoryImpl.createComponentFromJson(DEFAULT_CONFIGURATION_PATH, ComponentInjector.class.getName(), this);
    }

    @Override
    public void createComponent(final Map<String, Object> properties) {
        this.createComponent((Class<?>) properties.get(COMPONENT_PROPERTY_NAME_BOOTSTRAP_INTERFACE_CLASS), (Class<?>) properties.get(COMPONENT_PROPERTY_NAME_BOOTSTRAP_IMPLEMENTATION_CLASS), properties);
    }

    @Override
    public void createComponent(final Class<?> interfaceClass, final Class<?> implementationClass) {
        this.createComponent(interfaceClass, implementationClass, null);
    }

    @Override
    public void createComponent(final Class<?> interfaceClass, final Class<?> implementationClass, final Map<String, Object> properties) {
        if (!this.components.containsKey(interfaceClass)) {
            this.components.put(interfaceClass, new HashMap<Class<?>, Component>());
        }

        if (!this.components.get(interfaceClass).containsKey(implementationClass)) {
            final Component component = new ComponentImpl(interfaceClass, implementationClass, properties != null ? properties : new HashMap<String, Object>());

            component.create();

            component.inject(getAllComponentInstances(Injector.class), this);

            this.components.get(interfaceClass).put(implementationClass, component);
        } else {
            // Component for given interface and implementation already exists
        }
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
                return this.components.get(interfaceClass).get(implementationClass);
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
                return this.components.get(interfaceClass).values().iterator().next();
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

        for (final Map<Class<?>, Component> componentEntry : this.components.values()) {
            for (final Component component : componentEntry.values()) {
                components.add(component);
            }
        }

        return components;
    }

    @Override
    public List<Component> getAllComponents(final Class<?> interfaceClass) {
        if (this.components.containsKey(interfaceClass)) {
            if (!this.components.get(interfaceClass).isEmpty()) {
                return new ArrayList<Component>(this.components.get(interfaceClass).values());
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
