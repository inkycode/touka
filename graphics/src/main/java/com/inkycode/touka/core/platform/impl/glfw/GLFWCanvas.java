package com.inkycode.touka.core.platform.impl.glfw;

import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;

import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFWVidMode;

import com.inkycode.touka.core.bootstrap.annotations.Default;
import com.inkycode.touka.core.bootstrap.annotations.Inject;
import com.inkycode.touka.core.bootstrap.annotations.Source;
import com.inkycode.touka.core.platform.Canvas;

public class GLFWCanvas implements Canvas {

    @Inject
    @Source("property")
    @Default(stringValue = "GLFWCanvas")
    private String title;

    @Inject
    @Source("property")
    @Default(longValue = DISPLAY_MODE_BORDERLESS_WINDOW)
    private long displayMode;

    @Inject
    @Source("property")
    @Default(longValue = NULL)
    private long monitor;

    @Inject
    @Source("property")
    @Default(booleanValue = true)
    private boolean vsync;

    private long handle;

    private int width;

    private int height;

    @Override
    public void initialise() {
    	System.out.println("here 1");
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        System.out.println("here 2");
//        if (this.displayMode == DISPLAY_MODE_BORDERLESS_WINDOW) {
            final GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            System.out.println("here 3");
            this.width = videoMode.width();
            this.height = videoMode.height();
            System.out.println("here 4");
            this.monitor = this.monitor == NULL ? glfwGetPrimaryMonitor() : this.monitor;
//        } else {
//            this.width = 1024;
//            this.height = 768;
//            System.out.println("here 5");
//            this.monitor = NULL;
//        }
        this.handle = glfwCreateWindow(this.width, this.height, this.title, this.monitor, NULL);
        System.out.println("here 6");
        glfwMakeContextCurrent(this.handle);
        System.out.println("here 7");
        glfwSwapInterval(this.vsync ? 1 : 0);
        System.out.println("here 8");
        glfwShowWindow(this.handle);
    }

    @Override
    public void deinitialise() {
        glfwDestroyWindow(this.handle);
    }

    @Override
    public void swapBuffers() {
        glfwSwapBuffers(this.handle);
    }

    @Override
    public boolean isClosing() {
        return glfwWindowShouldClose(this.handle);
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

}