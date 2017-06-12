package com.inkycode.touka.core.graphics.impl.vertex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.joml.Vector3f;

import com.inkycode.touka.core.bootstrap.annotations.Activate;
import com.inkycode.touka.core.bootstrap.annotations.Inject;
import com.inkycode.touka.core.bootstrap.annotations.Named;
import com.inkycode.touka.core.bootstrap.annotations.Source;
import com.inkycode.touka.core.bootstrap.annotations.Via;
import com.inkycode.touka.core.graphics.Vertex;
import com.inkycode.touka.core.graphics.VertexAttributeDescriptor;
import com.inkycode.touka.core.graphics.VertexFactory;

public class Position3VertexFactory implements VertexFactory {

    // @Inject
    // @Source("component")
    // private Map<String, VertexAttributeDescriptor> vertexAttributeDescriptorPool;

    private Map<Integer, Object> attributes;

    @Inject
    @Source("component")
    @Via("property")
    @Named("vertexAttributeDescriptors")
    private Set<VertexAttributeDescriptor> vertexAttributeDescriptors;

    @Activate
    public void activate() {
        this.attributes = new TreeMap<Integer, Object>();

        // this.vertexAttributeDescriptors = new HashSet<VertexAttributeDescriptor>();
        // vertexAttributeDescriptors.add(vertexAttributeDescriptorPool.get("position3"));
    }

    @Override
    public <T> void setAttribute(final int position, final T value, final Class<T> type) {
        this.attributes.put(position, value);
    }

    @Override
    public Vertex build() {
        return new Position3Vertex(this.getAttribute(0, Vector3f.class));
    }

    @Override
    public <T> T getAttribute(final int position, final Class<T> type) {
        return type.cast(this.attributes.get(position));
    }

    @Override
    public int getVertexSize() {
        return 3;
    }

    @Override
    public Set<VertexAttributeDescriptor> getVertexAttributeDescriptors() {
        return this.vertexAttributeDescriptors;
    }


}