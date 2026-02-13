package io.github.samera2022.mouse_macros.adapter;

import io.github.samera2022.mouse_macros.manager.ConfigManager;
import io.github.samera2022.mouse_macros.ui.component.CustomToolTipWindow;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class CustomMouseAdapter extends MouseAdapter {
    private CustomToolTipWindow window;
    private final String tipText;
    private final boolean allowLongStr;

    public CustomMouseAdapter(String tipText) {
        this.tipText = tipText;
        this.allowLongStr = ConfigManager.config.allowLongStr;
    }
    public CustomMouseAdapter(String tipText, boolean allowLongStr) {
        this.tipText = tipText;
        this.allowLongStr = allowLongStr;
    }
    @Override
    public void mouseEntered(MouseEvent e) {
        if (shouldShowTip(e)) {
            if (window == null) window = new CustomToolTipWindow(tipText, allowLongStr);
            Point p = e.getLocationOnScreen();
            window.setLocation(p.x + 10, p.y + 10);
            window.setVisible(true);
        }
    }
    @Override
    public void mouseExited(MouseEvent e) {
        if (window != null) window.setVisible(false);
    }
    protected abstract boolean shouldShowTip(MouseEvent e);
}

