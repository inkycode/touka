package com.inkycode.touka.core.graphics;

public interface MeshFactory {

    void addVertex(Vertex vertex);

    void addPolygon(Polygon polygon);

    Mesh build();

}