package com.inkycode.touka.core.graphics;

import java.util.concurrent.Callable;

public interface Shader {

    public static final int SHADER_TYPE_UNKOWN = 0x00;
    public static final int SHADER_TYPE_VERTEX = 0x01;
    public static final int SHADER_TYPE_FRAGMENT = 0x01;

    void load();

    void unload();

    void use(Callable<Void> task);

}