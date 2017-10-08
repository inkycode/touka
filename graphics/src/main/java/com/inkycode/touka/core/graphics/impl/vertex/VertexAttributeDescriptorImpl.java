package com.inkycode.touka.core.graphics.impl.vertex;

import com.inkycode.touka.core.bootstrap.annotations.Inject;
import com.inkycode.touka.core.bootstrap.annotations.Source;
import com.inkycode.touka.core.graphics.vertex.VertexAttributeDescriptor;

public class VertexAttributeDescriptorImpl implements VertexAttributeDescriptor {

    @Inject
    @Source("property")
    private int size;

    @Inject
    @Source("property")
    private int offset;

    @Inject
    @Source("property")
    private int index;

    @Inject
    @Source("property")
    private int dataType;


    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public int getOffset() {
        return this.offset;
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    @Override
    public int getDataType() {
        return this.dataType;
    }

}