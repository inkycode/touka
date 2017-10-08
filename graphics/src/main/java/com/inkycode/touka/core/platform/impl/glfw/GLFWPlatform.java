package com.inkycode.touka.core.platform.impl.glfw;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;

import com.inkycode.touka.core.platform.Platform;

public class GLFWPlatform implements Platform {

    @Override
    public void initialise() {
        glfwInit();
    }

    @Override
    public void deinitialise() {
        glfwTerminate();
    }

    @Override
    public void update() {
        glfwPollEvents();
    }

}