package com.inkycode.touka.core.graphics.impl.vertex;

import com.inkycode.touka.core.graphics.Vertex;

import java.util.Map;

import org.joml.Vector3f;

public class Position3Vertex implements Vertex {

    final private Vector3f position;

    public Position3Vertex(final Map<Integer, Object> attributes) {
        this.position = (Vector3f) attributes.get(0);
    }

    // public Position3Vertex(final Vector3f position) {
    //     this.position = position;
    // }

    @Override
    public float get(final int index) {
        return position.get(index);
    }

}