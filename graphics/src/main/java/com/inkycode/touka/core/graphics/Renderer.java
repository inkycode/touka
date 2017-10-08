package com.inkycode.touka.core.graphics;

public interface Renderer {

    void initialise();

    void deinitialise();

    void clearBuffers();

    void setClearColor(final int red, final int green, final int blue);

    void setViewport(final int x, final int y, final int width, final int height);

}