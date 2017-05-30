package com.inkycode.touka.core.graphics.opengl;

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
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

import static org.lwjgl.system.MemoryStack.stackPush;

import java.nio.FloatBuffer;
import java.util.List;

import org.lwjgl.system.MemoryStack;

import com.inkycode.touka.core.graphics.Mesh;
import com.inkycode.touka.core.graphics.Polygon;
import com.inkycode.touka.core.graphics.Vertex;

public class OpenGLMesh implements Mesh {

    private int vboHandle, vaoHandle;

    private int vertexCount;

    public OpenGLMesh(List<Vertex> vertices, List<Polygon> polygons) {
        // TODO: Index buffers?
        try (MemoryStack stack = stackPush()) {
            FloatBuffer vertexBuffer = stack.mallocFloat(vertices.size() * 3);
            
            for (Vertex vertex : vertices) {
                vertexBuffer.put(vertex.get(0));
                vertexBuffer.put(vertex.get(1));
                vertexBuffer.put(vertex.get(2));
            }
            vertexBuffer.flip();

            this.vertexCount = vertices.size();

            this.vaoHandle = glGenVertexArrays();
            this.vboHandle = glGenBuffers();

            glBindVertexArray(this.vaoHandle);
            glBindBuffer(GL_ARRAY_BUFFER, this.vboHandle);
            glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

            // TODO: How should this be handled? Maybe all this stuff should just go inside the
            // MeshFactory...
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
            glEnableVertexAttribArray(0);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
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
        glDeleteBuffers(this.vboHandle);
        glDeleteVertexArrays(this.vaoHandle);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }
}