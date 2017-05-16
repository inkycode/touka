package com.inkycode.touka.core.application;

public interface ScreenManager {

    void setActiveScreen(Class<?> screen);

    void setActiveScreen(String screenName);

    Screen getActiveScreen();

}