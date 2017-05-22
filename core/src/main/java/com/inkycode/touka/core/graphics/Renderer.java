package com.inkycode.touka.core.graphics;

public interface Renderer {

    void initialise();

    void deinitialise();

    void clearBuffers();

    void setClearColor(int red, int green, int blue);

    void setViewport(int x, int y, int width, int height);

}