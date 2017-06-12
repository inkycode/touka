package com.inkycode.touka.core.graphics;

public interface VertexFactory {

    <T> void setAttribute(final int position, final T value, final Class<T> type);

    <T> T getAttribute(final int position, final Class<T> type);

    Vertex build();

}