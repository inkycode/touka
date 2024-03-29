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
import com.inkycode.touka.core.graphics.primitive.Primitive;
import com.inkycode.touka.core.graphics.primitive.PrimitiveFactory;
import com.inkycode.touka.core.graphics.vertex.Vertex;
import com.inkycode.touka.core.graphics.vertex.VertexFactory;

public class OpenGLMeshFactory implements MeshFactory {

    @Inject
    @Source("logger")
    private Logger log;

    @Inject
    @Source("component")
    @Via("property")
    @Named("vertexFactory")
    private VertexFactory vertexFactory;

    @Inject
    @Source("component")
    @Via("property")
    @Named("primitiveFactory")
    private PrimitiveFactory primitiveFactory;

    private List<Vertex> vertices;

    private List<Primitive> primitives;

    @Activate
    public void activate() {
        this.vertices = new ArrayList<Vertex>();

        this.primitives = new ArrayList<Primitive>();
    }

    @Override
    public void addVertex(final Vertex vertex) {
        this.vertices.add(vertex);
    }

    @Override
    public void addPrimitive(final Primitive primitive) {
        this.primitives.add(primitive);
    }

    @Override
    public VertexFactory getVertexFactory() {
        return this.vertexFactory;
    }

    @Override
    public PrimitiveFactory getPrimitiveFactory() {
        return this.primitiveFactory;
    }

    @Override
    public Mesh build() {
        return new OpenGLMesh(this.vertices, this.primitives, this.vertexFactory.getVertexAttributeDescriptors(), this.primitiveFactory.getPrimitiveDescriptor());
    }

}