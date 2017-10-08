package com.inkycode.touka.core.graphics;

import com.inkycode.touka.core.graphics.primitive.Primitive;
import com.inkycode.touka.core.graphics.primitive.PrimitiveFactory;
import com.inkycode.touka.core.graphics.vertex.Vertex;
import com.inkycode.touka.core.graphics.vertex.VertexFactory;

public interface MeshFactory {

    void addVertex(final Vertex vertex);

    void addPrimitive(final Primitive primitive);

    VertexFactory getVertexFactory();

    PrimitiveFactory getPrimitiveFactory();

    Mesh build();

}