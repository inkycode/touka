package com.inkycode.silverzemni.core.bootstrap.impl.injectors;

import java.lang.reflect.Field;

import com.inkycode.silverzemni.core.bootstrap.Component;
import com.inkycode.silverzemni.core.bootstrap.ComponentFactory;
import com.inkycode.silverzemni.core.bootstrap.Injector;

public class ComponentInjector implements Injector {

    public String getName() {
        return "component";
    }

    public Object getValue(Field field, Component component, ComponentFactory componentFactory) {
        return componentFactory.getComponent(field.getType()).getInstance();
    }

}