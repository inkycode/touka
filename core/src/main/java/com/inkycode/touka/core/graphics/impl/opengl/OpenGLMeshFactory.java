package com.inkycode.touka.core.graphics.impl.opengl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.inkycode.touka.core.bootstrap.annotations.Activate;
import com.inkycode.touka.core.bootstrap.annotations.Inject;
import com.inkycode.touka.core.bootstrap.annotations.Named;
import com.inkycode.touka.core.bootstrap.annotations.Source;
import com.inkycode.touka.core.bootstrap.annotations.Via;
import com.inkycode.touka.core.graphics.Mesh;
import com.inkycode.touka.core.graphics.MeshFactory;
import com.inkycode.touka.core.graphics.Polygon;
import com.inkycode.touka.core.graphics.Vertex;
import com.inkycode.touka.core.graphics.VertexFactory;

public class OpenGLMeshFactory implements MeshFactory {

    @Inject
    @Source("logger")
    private Logger log;

    @Inject
    @Source("component")
    @Via("property")
    @Named("vertexFactory")
    private VertexFactory vertexFactory;

    private List<Vertex> vertices;

    private List<Polygon> polygons;

    @Activate
    public void activate() {
        this.vertices = new ArrayList<Vertex>();

        this.polygons = new ArrayList<Polygon>();

        log.error("VertexFactory = {}", this.vertexFactory);
    }

    @Override
    public void addVertex(final Vertex vertex) {
        this.vertices.add(vertex);
    }

    @Override
    public void addPolygon(final Polygon polygon) {
        this.polygons.add(polygon);
    }

    @Override
    public Mesh build() {
        return new OpenGLMesh(this.vertices, this.polygons);
    }

}