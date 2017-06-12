package com.inkycode.touka.core.graphics.impl.polygon;

import java.util.ArrayList;
import java.util.List;

import com.inkycode.touka.core.graphics.Polygon;

public class Triangle implements Polygon {

    private List<Integer> indices;

    public Triangle(int index0, int index1, int index2) {
        this.indices = new ArrayList<Integer>();

        this.indices.add(index0);
        this.indices.add(index1);
        this.indices.add(index2);
    }

    @Override
    public List<Integer> getIndices() {
        return this.indices;
    }

}