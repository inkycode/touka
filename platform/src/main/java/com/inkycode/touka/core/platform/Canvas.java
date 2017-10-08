package com.inkycode.touka.core.platform;

public interface Canvas {

    public static final long DISPLAY_MODE_UNKOWN = 0x00;
    public static final long DISPLAY_MODE_WINDOW = 0x01;
    public static final long DISPLAY_MODE_BORDERLESS_WINDOW = 0x02;
    
    void initialise();

    void deinitialise();

    void swapBuffers();

    boolean isClosing();

    int getWidth();

    int getHeight();
}