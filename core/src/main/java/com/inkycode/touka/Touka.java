package com.inkycode.touka;

import static com.inkycode.touka.core.bootstrap.ComponentFactory.DEFAULT_CONFIGURATION_PATH;

import com.inkycode.touka.core.bootstrap.ComponentFactory;
import com.inkycode.touka.core.bootstrap.impl.ComponentFactoryImpl;

public class Touka {

    private static final ComponentFactory componentFactory = new ComponentFactoryImpl();

    public static void main(final String[] args) {
        getComponentFactory().initialise(DEFAULT_CONFIGURATION_PATH);
    }

    public static ComponentFactory getComponentFactory() {
        return Touka.componentFactory;
    }

}