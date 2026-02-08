package io.github.samera2022.mousemacros.app.ui.frame;

import io.github.samera2022.mousemacros.app.Localizer;
import io.github.samera2022.mousemacros.app.adapter.WindowClosingAdapter;
import io.github.samera2022.mousemacros.app.manager.CacheManager;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

import io.github.samera2022.mousemacros.app.config.ConfigManager;
import io.github.samera2022.mousemacros.app.constant.OtherConsts;
import io.github.samera2022.mousemacros.app.constant.IconConsts;
import io.github.samera2022.mousemacros.app.util.ComponentUtil;
import io.github.samera2022.mousemacros.app.ui.component.CustomRadioButton;

public class ExitDialog extends JDialog {
    public ExitDialog(MainFrame mf) {
        setTitle(Localizer.get("exit"));
        setName("exit");
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource(OtherConsts.RELATIVE_PATH + "icons/MouseMacros.png"))).getImage());
        setModal(true);
        setLayout(new BorderLayout(10, 10));
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel(Localizer.get("exit.title"));
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel);
        content.add(titlePanel);
        content.add(Box.createVerticalStrut(10));
        content.add(new JSeparator());
        content.add(Box.createVerticalStrut(10));

        ButtonGroup group = new ButtonGroup();
        CustomRadioButton exitOnCloseRadio = new CustomRadioButton(Localizer.get("exit.exit_on_close"));
        CustomRadioButton minimizeToTrayRadio = new CustomRadioButton(Localizer.get("exit.minimize_to_tray"));
        group.add(exitOnCloseRadio);
        group.add(minimizeToTrayRadio);
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        radioPanel.setOpaque(false);
        radioPanel.add(exitOnCloseRadio);
        radioPanel.add(minimizeToTrayRadio);
        radioPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(radioPanel);
        content.add(Box.createVerticalStrut(10));

        JPanel rememberPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JCheckBox rememberOptionBox = new JCheckBox(IconConsts.CHECK_BOX);
        JLabel rememberLabel = new JLabel(Localizer.get("exit.remember_this_option"));
        rememberPanel.add(rememberOptionBox);
        rememberPanel.add(rememberLabel);
        rememberPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(rememberPanel);
        content.add(Box.createVerticalStrut(10));

        add(content, BorderLayout.CENTER);
        JButton finishButton = new JButton(Localizer.get("exit.finish"));
        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        savePanel.add(finishButton);
        savePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(savePanel, BorderLayout.SOUTH);

        setLocationRelativeTo(mf);
        ComponentUtil.adjustFrameWithCache(this, 200, new JComponent[]{titleLabel}, new JComponent[]{exitOnCloseRadio, minimizeToTrayRadio}, new JComponent[]{rememberLabel, rememberOptionBox}, new JComponent[]{finishButton});

        finishButton.addActionListener(e -> {
            String op = "";
            if (exitOnCloseRadio.isSelected()) op = CacheManager.EXIT_ON_CLOSE;
            if (minimizeToTrayRadio.isSelected()) op = CacheManager.MINIMIZE_TO_TRAY;
            if (rememberOptionBox.isSelected()) {
                CacheManager.cache.defaultCloseOperation = op;
                CacheManager.saveCache();
            }
            switch (op) {
                case CacheManager.EXIT_ON_CLOSE:
                    System.exit(0);
                    break;
                case CacheManager.MINIMIZE_TO_TRAY:
                    mf.minimizeToTray();
                    break;
                default:
                    dispose();
                    break;
            }
        });
        addWindowListener(new WindowClosingAdapter());
    }
}