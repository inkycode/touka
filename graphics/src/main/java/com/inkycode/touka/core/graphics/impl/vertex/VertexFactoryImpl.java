package com.inkycode.touka.core.graphics.impl.vertex;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.inkycode.touka.core.bootstrap.annotations.Activate;
import com.inkycode.touka.core.bootstrap.annotations.Inject;
import com.inkycode.touka.core.bootstrap.annotations.Named;
import com.inkycode.touka.core.bootstrap.annotations.Source;
import com.inkycode.touka.core.bootstrap.annotations.Via;
import com.inkycode.touka.core.graphics.vertex.Vertex;
import com.inkycode.touka.core.graphics.vertex.VertexAttributeDescriptor;
import com.inkycode.touka.core.graphics.vertex.VertexFactory;

public class VertexFactoryImpl implements VertexFactory {

    private Map<Integer, Object> vertexAttributes;

    @Inject
    @Source("component")
    @Via("property")
    @Named("vertexAttributeDescriptors")
    private Set<VertexAttributeDescriptor> vertexAttributeDescriptors;

    @Inject
    @Source("property")
    private String vertexClass;

    @Activate
    public void activate() {
        this.vertexAttributes = new TreeMap<Integer, Object>();
    }

    @Override
    public void setAttribute(final int position, final Object value) {
        this.vertexAttributes.put(position, value);
    }

    @Override
    public <T> T getAttribute(final int position, final Class<T> type) {
        try {
            return type.cast(this.vertexAttributes.get(position));
        } catch (ClassCastException e) {
            // TODO: Handle errors
        }
        
        return null;
    }

    @Override
    public Set<VertexAttributeDescriptor> getVertexAttributeDescriptors() {
        return this.vertexAttributeDescriptors;
    }

    @Override
    public Vertex build() {
        
        try {
            final Class<? extends Vertex> vertexClass = Class.forName(this.vertexClass).asSubclass(Vertex.class);

            return vertexClass.getDeclaredConstructor(Map.class).newInstance(this.vertexAttributes);
        } catch (ReflectiveOperationException e) {
            // TODO: Handle errors
        }

        return null;
    }

}