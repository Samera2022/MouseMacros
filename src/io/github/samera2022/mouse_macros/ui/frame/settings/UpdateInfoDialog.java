package io.github.samera2022.mouse_macros.ui.frame.settings;

import io.github.samera2022.mouse_macros.Localizer;
import io.github.samera2022.mouse_macros.UpdateInfo;
import io.github.samera2022.mouse_macros.adapter.WindowClosingAdapter;
import io.github.samera2022.mouse_macros.constant.OtherConsts;
import io.github.samera2022.mouse_macros.util.ComponentUtil;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

import static io.github.samera2022.mouse_macros.manager.ConfigManager.config;

public class UpdateInfoDialog extends JDialog {
    private final JTextArea updateInfoArea;
    private final JComboBox<String> infoCombo;

    public UpdateInfoDialog() {
        setTitle(Localizer.get("settings.update_info"));
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/MouseMacros.png"))).getImage());
        setModal(true);
        setLayout(new BorderLayout(10, 10));

        JPanel content = new JPanel();
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

        String firstContent = UpdateInfo.values().length > 0 ? UpdateInfo.values()[length - 1].getFormattedLog() : "";
        this.updateInfoArea = new JTextArea(firstContent);
        updateInfoArea.setEditable(false);
        updateInfoArea.setLineWrap(true);
        updateInfoArea.setWrapStyleWord(true);
        updateInfoArea.setOpaque(false);
        updateInfoArea.setBorder(null);
        updateInfoArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(Box.createVerticalStrut(10));
        content.add(updateInfoArea);

        infoCombo.addActionListener(e -> {
            int idx = infoCombo.getSelectedIndex();
            if (idx >= 0 && idx < UpdateInfo.values().length) {
                updateInfoArea.setText(UpdateInfo.values()[idx].getFormattedLog());
                adaptWindowSize();
            }
        });

        add(content, BorderLayout.CENTER);
        ComponentUtil.setMode(getContentPane(), config.enableDarkMode ? OtherConsts.DARK_MODE : OtherConsts.LIGHT_MODE);

        adaptWindowSize();
        setLocationRelativeTo(null);
        addWindowListener(new WindowClosingAdapter());
    }

    private void adaptWindowSize() {
        int baseWidth = 400;
        int textHeight = getTextAreaRealHeight(updateInfoArea, baseWidth);

        int hAdjust = 160;
        int wPadding = 80;

        int contentWidth = baseWidth + wPadding;
        int contentHeight = textHeight + hAdjust;

        int[] finalSize = fitSize(contentWidth, contentHeight);
        setSize(finalSize[0], finalSize[1]);
        revalidate();
        repaint();
    }

    private int getTextAreaRealHeight(JTextArea textArea, int width) {
        textArea.setSize(width, Short.MAX_VALUE);
        return (int) textArea.getUI().getRootView(textArea).getPreferredSpan(javax.swing.text.View.Y_AXIS);
    }

    private int[] fitSize(int width, int height) {
        int targetH = height;
        int targetW = (int) Math.ceil(height * 3.0 / 2.0);
        if (targetW < width) {
            targetW = width;
            targetH = (int) Math.ceil(width * 2.0 / 3.0);
        }
        return new int[]{targetW, targetH};
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