package io.github.samera2022.mouse_macros.ui.frame.settings;

import io.github.samera2022.mouse_macros.Localizer;
import io.github.samera2022.mouse_macros.cache.SizeCache;
import io.github.samera2022.mouse_macros.constant.OtherConsts;
import io.github.samera2022.mouse_macros.util.ComponentUtil;

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
        if (config.enableDarkMode) {
            ComponentUtil.applyDarkMode(getContentPane(),this);
        } else {
            ComponentUtil.applyLightMode(getContentPane(),this);
        }
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(SizeCache.SIZE);
        setLocationRelativeTo(this);
    }
}
