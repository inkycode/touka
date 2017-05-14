package com.inkycode.touka.core.bootstrap.impl.injectors;

import java.lang.reflect.Field;

import com.inkycode.touka.core.bootstrap.Component;
import com.inkycode.touka.core.bootstrap.ComponentFactory;
import com.inkycode.touka.core.bootstrap.Injector;

public class PropertyInjector implements Injector {

    public String getName() {
        return "property";
    }

    public Object getValue(Field field, Component component, ComponentFactory componentFactory) {
        return component.getProperties().get(field.getName());
    }

}