package io.github.samera2022.mousemacros.ui.frame.settings;

import io.github.samera2022.mousemacros.Localizer;
import io.github.samera2022.mousemacros.UpdateInfo;
import io.github.samera2022.mousemacros.ui.adapter.WindowClosingAdapter;
import io.github.samera2022.mousemacros.config.ConfigManager;
import io.github.samera2022.mousemacros.constant.OtherConsts;
import io.github.samera2022.mousemacros.util.ComponentUtil;

import javax.swing.*;
import javax.swing.text.View;
import java.awt.*;
import java.util.Objects;
public class UpdateInfoDialog extends JDialog {
    private final JTextArea updateInfoArea;
    private final JComboBox<String> infoCombo;
    private final JPanel content;

    public UpdateInfoDialog() {
        setTitle(Localizer.get("settings.update_info"));
        //不使用cache，因而不进行setName
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource(OtherConsts.RELATIVE_PATH+"icons/MouseMacros.png"))).getImage());
        setModal(true);
        setLayout(new BorderLayout());

        content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel updateInfoTitle = new JLabel(Localizer.get("settings.update_info"));
        updateInfoTitle.setFont(updateInfoTitle.getFont().deriveFont(Font.BOLD, 18f));
        updateInfoTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(updateInfoTitle);

        content.add(Box.createVerticalStrut(10));
        content.add(new JSeparator());

        JPanel comboPanel = new JPanel();
        comboPanel.setLayout(new BoxLayout(comboPanel, BoxLayout.X_AXIS));
        comboPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel selectLabel = new JLabel(Localizer.get("settings.update_info.select_version"));
        selectLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        comboPanel.add(selectLabel);
        comboPanel.add(Box.createHorizontalStrut(8));

        this.infoCombo = getJComboBox();
        int length = UpdateInfo.values().length;
        infoCombo.setSelectedIndex(length - 1);
        comboPanel.add(infoCombo);

        content.add(Box.createVerticalStrut(10));
        content.add(comboPanel);
        content.add(Box.createVerticalStrut(10));

        String firstContent = UpdateInfo.values().length > 0 ? UpdateInfo.values()[length - 1].getFormattedLog() : "";
        this.updateInfoArea = new JTextArea(firstContent);
        updateInfoArea.setEditable(false);
        updateInfoArea.setLineWrap(true);
        updateInfoArea.setWrapStyleWord(true);
        updateInfoArea.setOpaque(false);
        updateInfoArea.setBorder(null);
        updateInfoArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(updateInfoArea);

        infoCombo.addActionListener(e -> {
            int idx = infoCombo.getSelectedIndex();
            if (idx >= 0 && idx < UpdateInfo.values().length) {
                updateInfoArea.setText(UpdateInfo.values()[idx].getFormattedLog());
                applyOptimalSize();
            }
        });

        add(content, BorderLayout.CENTER);

        ComponentUtil.setMode(getContentPane(), ConfigManager.getBoolean("enable_dark_mode") ?OtherConsts.DARK_MODE:OtherConsts.LIGHT_MODE);

        applyOptimalSize();

        setLocationRelativeTo(null);
        addWindowListener(new WindowClosingAdapter());
    }

    private void applyOptimalSize() {
        int chromeHeight = content.getInsets().top + content.getInsets().bottom;
        for (Component comp : content.getComponents()) {
            if (comp != updateInfoArea) {
                chromeHeight += comp.getPreferredSize().height;
            }
        }

        int testWidth = 500;
        int textHeightAtTest = getTextAreaHeightAtWidth(updateInfoArea, testWidth);
        long textAreaApprox = (long) testWidth * textHeightAtTest;

        double A = 1.0;
        double B = -1.5 * chromeHeight;
        double C = -1.5 * textAreaApprox;

        double optimalWidth = (-B + Math.sqrt(B * B - 4 * A * C)) / (2 * A);

        int finalWidth = (int) Math.max(optimalWidth, 400);

        finalWidth += 30; // [Redundancy 1] 增加30px宽度冗余，防止文字折行临界点导致的计算误差

        int finalTextHeight = getTextAreaHeightAtWidth(updateInfoArea, finalWidth);
        int finalHeight = finalTextHeight + chromeHeight;

        setSize(finalWidth, finalHeight + 45); // [Redundancy 2] 增加45px高度冗余，补偿标题栏Insets及底部Padding
        revalidate();
        repaint();
    }

    private int getTextAreaHeightAtWidth(JTextArea textArea, int width) {
        View view = textArea.getUI().getRootView(textArea);
        view.setSize(width, Float.MAX_VALUE);
        return (int) view.getPreferredSpan(View.Y_AXIS);
    }

    private static JComboBox<String> getJComboBox() {
        JComboBox<String> infoCombo = new JComboBox<>(UpdateInfo.getAllDisplayNames());
        infoCombo.setAlignmentY(Component.CENTER_ALIGNMENT);
        int maxWidth = 0;
        FontMetrics fm = infoCombo.getFontMetrics(infoCombo.getFont());
        for (UpdateInfo info : UpdateInfo.values()) {
            int w = fm.stringWidth(info.getDisplayName());
            if (w > maxWidth) maxWidth = w;
        }
        maxWidth += 32;
        infoCombo.setMaximumSize(new Dimension(maxWidth, infoCombo.getPreferredSize().height));
        infoCombo.setPreferredSize(new Dimension(maxWidth, infoCombo.getPreferredSize().height));
        return infoCombo;
    }
}