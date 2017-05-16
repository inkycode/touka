package com.inkycode.touka;

import static com.inkycode.touka.core.bootstrap.impl.ComponentFactoryImpl.DEFAULT_CONFIGURATION_PATH;

import com.inkycode.touka.core.bootstrap.impl.ComponentFactoryImpl;

public class Touka {

    public static void main(final String[] args) {
        new ComponentFactoryImpl().initialise(DEFAULT_CONFIGURATION_PATH);
    }

}