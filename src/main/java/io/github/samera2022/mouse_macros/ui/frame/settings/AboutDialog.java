package io.github.samera2022.mouse_macros.ui.frame.settings;

import io.github.samera2022.mouse_macros.Localizer;
import io.github.samera2022.mouse_macros.adapter.WindowClosingAdapter;
import io.github.samera2022.mouse_macros.constant.OtherConsts;
import io.github.samera2022.mouse_macros.util.ComponentUtil;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

import static io.github.samera2022.mouse_macros.manager.ConfigManager.config;

public class AboutDialog extends JDialog{
    public AboutDialog(){
        setTitle(Localizer.get("settings.about_author"));
        setName("settings.about_author");
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/MouseMacros.png"))).getImage());
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

        JTextArea aboutArea = new JTextArea(Localizer.get("settings.about_author.content"));
        aboutArea.setPreferredSize(new Dimension(350,200));
        aboutArea.setEditable(false);
        aboutArea.setLineWrap(true);
        aboutArea.setWrapStyleWord(true);
        aboutArea.setOpaque(false);
        aboutArea.setBorder(null);
        aboutArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(Box.createVerticalStrut(10));
        content.add(aboutArea);

        // 创建底部面板用于居中显示 GitHub 按钮
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        JButton githubButton = new JButton("GitHub");
        githubButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        githubButton.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new java.net.URI("https://github.com/Samera2022/MouseMacros"));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(AboutDialog.this, Localizer.get("settings.about_author.browser_opening_failed")+": " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        bottomPanel.add(githubButton);

        add(content, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        ComponentUtil.setMode(getContentPane(),config.enableDarkMode?OtherConsts.DARK_MODE:OtherConsts.LIGHT_MODE);
        ComponentUtil.adjustFrameWithCache(this, 0,
            new JComponent[]{aboutTitle},
            new JComponent[]{aboutArea},
            new JComponent[]{bottomPanel}
        );
        setLocationRelativeTo(this);
        addWindowListener(new WindowClosingAdapter());
    }
}
