package com.inkycode.touka.core.application.impl;

import static com.inkycode.touka.core.bootstrap.impl.ComponentFactoryImpl.DEFAULT_CONFIGURATION_PATH;

import org.slf4j.Logger;

import com.inkycode.touka.core.application.Application;
import com.inkycode.touka.core.bootstrap.annotations.Activate;
import com.inkycode.touka.core.bootstrap.annotations.Inject;
import com.inkycode.touka.core.bootstrap.annotations.Source;
import com.inkycode.touka.core.bootstrap.impl.ComponentFactoryImpl;
import com.inkycode.touka.core.graphics.Renderer;
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

    @Inject
    @Source("component")
    private Renderer renderer;

    @Activate
    @Override
    public void start() {
        this.log.info("Application started");

        this.run();
    }

    public void run() {
        this.startup();

        while (!this.canvas.isClosing()) {
            renderer.clearBuffers();

            canvas.swapBuffers();

            platform.update();
        }

        this.shutdown();
    }

    private void startup() {
        platform.initialise();

        canvas.initialise();
        
        renderer.initialise();
    }

    private void shutdown() {
        renderer.deinitialise();

        canvas.deinitialise();

        platform.deinitialise();
    }

}