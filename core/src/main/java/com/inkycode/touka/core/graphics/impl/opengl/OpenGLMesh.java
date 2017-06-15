package com.inkycode.touka.core.graphics.impl.opengl;

import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_INT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

import static org.lwjgl.system.MemoryStack.stackPush;

import static com.inkycode.touka.core.graphics.vertex.VertexAttributeDescriptor.DATA_TYPE_FLOAT;
import static com.inkycode.touka.core.graphics.vertex.VertexAttributeDescriptor.DATA_TYPE_INTEGER;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.Set;

import org.lwjgl.system.MemoryStack;

import com.inkycode.touka.core.graphics.Mesh;
import com.inkycode.touka.core.graphics.primitive.Primitive;
import com.inkycode.touka.core.graphics.primitive.PrimitiveDescriptor;
import com.inkycode.touka.core.graphics.vertex.Vertex;
import com.inkycode.touka.core.graphics.vertex.VertexAttributeDescriptor;

public class OpenGLMesh implements Mesh {

    private final int vaoHandle;

    private final int vboIndexHandle;

    private final int[] vboAttributeHandles;

    private final int vertexCount, indexCount, primitiveCount;

    public OpenGLMesh(final List<Vertex> vertices, final List<Primitive> primitives, final Set<VertexAttributeDescriptor> vertexAttributeDescriptors, final PrimitiveDescriptor primitiveDescriptor) {
        try (MemoryStack stack = stackPush()) {
            final int bufferCount = vertexAttributeDescriptors.size();

            this.vertexCount = vertices.size();
            this.primitiveCount = primitives.size();
            this.indexCount = this.primitiveCount * primitiveDescriptor.getSize();

            this.vaoHandle = glGenVertexArrays();
            glBindVertexArray(this.vaoHandle);

            this.vboAttributeHandles = new int[bufferCount];
            glGenBuffers(this.vboAttributeHandles);

            for (final VertexAttributeDescriptor vertexAttributeDescriptor : vertexAttributeDescriptors) {
                final int vboAttributeHandle = this.vboAttributeHandles[vertexAttributeDescriptor.getIndex()];
                final int bufferSize = vertices.size() * vertexAttributeDescriptor.getSize();
                
                if (vertexAttributeDescriptor.getDataType() == DATA_TYPE_FLOAT) {
                    this.bindFloatVertexBufferAttribute(stack, vertexAttributeDescriptor, vertices, bufferSize, vboAttributeHandle);
                } else if (vertexAttributeDescriptor.getDataType() == DATA_TYPE_INTEGER) {
                    this.bindIntegerVertexBufferAttribute(stack, vertexAttributeDescriptor, vertices, bufferSize, vboAttributeHandle);
                }
            }

            this.vboIndexHandle = glGenBuffers();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.vboIndexHandle);

            final IntBuffer indexBuffer = stack.mallocInt(this.indexCount);
            for (final Primitive primitive : primitives) {
                indexBuffer.put(primitive.getIndices());
            }
            
            indexBuffer.flip();

            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

            glBindVertexArray(0);
        }
    }

    @Override
    public int getVertexCount() {
        return this.vertexCount;
    }

    @Override
    public int getIndexCount() {
        return this.indexCount;
    }

    @Override
    public int getPrimitiveCount() {
        return this.primitiveCount;
    }

    @Override
    public void render() {
        glBindVertexArray(this.vaoHandle);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.vboIndexHandle);

        glDrawElements(GL_TRIANGLES, this.indexCount, GL_UNSIGNED_INT, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    @Override
    public void destroy() {
        glDeleteVertexArrays(this.vaoHandle);
        glBindVertexArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(this.vboAttributeHandles);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDeleteBuffers(this.vboIndexHandle);
    }

    private void bindFloatVertexBufferAttribute(final MemoryStack stack, final VertexAttributeDescriptor vertexAttributeDescriptor, final List<Vertex> vertices, final int bufferSize, final int vboAttributeHandle) {
        final FloatBuffer buffer = stack.mallocFloat(bufferSize);

        for (final Vertex vertex : vertices) {
            final int offset = vertexAttributeDescriptor.getOffset();
            for (int index = 0; index < vertexAttributeDescriptor.getSize(); index ++) {
                buffer.put(vertex.getAttribute(offset, float[].class)[index]);
            }
        }

        buffer.flip();

        glBindBuffer(GL_ARRAY_BUFFER, vboAttributeHandle);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

        glEnableVertexAttribArray(vertexAttributeDescriptor.getIndex());
        glVertexAttribPointer(vertexAttributeDescriptor.getIndex(), vertexAttributeDescriptor.getSize(), GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private void bindIntegerVertexBufferAttribute(final MemoryStack stack, final VertexAttributeDescriptor vertexAttributeDescriptor, final List<Vertex> vertices, final int bufferSize, final int vboAttributeHandle) {
        final IntBuffer buffer = stack.mallocInt(bufferSize);

        for (final Vertex vertex : vertices) {
            final int offset = vertexAttributeDescriptor.getOffset();
            for (int index = 0; index < vertexAttributeDescriptor.getSize(); index ++) {
                buffer.put(vertex.getAttribute(offset, int[].class)[index]);
            }
        }

        buffer.flip();

        glBindBuffer(GL_ARRAY_BUFFER, vboAttributeHandle);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

        glEnableVertexAttribArray(vertexAttributeDescriptor.getIndex());
        glVertexAttribPointer(vertexAttributeDescriptor.getIndex(), vertexAttributeDescriptor.getSize(), GL_INT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
}