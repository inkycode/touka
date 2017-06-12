package com.inkycode.touka.core.graphics.impl.vertex;

import java.util.Map;
import java.util.TreeMap;

import org.joml.Vector3f;

import com.inkycode.touka.core.bootstrap.annotations.Activate;
import com.inkycode.touka.core.graphics.Vertex;
import com.inkycode.touka.core.graphics.VertexFactory;

public class Position3VertexFactory implements VertexFactory {

    private Map<Integer, Object> attributes;

    @Activate
    public void activate() {
        this.attributes = new TreeMap<Integer, Object>();
    }

    public <T> void setAttribute(final int position, final T value, final Class<T> type) {
        this.attributes.put(position, value);
    }

    public Vertex build() {
        return new Position3Vertex(this.getAttribute(0, Vector3f.class));
    }

    public <T> T getAttribute(final int position, final Class<T> type) {
        return type.cast(this.attributes.get(position));
    }


}