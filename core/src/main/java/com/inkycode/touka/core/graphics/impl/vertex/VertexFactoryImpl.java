package com.inkycode.touka.core.graphics.impl.vertex;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.inkycode.touka.core.bootstrap.annotations.Activate;
import com.inkycode.touka.core.bootstrap.annotations.Inject;
import com.inkycode.touka.core.bootstrap.annotations.Named;
import com.inkycode.touka.core.bootstrap.annotations.Source;
import com.inkycode.touka.core.bootstrap.annotations.Via;
import com.inkycode.touka.core.graphics.Vertex;
import com.inkycode.touka.core.graphics.VertexAttributeDescriptor;
import com.inkycode.touka.core.graphics.VertexFactory;

public class VertexFactoryImpl implements VertexFactory {

    private Map<Integer, Object> vertexAttributes;

    @Inject
    @Source("component")
    @Via("property")
    @Named("vertexAttributeDescriptors")
    private Set<VertexAttributeDescriptor> vertexAttributeDescriptors;

    @Inject
    @Source("property")
    private String vertexClazz;

    @Inject
    @Source("property")
    private int vertexSize;

    @Activate
    public void activate() {
        this.vertexAttributes = new TreeMap<Integer, Object>();
    }

    @Override
    public <T> void setAttribute(final int position, final T value, final Class<T> type) {
        this.vertexAttributes.put(position, value);
    }

    @Override
    public Vertex build() {
        
        try {
            final Class<? extends Vertex> vertexClazz = Class.forName(this.vertexClazz).asSubclass(Vertex.class);

            return vertexClazz.getDeclaredConstructor(Map.class).newInstance(this.vertexAttributes);
        } catch (ReflectiveOperationException e) {
            // TODO: Handle errors
        }

        return null;
    }

    @Override
    public <T> T getAttribute(final int position, final Class<T> type) {
        return type.cast(this.vertexAttributes.get(position));
    }

    @Override
    public int getVertexSize() {
        return this.vertexSize;
    }

    @Override
    public Set<VertexAttributeDescriptor> getVertexAttributeDescriptors() {
        return this.vertexAttributeDescriptors;
    }


}