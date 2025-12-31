package io.github.samera2022.mouse_macros.ui.frame;

import io.github.samera2022.mouse_macros.Localizer;
import io.github.samera2022.mouse_macros.manager.CacheManager;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

import static io.github.samera2022.mouse_macros.manager.ConfigManager.config;
import io.github.samera2022.mouse_macros.constant.OtherConsts;
import io.github.samera2022.mouse_macros.constant.IconConsts;
import io.github.samera2022.mouse_macros.util.ComponentUtil;

public class ExitDialog extends JDialog {

    public ExitDialog(MainFrame mf) {
        setTitle(Localizer.get("exit"));
        setName("exit");
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/MouseMacros.png"))).getImage());
        setModal(true);
        setLayout(new BorderLayout(10, 10));
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel(Localizer.get("exit.title"));
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 标题左对齐，包装一层流布局
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel);
        content.add(titlePanel);
        content.add(Box.createVerticalStrut(10));
        content.add(new JSeparator());
        content.add(Box.createVerticalStrut(10));

        // 单选框区（整体居中）
        ButtonGroup group = new ButtonGroup();
        JRadioButton exitOnCloseRadio = new JRadioButton(Localizer.get("exit.exit_on_close"));
        JRadioButton minimizeToTrayRadio = new JRadioButton(Localizer.get("exit.minimize_to_tray"));
        group.add(exitOnCloseRadio);
        group.add(minimizeToTrayRadio);
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        radioPanel.setOpaque(false);
        radioPanel.add(exitOnCloseRadio);
        radioPanel.add(minimizeToTrayRadio);
        radioPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(radioPanel);
        content.add(Box.createVerticalStrut(10));

        // 记住选项区（整体居中，勾选框在左，文字在右）
        JPanel rememberPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JCheckBox rememberOptionBox = new JCheckBox(IconConsts.CHECK_BOX);
        JLabel rememberLabel = new JLabel(Localizer.get("exit.remember_this_option"));
        rememberPanel.add(rememberOptionBox);
        rememberPanel.add(rememberLabel);
        rememberPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(rememberPanel);
        content.add(Box.createVerticalStrut(10));

        add(content, BorderLayout.CENTER);
        // 保存按钮区
        JButton finishButton = new JButton(Localizer.get("exit.finish"));
        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        savePanel.add(finishButton);
        savePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(savePanel, BorderLayout.SOUTH);

        // 暗色模式
        int mode = config.enableDarkMode ? OtherConsts.DARK_MODE : OtherConsts.LIGHT_MODE;
        ComponentUtil.setMode(getContentPane(), mode);

        setLocationRelativeTo(mf);
        ComponentUtil.applyWindowSizeCache(this, "exit", 481, 274);

        finishButton.addActionListener(e -> {
            String op = exitOnCloseRadio.isSelected() ? CacheManager.EXIT_ON_CLOSE : CacheManager.MINIMIZE_TO_TRAY;
            if (rememberOptionBox.isSelected()) CacheManager.setDefaultCloseOperation(op);
            dispose();
            if (CacheManager.EXIT_ON_CLOSE.equals(op)) System.exit(0);
            else mf.minimizeToTray();
        });
    }
}
