package io.github.samera2022.mouse_macros.ui.frame.settings;

import io.github.samera2022.mouse_macros.Localizer;
import io.github.samera2022.mouse_macros.constant.IconConsts;
import io.github.samera2022.mouse_macros.constant.OtherConsts;
import io.github.samera2022.mouse_macros.manager.ConfigManager;
import io.github.samera2022.mouse_macros.util.ComponentUtil;
import io.github.samera2022.mouse_macros.util.ScreenUtil;

import javax.swing.*;
import java.awt.*;

import static io.github.samera2022.mouse_macros.manager.ConfigManager.config;

public class MacroSettingsDialogTest extends JDialog {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MacroSettingsDialogTest().setVisible(true));
    }

    public MacroSettingsDialogTest() {
        setTitle(Localizer.get("macro_settings"));
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
        //70是显示2147483647所需的宽度，再加10是为了紧凑
//        int maxWidth = 50;
//        repeatField.setPreferredSize(new Dimension(maxWidth, 25));
//        repeatField.setMinimumSize(new Dimension(maxWidth, 25));
//        repeatField.setMaximumSize(new Dimension(maxWidth, 25));
        // 只允许输入全体正整数，或唯��允许的负数为-1，允许输入"-"便于编辑
        ((javax.swing.text.AbstractDocument) repeatField.getDocument()).setDocumentFilter(new javax.swing.text.DocumentFilter() {
            private boolean isValidInt(String text) {
                // 只允许-1，正整数，空字符串，或单独一个负号（便于输入-1）
                return text.equals("-1") || text.matches("\\d+") || text.isEmpty() || text.equals("-");
            }

            @Override
            public void insertString(FilterBypass fb, int offset, String string, javax.swing.text.AttributeSet attr) throws javax.swing.text.BadLocationException {
                StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
                sb.insert(offset, string);
                if (isValidInt(sb.toString())) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, javax.swing.text.AttributeSet attrs) throws javax.swing.text.BadLocationException {
                StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
                sb.replace(offset, offset + length, text);
                if (isValidInt(sb.toString())) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        JPanel repeatPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        repeatPanel.add(repeatLabel);
        repeatPanel.add(repeatField);
        repeatPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        subSettingsPanel.add(repeatPanel);
        subSettingsPanel.add(Box.createHorizontalStrut(10));
        content.add(subSettingsPanel);



        // 保存按钮单独底部居中
        JButton saveSettingsBtn = new JButton(Localizer.get("macro_settings.save_settings"));
        saveSettingsBtn.addActionListener(e -> {
            String text = repeatField.getText();
            if (!text.isEmpty()) {
                config.enableCustomMacroSettings = enableCustomSettingsBox.isSelected();
                try {
                    config.repeatTime = Integer.parseInt(text);
                } catch (NumberFormatException ex) {
//                    config.repeatTime = 1;
                    ex.printStackTrace();
                }
            }
            // 热键配置保存到config.keyMap（假设已有相关逻辑）
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
            if (!enabled) repeatField.setText("1");
        };
        enableCustomSettingsBox.addItemListener(followSysListener);
        followSysListener.itemStateChanged(null);

        add(content, BorderLayout.CENTER);
        add(savePanel, BorderLayout.SOUTH);
        ComponentUtil.setMode(getContentPane(), config.enableDarkMode ? OtherConsts.DARK_MODE : OtherConsts.LIGHT_MODE);

        ComponentUtil.setCorrectSize(this, 600, 500);
        setLocationRelativeTo(this);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // 添加content的MouseListener用于强制修正焦点
        content.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                content.requestFocusInWindow();
            }
        });
    }
}