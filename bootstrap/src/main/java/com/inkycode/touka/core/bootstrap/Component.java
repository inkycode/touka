package com.inkycode.touka.core.bootstrap;

import java.util.List;
import java.util.Map;

public interface Component {

    public static final int COMPONENT_STATE_UNKOWN = 0x00;
    public static final int COMPONENT_STATE_CREATED = 0x01;
    public static final int COMPONENT_STATE_ACTIVE = 0x02;

    public static final String COMPONENT_PROPERTY_NAME_BOOTSTRAP_INTERFACE_CLASS = "bootstrap.interface-class";
    public static final String COMPONENT_PROPERTY_NAME_BOOTSTRAP_IMPLEMENTATION_CLASS = "bootstrap.implementation-class";
    public static final String COMPONENT_PROPERTY_NAME_BOOTSTRAP_INSTANCE_NAME = "bootstrap.instance-name";
    
    Class<?> getInterfaceClass();

    Class<?> getImplementationClass();

    String getInstanceName();

    Object getInstance();

    <T> T getInstance(final Class<T> clazz);

    int getState();

    Map<String, Object> getProperties();

    void create();

    void activate();

    void inject(final List<Injector> injectors, final ComponentFactory componentFactory);
}
