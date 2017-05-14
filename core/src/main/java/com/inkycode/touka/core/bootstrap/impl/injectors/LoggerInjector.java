package com.inkycode.touka.core.bootstrap.impl.injectors;

import java.lang.reflect.Field;

import org.slf4j.LoggerFactory;

import com.inkycode.touka.core.bootstrap.Component;
import com.inkycode.touka.core.bootstrap.ComponentFactory;
import com.inkycode.touka.core.bootstrap.Injector;

public class LoggerInjector implements Injector {

    public String getName() {
        return "logger";
    }

    public Object getValue(Field field, Component component, ComponentFactory componentFactory) {
        return LoggerFactory.getLogger(component.getImplementationClass());
    }

}