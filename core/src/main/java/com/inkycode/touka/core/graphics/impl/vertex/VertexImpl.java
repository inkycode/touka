package com.inkycode.touka.core.graphics.impl.vertex;

import com.inkycode.touka.core.graphics.Vertex;

import java.util.Map;
import java.util.TreeMap;

public class VertexImpl implements Vertex {

    final private Map<Integer, Object> attributes;

    public VertexImpl(final Map<Integer, Object> attributes) {
        this.attributes = new TreeMap<Integer, Object>(attributes);
    }

    @Override
    public <T> T getAttribute(final int index, Class<T> type) {

        try {
            return type.cast(attributes.get(index));
        } catch (ClassCastException e) {
            // TODO: Handle errors
        }

        return null;
    }

}