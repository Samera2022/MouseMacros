package io.github.samera2022.mouse_macros.ui.frame;

import io.github.samera2022.mouse_macros.Localizer;
import io.github.samera2022.mouse_macros.manager.CacheManager;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ExitDialog extends JDialog {

    public ExitDialog(MainFrame mf) {
        super(mf, true);
        setTitle(Localizer.get("exit.title"));
        setName("exit_dialog");
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/MouseMacros.png"))).getImage());
        setLayout(new BorderLayout(10, 10));
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel(Localizer.get("exit.title"));
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(titleLabel);
        content.add(Box.createVerticalStrut(10));
        content.add(new JSeparator());
        content.add(Box.createVerticalStrut(10));

        ButtonGroup group = new ButtonGroup();
        JRadioButton exitOnCloseRadio = new JRadioButton(Localizer.get("exit.exit_on_close"));
        JRadioButton minimizeToTrayRadio = new JRadioButton(Localizer.get("exit.minimize_to_tray"));
        group.add(exitOnCloseRadio);
        group.add(minimizeToTrayRadio);
        exitOnCloseRadio.setAlignmentX(Component.LEFT_ALIGNMENT);
        minimizeToTrayRadio.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(exitOnCloseRadio);
        content.add(Box.createVerticalStrut(5));
        content.add(minimizeToTrayRadio);
        content.add(Box.createVerticalStrut(15));

        JCheckBox rememberOptionBox = new JCheckBox(Localizer.get("exit.remember_this_option"));
        rememberOptionBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(rememberOptionBox);
        content.add(Box.createVerticalStrut(20));

        JButton finishButton = new JButton(Localizer.get("exit.finish"));
        finishButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.add(finishButton);
        content.add(buttonPanel);

        add(content, BorderLayout.CENTER);
        pack();
        setResizable(false);
        setLocationRelativeTo(mf);

        finishButton.addActionListener(e -> {
            String op = exitOnCloseRadio.isSelected() ? CacheManager.EXIT_ON_CLOSE : CacheManager.MINIMIZE_TO_TRAY;
            if (rememberOptionBox.isSelected()) CacheManager.setDefaultCloseOperation(op);
            dispose();
            if (CacheManager.EXIT_ON_CLOSE.equals(op)) System.exit(0);
            else mf.minimizeToTray();
        });
    }
}
