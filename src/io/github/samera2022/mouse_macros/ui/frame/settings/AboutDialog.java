package io.github.samera2022.mouse_macros.ui.frame.settings;

import io.github.samera2022.mouse_macros.Localizer;
import io.github.samera2022.mouse_macros.cache.SizeCache;
import io.github.samera2022.mouse_macros.constant.OtherConsts;
import io.github.samera2022.mouse_macros.util.ComponentUtil;
import io.github.samera2022.mouse_macros.manager.CacheManager;

import javax.swing.*;
import java.awt.*;

import static io.github.samera2022.mouse_macros.manager.ConfigManager.config;

public class AboutDialog extends JDialog{
    public AboutDialog(){
        setTitle(Localizer.get("settings.about_author"));
        setModal(true);
        setLayout(new BorderLayout(10, 10));
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel aboutTitle = new JLabel(Localizer.get("settings.about_author"));
        aboutTitle.setFont(aboutTitle.getFont().deriveFont(Font.BOLD, 18f));
        aboutTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(aboutTitle);
        content.add(Box.createVerticalStrut(10));
        content.add(new JSeparator());
        // 新增无边框JTextArea
        JTextArea aboutArea = new JTextArea(OtherConsts.ABOUT_AUTHOR);
        aboutArea.setEditable(false);
        aboutArea.setLineWrap(true);
        aboutArea.setWrapStyleWord(true);
        aboutArea.setOpaque(false);
        aboutArea.setBorder(null);
        aboutArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(Box.createVerticalStrut(10));
        content.add(aboutArea);

        add(content, BorderLayout.CENTER);
        ComponentUtil.setMode(getContentPane(),config.enableDarkMode?OtherConsts.DARK_MODE:OtherConsts.LIGHT_MODE);
        ComponentUtil.applyWindowSizeCache(this, "AboutDialog", 500, 360);
        setLocationRelativeTo(this);

        // 关闭时保存当前尺寸
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                int w = getWidth(), h = getHeight();
                CacheManager.setWindowSize("AboutDialog", w + "," + h);
            }
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                int w = getWidth(), h = getHeight();
                CacheManager.setWindowSize("AboutDialog", w + "," + h);
            }
        });
    }
}
