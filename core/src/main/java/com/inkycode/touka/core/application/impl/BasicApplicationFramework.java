package com.inkycode.touka.core.application.impl;

import org.slf4j.Logger;

import com.inkycode.touka.core.application.Application;
import com.inkycode.touka.core.application.ScreenManager;
import com.inkycode.touka.core.bootstrap.annotations.Activate;
import com.inkycode.touka.core.bootstrap.annotations.Inject;
import com.inkycode.touka.core.bootstrap.annotations.Source;
import com.inkycode.touka.core.graphics.Renderer;
import com.inkycode.touka.core.platform.Canvas;
import com.inkycode.touka.core.platform.Platform;

public class BasicApplicationFramework implements Application {

    @Inject
    @Source("logger")
    private Logger log;

    @Inject
    @Source("property")
    private String initialScreenName;

    @Inject
    @Source("component")
    private Platform platform;

    @Inject
    @Source("component")
    private Canvas canvas;

    @Inject
    @Source("component")
    private Renderer renderer;

    @Inject
    @Source("component")
    private ScreenManager screenManager;

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

            screenManager.getActiveScreen().render();

            screenManager.getActiveScreen().update();

            canvas.swapBuffers();

            platform.update();
        }

        this.shutdown();
    }

    private void startup() {
        platform.initialise();

        canvas.initialise();
        
        renderer.initialise();

        screenManager.setActiveScreen(this.initialScreenName);
    }

    private void shutdown() {
        renderer.deinitialise();

        canvas.deinitialise();

        platform.deinitialise();
    }

}