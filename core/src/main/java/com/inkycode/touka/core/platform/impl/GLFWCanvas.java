package com.inkycode.touka.core.platform.impl;

import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;

import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFWVidMode;
import org.slf4j.Logger;

import com.inkycode.touka.core.bootstrap.annotations.Default;
import com.inkycode.touka.core.bootstrap.annotations.Inject;
import com.inkycode.touka.core.bootstrap.annotations.Source;
import com.inkycode.touka.core.platform.Canvas;

public class GLFWCanvas implements Canvas {

    public static final long DISPLAY_MODE_UNKOWN = 0x00;
    public static final long DISPLAY_MODE_WINDOW = 0x01;
    public static final long DISPLAY_MODE_BORDERLESS_WINDOW = 0x02;

    public static final long MONITOR_NULL = NULL;

    private long handle;

    private int width;

    private int height;

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
    @Default(longValue = MONITOR_NULL)
    private long monitor;

    @Inject
    @Source("property")
    @Default(booleanValue = true)
    private boolean vsync;

    @Override
    public void initialise() {
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        if (this.displayMode == DISPLAY_MODE_BORDERLESS_WINDOW) {
            GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            this.width = videoMode.width();
            this.height = videoMode.height();

            this.monitor = this.monitor == MONITOR_NULL ? glfwGetPrimaryMonitor() : this.monitor;
        } else {

            this.width = 300;
            this.height = 300;

            this.monitor = NULL;
        }
        this.handle = glfwCreateWindow(this.width, this.height, this.title, this.monitor, NULL);

        glfwMakeContextCurrent(this.handle);

        glfwSwapInterval(this.vsync ? 1 : 0);

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

}