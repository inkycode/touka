package com.inkycode.touka.core.graphics;

public interface Vertex {
    
    <T> T get(final int index, Class<T> type);
    
}