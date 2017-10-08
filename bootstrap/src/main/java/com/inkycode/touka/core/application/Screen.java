package com.inkycode.touka.core.application;

public interface Screen {

    void load();

    void unload();
    
    void render();

    void update();

}