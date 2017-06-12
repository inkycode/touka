package com.inkycode.touka.core.application;

public interface ScreenManager {

    void setActiveScreen(final Class<?> screen);

    void setActiveScreen(final String screenName);

    Screen getActiveScreen();

}