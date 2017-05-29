package com.inkycode.touka.core.bootstrap.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.inkycode.touka.core.bootstrap.Component;
import com.inkycode.touka.core.bootstrap.ComponentFactory;
import com.inkycode.touka.core.bootstrap.Injector;
import com.inkycode.touka.core.bootstrap.annotations.Activate;
import com.inkycode.touka.core.bootstrap.annotations.Inject;
import com.inkycode.touka.core.bootstrap.annotations.Source;

public class ComponentImpl implements Component {

    public static final int COMPONENT_STATE_UNKOWN = 0x00;
    public static final int COMPONENT_STATE_CREATED = 0x01;
    public static final int COMPONENT_STATE_ACTIVE = 0x02;

    public static final Object[] COMPONENT_EMPTY_ARGUMENTS = new Object[] {};

    public static final String COMPONENT_PROPERTY_NAME_BOOTSTRAP_INTERFACE_CLASS = "bootstrap.interface-class";
    public static final String COMPONENT_PROPERTY_NAME_BOOTSTRAP_IMPLEMENTATION_CLASS = "bootstrap.implementation-class";
    public static final String COMPONENT_PROPERTY_NAME_BOOTSTRAP_INSTANCE_NAME = "bootstrap.instance-name";

    private final Class<?> interfaceClass;

    private final Class<?> implementationClass;

    private Object instance;

    private String instanceName;

    private int state;

    private final Map<String, Object> properties;

    public ComponentImpl(final Class<?> interfaceClass, final Class<?> implementationClass, final Map<String, Object> properties) {
        this(implementationClass.getName(), interfaceClass, implementationClass, properties);
    }

    public ComponentImpl(String instanceName, final Class<?> interfaceClass, final Class<?> implementationClass, final Map<String, Object> properties) {
        this.instanceName = instanceName;
        this.interfaceClass = interfaceClass;
        this.implementationClass = implementationClass;
        this.instance = null;
        this.state = COMPONENT_STATE_UNKOWN;
        this.properties = properties;
    }

    @Override
    public Class<?> getInterfaceClass() {
        return this.interfaceClass;
    }

    @Override
    public Class<?> getImplementationClass() {
        return this.implementationClass;
    }

    @Override
    public String getInstanceName() {
        return this.instanceName;
    }

    @Override
    public Object getInstance() {
        return this.instance;
    }

    @Override
    public <T> T getInstance(Class<T> clazz) {
        return clazz.cast(getInstance());
    }

    @Override
    public int getState() {
        return this.state;
    }

    @Override
    public Map<String, Object> getProperties() {
        return this.properties;
    }

    @Override
    public void create() {
        try {
            this.instance = this.getImplementationClass().newInstance();
            
            this.state = COMPONENT_STATE_CREATED;
        } catch (final ReflectiveOperationException e) {

        }
    }

    @Override
    public void activate() {
        try {
            for (final Method method : this.getImplementationClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(Activate.class)) {
                    method.invoke(this.getInstance(), COMPONENT_EMPTY_ARGUMENTS);

                    this.state = COMPONENT_STATE_ACTIVE;
                }
            }
        } catch (final ReflectiveOperationException e) {

        }
    }

    @Override
    public void inject(List<Injector> injectors, ComponentFactory componentFactory) {
        for (final Field field : this.getImplementationClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                if (field.isAnnotationPresent(Source.class)) {
                    Source source = field.getDeclaredAnnotation(Source.class);
                        
                    String injectSource = source.value();

                    for (Injector injector : injectors) {
                        try {
                            if (injector.getName().equalsIgnoreCase(injectSource)) {
                                field.setAccessible(true);
                                field.set(this.getInstance(), injector.getValue(field, this, componentFactory));
                            }
                        } catch (final ReflectiveOperationException e) {

                        }
                    }
                }
            }
        }
    }
}
