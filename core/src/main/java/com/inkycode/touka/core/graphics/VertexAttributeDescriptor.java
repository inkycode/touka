package com.inkycode.touka.core.graphics;

public interface VertexAttributeDescriptor {

    public static final int DATA_TYPE_FLOAT = 0x01;
    public static final int DATA_TYPE_INTEGER = 0x02;

    int getSize();

    int getOffset();

    int getIndex();

    int getDataType();

}