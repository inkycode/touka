package com.inkycode.touka.core.graphics;

public interface Vertex {
    
    <T> T getAttribute(final int index, Class<T> type);
    
}