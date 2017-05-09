package com.inkycode.silverzemni.core.bootstrap.impl.injectors;

import java.lang.reflect.Field;

import com.inkycode.silverzemni.core.bootstrap.Component;
import com.inkycode.silverzemni.core.bootstrap.ComponentFactory;
import com.inkycode.silverzemni.core.bootstrap.Injector;

public class PropertyInjector implements Injector {

    public String getName() {
        return "property";
    }

    public Object getValue(Field field, Component component, ComponentFactory componentFactory) {
        return component.getProperties().get(field.getName());
    }

}