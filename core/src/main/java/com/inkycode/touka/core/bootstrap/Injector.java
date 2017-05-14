package com.inkycode.touka.core.bootstrap;

import java.lang.reflect.Field;

public interface Injector {

    String getName();

    Object getValue(Field field, Component component, ComponentFactory componentFactory);
}