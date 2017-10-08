package com.inkycode.touka.core.graphics.vertex;

import java.util.Set;

public interface VertexFactory {

    <T> void setAttribute(final int position, final T value);

    <T> T getAttribute(final int position, final Class<T> type);

    Vertex build();

    Set<VertexAttributeDescriptor> getVertexAttributeDescriptors();

}