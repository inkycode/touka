package com.inkycode.touka.core.graphics;

public interface MeshFactory {

    void addVertex(final Vertex vertex);

    void addPolygon(final Polygon polygon);

    VertexFactory getVertexFactory();

    Mesh build();

}