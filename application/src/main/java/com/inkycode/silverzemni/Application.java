package com.inkycode.silverzemni;

import com.inkycode.silverzemni.core.bootstrap.Component;
import com.inkycode.silverzemni.core.bootstrap.ComponentFactory;
import com.inkycode.silverzemni.core.bootstrap.impl.ComponentFactoryImpl;

public class Application {

    public static void main(final String[] args) {
        final ComponentFactory componentFactory = new ComponentFactoryImpl();

        ComponentFactoryImpl.createComponentFromJson("BOOTSTRAP-INF/configuration", InjectedClass.class.getName(), componentFactory);
        ComponentFactoryImpl.createComponentFromJson("BOOTSTRAP-INF/configuration", TestClass.class.getName(), componentFactory);

        for (final Component component : componentFactory.getAllComponents()) {
            component.activate();
        }
    }

}
