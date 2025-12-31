package io.github.samera2022.mouse_macros.adapter;

import io.github.samera2022.mouse_macros.manager.ConfigManager;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Objects;

public class MouseCheckAdapter extends MouseAdapter {

    @Override
    public void mouseEntered(MouseEvent e) { e.getComponent().setCursor(ConfigManager.config.enableDarkMode ? CustomCursor.DARK_HELP_CURSOR : CustomCursor.LIGHT_HELP_CURSOR); }

    @Override
    public void mouseExited(MouseEvent e) { e.getComponent().setCursor(Cursor.getDefaultCursor()); }
} class CustomCursor {
    public static Cursor DARK_HELP_CURSOR;
    public static Cursor LIGHT_HELP_CURSOR;
    static {
        try {
            DARK_HELP_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(
                    ImageIO.read(Objects.requireNonNull(MouseCheckAdapter.class.getResource("/LIGHT_HELP_CURSOR.png")))
                    , new Point(16, 16), "LightHelpCursor");
            LIGHT_HELP_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(
                    ImageIO.read(Objects.requireNonNull(MouseCheckAdapter.class.getResource("/DARK_HELP_CURSOR.png")))
                    , new Point(16, 16), "DarkHelpCursor");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
