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
        log.info("Starting application");

        log.info("Initialising platform");
        platform.initialise();
        log.info("Platform initialised");

        log.info("Initialising canvas");
        canvas.initialise();
        log.info("Canvas initialised");
        
        log.info("Initialising renderer");
        renderer.initialise();
        log.info("Renderer initialised");

        renderer.setViewport(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
        renderer.setClearColor(0, 155, 255);

        log.info("Setting active screen to {}", this.initialScreenName);
        screenManager.setActiveScreen(this.initialScreenName);

        log.info("Application successfully started");
    }

    private void shutdown() {
        log.info("Shutting down application");

        if (screenManager.getActiveScreen() != null) {
            log.info("Unloading active screen {}", screenManager.getActiveScreen().getClass().getName());
            screenManager.getActiveScreen().unload();
        }

        log.info("Deinitialising renderer");
        renderer.deinitialise();
        log.info("Renderer deinitialised");

        log.info("Deinitialising canvas");
        canvas.deinitialise();
        log.info("Canvas deinitialised");

        log.info("Deinitialising platform");
        platform.deinitialise();
        log.info("Platform deinitialised");

        log.info("Application successfully shutdown");
    }

}