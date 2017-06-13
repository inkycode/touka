package com.inkycode.touka.core.graphics;

public interface PrimitiveFactory {

    PrimitiveDescriptor getPrimitiveDescriptor();

    void setIndex(final int position, final int index);

    Primitive build();

}