package io.github.samera2022.mouse_macros.ui.frame;

import io.github.samera2022.mouse_macros.Localizer;
import io.github.samera2022.mouse_macros.constant.OtherConsts;
import io.github.samera2022.mouse_macros.util.ComponentUtil;

import javax.swing.*;
import java.awt.*;

import static io.github.samera2022.mouse_macros.manager.ConfigManager.config;
import static io.github.samera2022.mouse_macros.ui.frame.MainFrame.MAIN_FRAME;

public class MacroSettingsDialog extends JDialog {
    public MacroSettingsDialog() {
        setTitle(Localizer.get("macro_settings"));
        setModal(true);
        setLayout(new BorderLayout(10, 10));
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel(Localizer.get("macro_settings"));
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(title);
        content.add(Box.createVerticalStrut(10));
        content.add(new JSeparator());

        // 输入区域
        JPanel repeatPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        repeatPanel.setOpaque(false);
        JLabel repeatLabel = new JLabel(Localizer.get("repeat_time") + ": ");
        repeatPanel.add(repeatLabel);
        JTextField repeatField = new JTextField(String.valueOf(config.repeatTime));
        repeatField.setColumns(4);
        repeatField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        // 动态调整输入框宽度
        repeatField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void updateWidth() {
                String text = repeatField.getText();
                FontMetrics fm = repeatField.getFontMetrics(repeatField.getFont());
                int width = fm.stringWidth(text.isEmpty() ? "0" : text) + 20;
                repeatField.setPreferredSize(new Dimension(width, 25));
                repeatField.setMinimumSize(new Dimension(width, 25));
                repeatField.setMaximumSize(new Dimension(width, 25));
                repeatField.revalidate();
                repeatField.repaint();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateWidth(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateWidth(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateWidth(); }
        });
        // 初始化时也设置一次宽度
        {
            String text = repeatField.getText();
            FontMetrics fm = repeatField.getFontMetrics(repeatField.getFont());
            int width = fm.stringWidth(text.isEmpty() ? "0" : text) + 20;
            repeatField.setPreferredSize(new Dimension(width, 25));
            repeatField.setMinimumSize(new Dimension(width, 25));
            repeatField.setMaximumSize(new Dimension(width, 25));
        }
        // 只允许输入全体整数，且负号只能在最前面且最多一个
        ((javax.swing.text.AbstractDocument) repeatField.getDocument()).setDocumentFilter(new javax.swing.text.DocumentFilter() {
            private boolean isValidInt(String text) {
                // 允许负号在最前面且只出现一次，其余为数字，且不能是"-"单独一个
                return text.matches("-?\\d+") || text.equals("");
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
        // 同步ConfigManager.repeatTime
        repeatField.addActionListener(e -> {
            String text = repeatField.getText();
            if (!text.isEmpty()) {
                config.repeatTime = Integer.parseInt(text);
            }
        });
        // 输入框失去焦点时校验
        repeatField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                String text = repeatField.getText();
                int value = 1;
                try {
                    value = Integer.parseInt(text);
                    if ((value <= 0 && value != -1)) {
                        value = 1;
                    } else if ((!text.isEmpty()) && (new java.math.BigInteger(text).compareTo(java.math.BigInteger.valueOf(Integer.MAX_VALUE)) > 0)) {
                        value = -1;
                    }
                } catch (Exception ex) {
                    value = 1;
                }
                repeatField.setText(String.valueOf(value));
            }
        });
        content.add(Box.createVerticalStrut(15));
        content.add(repeatPanel);

        // 保存按钮单独底部居中
        JButton saveButton = new JButton(Localizer.get("save_and_apply"));
        saveButton.addActionListener(e -> {
            String text = repeatField.getText();
            if (!text.isEmpty()) {
                try {
                    config.repeatTime = Integer.parseInt(text);
                } catch (NumberFormatException ex) {
                    config.repeatTime = 1;
                    repeatField.setText("1");
                }
            }
        });
        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        savePanel.add(saveButton);
        savePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        add(content, BorderLayout.CENTER);
        add(savePanel, BorderLayout.SOUTH);
        ComponentUtil.setMode(getContentPane(), config.enableDarkMode ? OtherConsts.DARK_MODE : OtherConsts.LIGHT_MODE);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(MAIN_FRAME);

        // 让输入框失去焦点（光标消失）
        content.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (!repeatField.hasFocus()) return;
                Component comp = content.getComponentAt(e.getPoint());
                if (comp != repeatField) {
                    repeatField.transferFocus();
                }
            }
        });
    }
}
