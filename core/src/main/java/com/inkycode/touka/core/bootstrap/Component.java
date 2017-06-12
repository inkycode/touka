package com.inkycode.touka.core.bootstrap;

import java.util.List;
import java.util.Map;

public interface Component {

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
