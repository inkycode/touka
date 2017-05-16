package com.inkycode.touka.core.platform;

public interface Canvas {

    void initialise();

    void deinitialise();

    void swapBuffers();

    boolean isClosing();
}