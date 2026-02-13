package io.github.samera2022.mouse_macros.adapter;

import io.github.samera2022.mouse_macros.manager.CacheManager;

import java.awt.event.WindowAdapter;

public class WindowClosingAdapter extends WindowAdapter {
    @Override
    public void windowClosed(java.awt.event.WindowEvent e) {
        int w = e.getComponent().getWidth(), h = e.getComponent().getHeight();
        CacheManager.setWindowSize(e.getComponent().getName(), w + "," + h);
    }
    @Override
    public void windowClosing(java.awt.event.WindowEvent e) {
        int w = e.getComponent().getWidth(), h = e.getComponent().getHeight();
        CacheManager.setWindowSize(e.getComponent().getName(), w + "," + h);
    }
}
