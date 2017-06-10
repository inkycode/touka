package com.inkycode.touka.core.graphics;

public interface VertexFactory {

    <T> void setAttribute(int position, T value, Class<T> type);

    <T> T getAttribute(int position, Class<T> type);

    Vertex build();

}