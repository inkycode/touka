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
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_INT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

import static org.lwjgl.system.MemoryStack.stackPush;

import static com.inkycode.touka.core.graphics.VertexAttributeDescriptor.DATA_TYPE_FLOAT;
import static com.inkycode.touka.core.graphics.VertexAttributeDescriptor.DATA_TYPE_INTEGER;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.system.MemoryStack;

import com.inkycode.touka.core.graphics.Mesh;
import com.inkycode.touka.core.graphics.Polygon;
import com.inkycode.touka.core.graphics.Vertex;
import com.inkycode.touka.core.graphics.VertexAttributeDescriptor;
import com.inkycode.touka.core.graphics.VertexFactory;

public class OpenGLMesh implements Mesh {

    private final int vaoHandle;

    private final int vertexCount;

    private final int[] vboAttributeHandles;

    public OpenGLMesh(final List<Vertex> vertices, final List<Polygon> polygons, final Set<VertexAttributeDescriptor> vertexAttributeDescriptors) {
        //this.vboAttributeHandles = new HashSet<Integer>();
        
        // TODO: Index buffers?
        try (MemoryStack stack = stackPush()) {
            final int bufferCount = vertexAttributeDescriptors.size();

            this.vertexCount = vertices.size();
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

                glBindBuffer(GL_ARRAY_BUFFER, 0);
            }

            glBindVertexArray(0);
        }
    }

    @Override
    public void render() {
        glBindVertexArray(this.vaoHandle);

        glDrawArrays(GL_TRIANGLES, 0, this.vertexCount);
    }

    @Override
    public void destroy() {
        glDeleteVertexArrays(this.vaoHandle);
        glBindVertexArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(this.vboAttributeHandles);
    }

    private void bindFloatVertexBufferAttribute(final MemoryStack stack, final VertexAttributeDescriptor vertexAttributeDescriptor, final List<Vertex> vertices, final int bufferSize, final int vboAttributeHandle) {
        glBindBuffer(GL_ARRAY_BUFFER, vboAttributeHandle);
        glEnableVertexAttribArray(vertexAttributeDescriptor.getIndex());

        final FloatBuffer buffer = stack.mallocFloat(bufferSize);

        for (final Vertex vertex : vertices) {
            final int offset = vertexAttributeDescriptor.getOffset();
            for (int index = 0; index < vertexAttributeDescriptor.getSize(); index ++) {
                buffer.put(vertex.getAttribute(offset, float[].class)[index]);
            }
        }

        buffer.flip();

        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(vertexAttributeDescriptor.getIndex(), vertexAttributeDescriptor.getSize(), GL_FLOAT, false, 0, 0);
    }

    private void bindIntegerVertexBufferAttribute(final MemoryStack stack, final VertexAttributeDescriptor vertexAttributeDescriptor, final List<Vertex> vertices, final int bufferSize, final int vboAttributeHandle) {
        glBindBuffer(GL_ARRAY_BUFFER, vboAttributeHandle);
        glEnableVertexAttribArray(vertexAttributeDescriptor.getIndex());

        final IntBuffer buffer = stack.mallocInt(bufferSize);

        for (final Vertex vertex : vertices) {
            final int offset = vertexAttributeDescriptor.getOffset();
            for (int index = 0; index < vertexAttributeDescriptor.getSize(); index ++) {
                buffer.put(vertex.getAttribute(offset, int[].class)[index]);
            }
        }

        buffer.flip();

        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(vertexAttributeDescriptor.getIndex(), vertexAttributeDescriptor.getSize(), GL_INT, false, 0, 0);
    }
}