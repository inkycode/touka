package com.inkycode.touka.core.application.impl;

import java.util.Map;

import com.inkycode.touka.core.application.Screen;
import com.inkycode.touka.core.application.ScreenManager;
import com.inkycode.touka.core.bootstrap.annotations.Inject;
import com.inkycode.touka.core.bootstrap.annotations.Source;

public class ScreenManagerImpl implements ScreenManager {

    @Inject
    @Source("component")
    private Map<String, Screen> screens;

    private Screen activeScreen;

    @Override
    public void setActiveScreen(Class<?> screen) {
        this.setActiveScreen(screen.getName());
    }

    @Override
    public void setActiveScreen(String screenName) {
        if (this.screens.containsKey(screenName)) {
            if (this.activeScreen != null) {
                this.activeScreen.unload();
            }

            this.activeScreen = this.screens.get(screenName);

            this.activeScreen.load();
        }
    }

    @Override
    public Screen getActiveScreen() {
        return this.activeScreen;
    }
    
}