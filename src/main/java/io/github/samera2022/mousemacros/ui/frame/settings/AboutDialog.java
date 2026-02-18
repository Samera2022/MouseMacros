package io.github.samera2022.mousemacros.ui.frame.settings;

import io.github.samera2022.mousemacros.Localizer;
import io.github.samera2022.mousemacros.ui.adapter.WindowClosingAdapter;
import io.github.samera2022.mousemacros.config.ConfigManager;
import io.github.samera2022.mousemacros.constant.OtherConsts;
import io.github.samera2022.mousemacros.util.ComponentUtil;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class AboutDialog extends JDialog{
    public AboutDialog(){
        setTitle(Localizer.get("settings.about_author"));
        setName("settings.about_author");
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource(OtherConsts.RELATIVE_PATH + "icons/MouseMacros.png"))).getImage());
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

        JTextArea aboutArea = new JTextArea(Localizer.get("about_author.content"));
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
                JOptionPane.showMessageDialog(AboutDialog.this, Localizer.get("about_author.browser_opening_failed")+": " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        bottomPanel.add(githubButton);

        add(content, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        ComponentUtil.adjustFrameWithCache(this, 0,
                new JComponent[]{aboutTitle},
                new JComponent[]{aboutArea},
                new JComponent[]{bottomPanel}
        );
        ComponentUtil.setMode(getContentPane(), ConfigManager.getBoolean("enable_dark_mode") ?OtherConsts.DARK_MODE:OtherConsts.LIGHT_MODE);

        setLocationRelativeTo(this);
        addWindowListener(new WindowClosingAdapter());
    }
}
