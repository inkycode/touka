package com.inkycode.touka.core.graphics.impl.polygon;

import java.util.ArrayList;
import java.util.List;

import com.inkycode.touka.core.graphics.Polygon;

public class Triangle implements Polygon {

    final private List<Integer> indices;

    public Triangle(final int index0, final int index1, final int index2) {
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