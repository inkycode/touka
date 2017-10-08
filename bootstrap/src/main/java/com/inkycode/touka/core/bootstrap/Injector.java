package com.inkycode.touka.core.bootstrap;

import java.lang.reflect.Field;

public interface Injector {

    String getName();

    Object getValue(final Field field, final Component component, final ComponentFactory componentFactory);
}