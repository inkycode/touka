package com.inkycode.touka.core.graphics.impl.primitive;

import com.inkycode.touka.core.graphics.primitive.Primitive;

public class PrimitiveImpl implements Primitive {

    final private int[] indices;

    public PrimitiveImpl(final int[] indices) {
        this.indices = new int[indices.length];

        for (int i = 0; i < indices.length; i ++) {
            this.indices[i] = indices[i];
        }
    }

    @Override
    public int[] getIndices() {
        return this.indices;
    }

}