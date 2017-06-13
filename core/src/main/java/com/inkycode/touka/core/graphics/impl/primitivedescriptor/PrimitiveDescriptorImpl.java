package com.inkycode.touka.core.graphics.impl.primitivedescriptor;

import com.inkycode.touka.core.bootstrap.annotations.Inject;
import com.inkycode.touka.core.bootstrap.annotations.Source;
import com.inkycode.touka.core.graphics.PrimitiveDescriptor;

public class PrimitiveDescriptorImpl implements PrimitiveDescriptor {

    @Inject
    @Source("property")
    private int size;

    @Override
    public int getSize() {
        return this.size;
    }

}