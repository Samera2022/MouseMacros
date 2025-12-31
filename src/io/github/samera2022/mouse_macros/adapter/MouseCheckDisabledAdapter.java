package io.github.samera2022.mouse_macros.adapter;

import java.awt.event.MouseEvent;

public class MouseCheckDisabledAdapter extends CustomMouseAdapter {
    public MouseCheckDisabledAdapter(String tipText, boolean allowLongStr) {
        super(tipText, allowLongStr);
    }
    @Override
    protected boolean shouldShowTip(MouseEvent e) {
        return !e.getComponent().isEnabled();
    }
}