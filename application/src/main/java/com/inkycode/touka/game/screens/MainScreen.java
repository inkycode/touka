package com.inkycode.touka.game.screens;

import org.slf4j.Logger;

import com.inkycode.touka.core.application.Screen;
import com.inkycode.touka.core.bootstrap.annotations.Inject;
import com.inkycode.touka.core.bootstrap.annotations.Source;

public class MainScreen implements Screen {

    @Inject
    @Source("logger")
    private Logger log;


    @Override
    public void render() {
        this.log.info("render()");
    }

    @Override
    public void update() {
        this.log.info("update()");
    }
}