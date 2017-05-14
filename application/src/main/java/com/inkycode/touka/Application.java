package com.inkycode.touka;

import com.inkycode.touka.components.impl.InjectedClass;
import com.inkycode.touka.components.impl.TestClass;
import com.inkycode.touka.core.bootstrap.Component;
import com.inkycode.touka.core.bootstrap.ComponentFactory;
import com.inkycode.touka.core.bootstrap.impl.ComponentFactoryImpl;

public class Application {

    public static void main(final String[] args) {
        final ComponentFactory componentFactory = new ComponentFactoryImpl();

        ComponentFactoryImpl.createComponents(ComponentFactoryImpl.DEFAULT_CONFIGURATION_PATH, componentFactory);

        for (final Component component : componentFactory.getAllComponents()) {
            component.activate();
        }
    }

}
