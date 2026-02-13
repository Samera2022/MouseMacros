package io.github.samera2022.mouse_macros.adapter;

import io.github.samera2022.mouse_macros.manager.CacheManager;

import java.awt.event.WindowAdapter;

public class WindowClosingAdapter extends WindowAdapter {
    @Override
    public void windowClosed(java.awt.event.WindowEvent e) { commonEvent(e); }
    @Override
    public void windowClosing(java.awt.event.WindowEvent e) { commonEvent(e); }

    private void commonEvent(java.awt.event.WindowEvent e) {
        int w = e.getComponent().getWidth(), h = e.getComponent().getHeight();
        CacheManager.cache.windowSizeMap.put(e.getComponent().getName(), w + "," + h);
        CacheManager.saveCache();
    }
}
