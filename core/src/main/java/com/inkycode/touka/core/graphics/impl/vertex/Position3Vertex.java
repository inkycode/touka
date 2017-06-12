package com.inkycode.touka.core.graphics.impl.vertex;

import com.inkycode.touka.core.graphics.Vertex;

import org.joml.Vector3f;

public class Position3Vertex implements Vertex {

    private Vector3f position;

    public Position3Vertex(Vector3f position) {
        this.position = position;
    }

    @Override
    public float get(int index) {
        return position.get(index);
    }

}