package io.github.samera2022.mouse_macros.fee;

import io.github.samera2022.mouse_macros.Localizer;
import io.github.samera2022.mouse_macros.constant.OtherConsts;
import io.github.samera2022.mouse_macros.ui.frame.MainFrame;
import io.github.samera2022.mouse_macros.util.ComponentUtil;

import javax.swing.*;
import java.awt.*;

import static io.github.samera2022.mouse_macros.manager.ConfigManager.config;
import static io.github.samera2022.mouse_macros.ui.frame.MainFrame.MAIN_FRAME;

public class VerificationFrame extends JFrame {
    private JTextField verificationField;
    private boolean isFormatting = false; // 添加标志位防止递归

    public VerificationFrame() {
        setTitle(Localizer.get("verification"));
        setLayout(new BorderLayout(10, 10));
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        content.setFocusable(true);

        JLabel titleLabel = new JLabel(Localizer.get("verification"));
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(titleLabel);
        content.add(Box.createVerticalStrut(10));
        content.add(new JSeparator());
        content.add(Box.createVerticalStrut(15));

        // 添加说明标签
        JLabel instructionLabel = new JLabel(Localizer.get("verification.instruction"));
        instructionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(instructionLabel);
        content.add(Box.createVerticalStrut(10));

        // 验证码输入框
        JLabel verificationLabel = new JLabel("        "+Localizer.get("verification.input") + ": ");
        verificationField = new JTextField(15);
        verificationField.setMaximumSize(new Dimension(250, 30));

        // 设置输入格式限制：只允许大写字母、数字和连字符
        ((javax.swing.text.AbstractDocument) verificationField.getDocument()).setDocumentFilter(
                new javax.swing.text.DocumentFilter() {
                    @Override
                    public void insertString(FilterBypass fb, int offset, String string,
                                             javax.swing.text.AttributeSet attr)
                            throws javax.swing.text.BadLocationException {

                        string = string.toUpperCase().replaceAll("[^A-Z0-9-]", "");
                        super.insertString(fb, offset, string, attr);
                        if (!isFormatting) { // 防止递归
                            formatVerificationCode();
                        }
                    }

                    @Override
                    public void replace(FilterBypass fb, int offset, int length, String text,
                                        javax.swing.text.AttributeSet attrs)
                            throws javax.swing.text.BadLocationException {

                        text = text.toUpperCase().replaceAll("[^A-Z0-9-]", "");
                        super.replace(fb, offset, length, text, attrs);
                        if (!isFormatting) { // 防止递归
                            formatVerificationCode();
                        }
                    }
                }
        );

        JPanel verificationPanel = new JPanel();
        verificationPanel.setLayout(new BoxLayout(verificationPanel, BoxLayout.X_AXIS));
        verificationPanel.add(verificationLabel);
        verificationPanel.add(verificationField);
        verificationPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(verificationPanel);
        content.add(Box.createVerticalStrut(15));

        // 验证按钮
        JButton verifyButton = new JButton(Localizer.get("verification.verify"));
        verifyButton.addActionListener(e -> verify());
        verifyButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(verifyButton);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(buttonPanel);

        add(content, BorderLayout.CENTER);
        ComponentUtil.setMode(getContentPane(), config.enableDarkMode ? OtherConsts.DARK_MODE : OtherConsts.LIGHT_MODE);

        ComponentUtil.setCorrectSize(this, 1060, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    // 自动格式化验证码为 XXXXX-XXXXX-XXXXX 格式
    private void formatVerificationCode() {
        if (isFormatting) return; // 如果正在格式化则直接返回

        isFormatting = true; // 设置标志位
        try {
            String text = verificationField.getText().replace("-", "").toUpperCase();
            if (text.length() > 15) {
                text = text.substring(0, 15);
            }

            StringBuilder formatted = new StringBuilder();
            for (int i = 0; i < text.length(); i++) {
                if (i > 0 && (i % 5 == 0) && i < 15) {
                    formatted.append('-');
                }
                formatted.append(text.charAt(i));
            }

            // 只在实际变化时设置文本
            if (!formatted.toString().equals(verificationField.getText())) {
                verificationField.setText(formatted.toString());
            }
        } finally {
            isFormatting = false; // 重置标志位
        }
    }

    private void verify(){
        DateIntEncoderDecoder.DecodedResult result = CryptoUtil.verifyCode(verificationField.getText());
        if (result!=null) {
            // 存储验证码（安全存储）
            try {
                SecureCodeStorage.saveActivationInfo(verificationField.getText());
                dispose();
                MAIN_FRAME.setVisible(true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 显示验证窗口
     */
    public static void showVerificationFrame() {
        // 使用SwingUtilities确保在事件调度线程中创建UI
        javax.swing.SwingUtilities.invokeLater(() -> {
            VerificationFrame frame = new VerificationFrame();
            frame.setVisible(true);
        });
    }
}