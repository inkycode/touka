package com.inkycode.touka.core.graphics;

import java.util.concurrent.Callable;

public interface ShaderProgram {

    void load();

    void unload();

    void use(final Callable<Void> task);

    boolean isValid();

}