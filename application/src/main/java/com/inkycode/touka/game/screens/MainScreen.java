package com.inkycode.touka.game.screens;

import java.util.Map;

import org.slf4j.Logger;

import com.inkycode.touka.core.application.Screen;
import com.inkycode.touka.core.bootstrap.annotations.Inject;
import com.inkycode.touka.core.bootstrap.annotations.Source;
import com.inkycode.touka.core.graphics.Renderer;
import com.inkycode.touka.core.graphics.Shader;

public class MainScreen implements Screen {

    @Inject
    @Source("logger")
    private Logger log;

    @Inject
    @Source("component")
    private Renderer renderer;

    @Inject
    @Source("component")
    private Map<String, Shader> shaders;

    @Override
    public void load() {
        for (Shader shader : shaders.values()) {
            shader.load();
        }
    }

    public void unload() {
        for (Shader shader : shaders.values()) {
            shader.unload();
        }
    }

    @Override
    public void render() {
        
    }

    @Override
    public void update() {
        
    }
}