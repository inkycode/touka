package com.inkycode.touka.core.graphics.impl.opengl;

import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

import com.inkycode.touka.core.graphics.Renderer;

public class OpenGLRenderer implements Renderer {

    @Override
    public void initialise() {
        createCapabilities();
    }

    @Override
    public void deinitialise() {

    }

    @Override
    public void clearBuffers() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void setClearColor(int red, int green, int blue) {
        glClearColor(red / 255f, green / 255f, blue / 255f, 1.0f);
    }

    @Override
    public void setViewport(int x, int y, int width, int height) {
        glViewport(x, y, width, height);
    }

}