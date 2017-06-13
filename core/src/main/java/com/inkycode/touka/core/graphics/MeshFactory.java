package com.inkycode.touka.core.graphics;

public interface MeshFactory {

    void addVertex(final Vertex vertex);

    void addPrimitive(final Primitive primitive);

    VertexFactory getVertexFactory();

    Mesh build();

}