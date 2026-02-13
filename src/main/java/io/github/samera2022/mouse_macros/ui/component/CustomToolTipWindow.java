package io.github.samera2022.mouse_macros.ui.component;

import io.github.samera2022.mouse_macros.constant.OtherConsts;
import io.github.samera2022.mouse_macros.manager.ConfigManager;
import io.github.samera2022.mouse_macros.util.ComponentUtil;
import io.github.samera2022.mouse_macros.util.SystemUtil;

import javax.swing.*;
import java.awt.*;

public class CustomToolTipWindow extends JWindow {
    private JLabel label;
    private final boolean allowLongStr;
    // 本来考虑要不要用ComponentUtil的setSize方法的，后来试了下还是算了
    private final int FIXED_WIDTH = (int) ( 130 * SystemUtil.getScale()[0]);

    public CustomToolTipWindow(String text, boolean allowLongStr) {
        super();
        this.allowLongStr = allowLongStr;
        setAlwaysOnTop(true); // 保证提示窗体总在最前面
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // 圆角背景
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        label = new JLabel();
        label.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        panel.setLayout(new BorderLayout());
        panel.add(label, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createLineBorder(new Color(180,180,180), 1, true));
        setContentPane(panel);
        // 设置样式
        int mode = ConfigManager.config.enableDarkMode ? OtherConsts.DARK_MODE : OtherConsts.LIGHT_MODE;
        ComponentUtil.setMode(panel, mode);
        setText(text);
    }
    public void setText(String text) {
        String htmlShort = "<html><div style='text-align:center;'>" + text.replace("\n", "<br>") + "</div></html>";
        label.setText(htmlShort);
        pack();
        int labelWidth = label.getWidth();
        if (allowLongStr) {
            // 2. 仅当超出屏幕80%宽度时才限制最大宽度
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            int maxWidth = (int)(screen.width * 0.8);
            if (labelWidth > maxWidth) {
                label.setText("<html><div style='width: " + maxWidth + "px; text-align:center;'>" + text.replace("\n", "<br>") + "</div></html>");
                pack();
            }
        } else {
            // 3. 仅当超出FIXED_WIDTH时才限制最大宽度
            if (labelWidth*0.8 > FIXED_WIDTH) {
                label.setText("<html><div style='width: " + FIXED_WIDTH + "px; text-align:center;'>" + text.replace("\n", "<br>") + "</div></html>");
                pack();
            }
        }
    }
}
