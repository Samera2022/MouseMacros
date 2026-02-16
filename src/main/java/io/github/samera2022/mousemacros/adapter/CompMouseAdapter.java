package io.github.samera2022.mousemacros.adapter;

import io.github.samera2022.mousemacros.config.ConfigManager;
import io.github.samera2022.mousemacros.constant.OtherConsts;
import io.github.samera2022.mousemacros.ui.component.CustomToolTipWindow;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Objects;

public class CompMouseAdapter extends MouseAdapter {
    private final String enabledText;
    private final String disabledText;
    private final int type;
    private final boolean allowLongStr;
    private CustomToolTipWindow tipWindow;

    public static final int ENABLED = 1;
    public static final int DISABLED = 2;
    public static final int BOTH = 3;

    public CompMouseAdapter(String enabledText, String disabledText, int type) {
        this.enabledText = enabledText;
        this.disabledText = disabledText;
        this.type = type;
        this.allowLongStr = ConfigManager.getBoolean("allow_long_str");
    }
    public CompMouseAdapter(String enabledText, String disabledText, int type, boolean allowLongStr) {
        this.enabledText = enabledText;
        this.disabledText = disabledText;
        this.type = type;
        this.allowLongStr = allowLongStr;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Component c = (Component) e.getSource();
        String textToShow = null;

        if (c.isEnabled()) {
            if (type == 1 || type == 3) {
                textToShow = enabledText;
                c.setCursor(ConfigManager.getBoolean("enable_dark_mode") ? CustomCursor.LIGHT_HELP_CURSOR : CustomCursor.DARK_HELP_CURSOR);
            }
        } else {
            if (type == 2 || type == 3) {
                textToShow = disabledText;
            }
        }

        if (textToShow == null || textToShow.trim().isEmpty()) {
            return;
        }

        if (tipWindow == null) {
            tipWindow = new CustomToolTipWindow(textToShow, allowLongStr);
        } else {
            tipWindow.setText(textToShow);
        }

        Point p = c.getLocationOnScreen();
        tipWindow.setLocation(p.x, p.y - tipWindow.getHeight() - 5);
        tipWindow.setVisible(true);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        Component c = (Component) e.getSource();
        c.setCursor(Cursor.getDefaultCursor());

        if (tipWindow != null) {
            tipWindow.setVisible(false);
        }
    }
}

class CustomCursor {
    public static Cursor DARK_HELP_CURSOR = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    public static Cursor LIGHT_HELP_CURSOR = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

    static {
        try {
            DARK_HELP_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(
                    ImageIO.read(Objects.requireNonNull(CompMouseAdapter.class.getResource(OtherConsts.RELATIVE_PATH + "cursors/DARK_HELP_CURSOR.png"))),
                    new Point(16, 16), "DarkHelpCursor");

            LIGHT_HELP_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(
                    ImageIO.read(Objects.requireNonNull(CompMouseAdapter.class.getResource(OtherConsts.RELATIVE_PATH + "cursors/LIGHT_HELP_CURSOR.png"))),
                    new Point(16, 16), "LightHelpCursor");

        } catch (IOException | NullPointerException e) {
            System.err.println("Failed to load custom cursors. Falling back to default.");
            e.printStackTrace();
        }
    }
}
