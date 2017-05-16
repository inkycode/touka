package com.inkycode.touka.components.impl;

import static com.inkycode.touka.core.bootstrap.impl.ComponentFactoryImpl.DEFAULT_CONFIGURATION_PATH;

import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

import org.slf4j.Logger;

import com.inkycode.touka.Application;
import com.inkycode.touka.core.bootstrap.annotations.Activate;
import com.inkycode.touka.core.bootstrap.annotations.Inject;
import com.inkycode.touka.core.bootstrap.annotations.Source;
import com.inkycode.touka.core.bootstrap.impl.ComponentFactoryImpl;
import com.inkycode.touka.core.platform.Canvas;
import com.inkycode.touka.core.platform.Platform;

public class ApplicationImpl implements Application {

    @Inject
    @Source("logger")
    private Logger log;

    @Inject
    @Source("component")
    private Platform platform;

    @Inject
    @Source("component")
    private Canvas canvas;

    @Activate
    @Override
    public void start() {
        this.log.info("Application started");

        this.run();
    }

    public void run() {
        this.startup();

        while (!this.canvas.isClosing()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            canvas.swapBuffers();

            platform.update();
        }

        this.shutdown();
    }

    private void startup() {
        platform.initialise();

        canvas.initialise();
        
        createCapabilities();
    }

    private void shutdown() {
        canvas.deinitialise();

        platform.deinitialise();
    }

    public static void main(final String[] args) {
        new ComponentFactoryImpl().initialise(DEFAULT_CONFIGURATION_PATH);
    }

}