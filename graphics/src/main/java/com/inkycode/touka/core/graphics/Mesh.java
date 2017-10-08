package com.inkycode.touka.core.graphics;

public interface Mesh {

    int getVertexCount();

    int getIndexCount();

    int getPrimitiveCount();

    void render();

    void destroy();

}