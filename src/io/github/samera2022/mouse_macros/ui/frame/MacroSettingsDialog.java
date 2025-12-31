package io.github.samera2022.mouse_macros.ui.frame;

import io.github.samera2022.mouse_macros.Localizer;
import io.github.samera2022.mouse_macros.adapter.WindowClosingAdapter;
import io.github.samera2022.mouse_macros.constant.IconConsts;
import io.github.samera2022.mouse_macros.constant.OtherConsts;
import io.github.samera2022.mouse_macros.filter.DocumentInputFilter;
import io.github.samera2022.mouse_macros.manager.ConfigManager;
import io.github.samera2022.mouse_macros.util.ComponentUtil;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

import static io.github.samera2022.mouse_macros.manager.ConfigManager.config;

public class MacroSettingsDialog extends JDialog {

    public MacroSettingsDialog() {
        setTitle(Localizer.get("macro_settings"));
        setName("macro_settings");
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/MouseMacros.png"))).getImage());
        setModal(true);
        setLayout(new BorderLayout(10, 10));
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        content.setFocusable(true);

        JLabel macroSettingsTitle = new JLabel(Localizer.get("macro_settings"));
        macroSettingsTitle.setFont(macroSettingsTitle.getFont().deriveFont(Font.BOLD, 18f));
        macroSettingsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(macroSettingsTitle);
        content.add(Box.createVerticalStrut(10));
        content.add(new JSeparator());

        JPanel enableCustomSettingsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel enableCustomSettingsLabel = new JLabel(Localizer.get("macro_settings.enable_custom_macro_settings"));
        JCheckBox enableCustomSettingsBox = new JCheckBox(IconConsts.CHECK_BOX);
        enableCustomSettingsBox.setSelected(config.enableCustomMacroSettings);
        enableCustomSettingsPanel.add(enableCustomSettingsLabel);
        enableCustomSettingsPanel.add(Box.createHorizontalStrut(10));
        enableCustomSettingsPanel.add(enableCustomSettingsBox);
        enableCustomSettingsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(Box.createVerticalStrut(10));
        content.add(enableCustomSettingsPanel);

        // 二级设置面板（缩进）
        JPanel subSettingsPanel = new JPanel();
        subSettingsPanel.setLayout(new BoxLayout(subSettingsPanel, BoxLayout.Y_AXIS));
        subSettingsPanel.setBorder(BorderFactory.createEmptyBorder(0, 32, 0, 0)); // 四个空格缩进
        subSettingsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        subSettingsPanel.setFocusable(true);

        JLabel repeatLabel = new JLabel(Localizer.get("macro_settings.repeat_times") + ": ");
        JTextField repeatField = new JTextField(String.valueOf(config.repeatTime));
        repeatField.setColumns(7); // 10位数字
        // 计算能完全显示2147483647所需的宽度
        // 先临时设置字体，确保FontMetrics获取准确
        repeatField.setFont(repeatField.getFont());
        // 只允许输入全体正整数，或唯一允许的负数为-1，允许输入"-"便于编辑
        ((javax.swing.text.AbstractDocument) repeatField.getDocument()).setDocumentFilter(new DocumentInputFilter() {
            @Override
            public boolean isValidContent(String text) {
                // 只允许-1，正整数，空字符串，或单独一个负号（便于输入-1）
                return text.equals("-1") || text.matches("\\d+") || text.isEmpty() || text.equals("-");
            }
        });

        JLabel repeatDelayLabel = new JLabel(Localizer.get("macro_settings.repeat_delay") + ": ");
        JTextField repeatDelayField = new JTextField(String.valueOf(config.repeatDelay));
        repeatDelayField.setColumns(7);
        repeatDelayField.setFont(repeatDelayField.getFont());
        ((javax.swing.text.AbstractDocument) repeatDelayField.getDocument()).setDocumentFilter(new DocumentInputFilter() {
            @Override
            public boolean isValidContent(String text) {
                // 允许整数或最多三位小数
                return text.matches("\\d*(\\.\\d{0,3})?") || text.isEmpty();
            }
        });

        JPanel repeatPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        repeatPanel.add(repeatLabel);
        repeatPanel.add(repeatField);
        repeatPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // repeatDelay 同级同缩进
        JPanel repeatDelayPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        repeatDelayPanel.add(repeatDelayLabel);
        repeatDelayPanel.add(repeatDelayField);
        repeatDelayPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        subSettingsPanel.add(repeatPanel);
        subSettingsPanel.add(repeatDelayPanel);
        subSettingsPanel.add(Box.createHorizontalStrut(10));
        content.add(subSettingsPanel);

        // 保存按钮单独底部居中
        JButton saveSettingsBtn = new JButton(Localizer.get("macro_settings.save_settings"));
        saveSettingsBtn.addActionListener(e -> {
            String text = repeatField.getText();
            String delayText = repeatDelayField.getText();
            if (!text.isEmpty()) {
                config.enableCustomMacroSettings = enableCustomSettingsBox.isSelected();
                try {
                    config.repeatTime = Integer.parseInt(text);
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }
            if (!delayText.isEmpty()) {
                try {
                    config.repeatDelay = Double.parseDouble(delayText);
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }
            ConfigManager.saveConfig(config);
            ConfigManager.reloadConfig();
            dispose();
        });
        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        savePanel.add(saveSettingsBtn);
        savePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        //联动
        java.awt.event.ItemListener followSysListener = e -> {
            boolean enabled = enableCustomSettingsBox.isSelected();
            repeatField.setEnabled(enabled);
            repeatDelayField.setEnabled(enabled);
            if (!enabled) {
                repeatField.setText("1");
                repeatDelayField.setText("0");
            }
        };
        enableCustomSettingsBox.addItemListener(followSysListener);
        followSysListener.itemStateChanged(null);

        add(content, BorderLayout.CENTER);
        add(savePanel, BorderLayout.SOUTH);
        ComponentUtil.setMode(getContentPane(),config.enableDarkMode?OtherConsts.DARK_MODE:OtherConsts.LIGHT_MODE);
        ComponentUtil.applyWindowSizeCache(this, "macro_settings", 280, 181);
        setLocationRelativeTo(this);
        addWindowListener(new WindowClosingAdapter());
    }
}
