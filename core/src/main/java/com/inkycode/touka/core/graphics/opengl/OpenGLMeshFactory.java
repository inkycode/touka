package com.inkycode.touka.core.graphics.opengl;

import java.util.ArrayList;
import java.util.List;

import com.inkycode.touka.core.bootstrap.annotations.Activate;
import com.inkycode.touka.core.graphics.Mesh;
import com.inkycode.touka.core.graphics.MeshFactory;
import com.inkycode.touka.core.graphics.Polygon;
import com.inkycode.touka.core.graphics.Vertex;

public class OpenGLMeshFactory implements MeshFactory {

    private List<Vertex> vertices;

    private List<Polygon> polygons;


    @Activate
    public void activate() {
        this.vertices = new ArrayList<Vertex>();

        this.polygons = new ArrayList<Polygon>();
    }

    @Override
    public void addVertex(Vertex vertex) {
        this.vertices.add(vertex);
    }

    @Override
    public void addPolygon(Polygon polygon) {
        this.polygons.add(polygon);
    }

    @Override
    public Mesh build() {
        return new OpenGLMesh(this.vertices, this.polygons);
    }

}