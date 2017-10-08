package com.inkycode.touka.core.graphics.impl.primitive;

import com.inkycode.touka.core.bootstrap.annotations.Activate;
import com.inkycode.touka.core.bootstrap.annotations.Inject;
import com.inkycode.touka.core.bootstrap.annotations.Named;
import com.inkycode.touka.core.bootstrap.annotations.Source;
import com.inkycode.touka.core.bootstrap.annotations.Via;
import com.inkycode.touka.core.graphics.primitive.Primitive;
import com.inkycode.touka.core.graphics.primitive.PrimitiveDescriptor;
import com.inkycode.touka.core.graphics.primitive.PrimitiveFactory;

public class PrimitiveFactoryImpl implements PrimitiveFactory {

    private int[] indices;

    @Inject
    @Source("component")
    @Via("property")
    @Named("primitiveDescriptor")
    private PrimitiveDescriptor primitiveDescriptor;

    @Inject
    @Source("property")
    private String primitiveClass;

    @Activate
    public void activate() {
        this.indices = new int[primitiveDescriptor.getSize()];
    }

    @Override
    public void setIndex(final int position, final int index) {
        this.indices[position] = index;
    }

    @Override
    public PrimitiveDescriptor getPrimitiveDescriptor() {
        return this.primitiveDescriptor;
    }

    @Override
    public Primitive build() {
        try {
            final Class<? extends Primitive> primitiveClass = Class.forName(this.primitiveClass).asSubclass(Primitive.class);

            return primitiveClass.getDeclaredConstructor(int[].class).newInstance(this.indices);
        } catch (ReflectiveOperationException e) {
            // TODO: Handle errors
        }

        return null;
    }

}