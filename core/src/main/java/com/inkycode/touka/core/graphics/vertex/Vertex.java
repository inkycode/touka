package com.inkycode.touka.core.graphics.vertex;

public interface Vertex {
    
    <T> T getAttribute(final int index, Class<T> type);
    
}