package com.inkycode.touka.core.graphics.impl;

import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.glClear;
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

}