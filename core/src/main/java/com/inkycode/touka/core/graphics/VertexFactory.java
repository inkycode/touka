package com.inkycode.touka.core.graphics;

import java.util.Set;

public interface VertexFactory {

    <T> void setAttribute(final int position, final T value);

    <T> T getAttribute(final int position, final Class<T> type);

    Vertex build();

    int getVertexSize();

    Set<VertexAttributeDescriptor> getVertexAttributeDescriptors();

}