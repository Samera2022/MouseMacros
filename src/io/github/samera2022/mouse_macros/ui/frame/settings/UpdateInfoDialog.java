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
    public UpdateInfoDialog() {
        setTitle(Localizer.get("settings.update_info"));
//        setName("settings.update_info"); 不采用cache
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

        // 添加“选择版本”标签和自适应宽度的JComboBox
        JPanel comboPanel = new JPanel();
        comboPanel.setLayout(new BoxLayout(comboPanel, BoxLayout.X_AXIS));
        comboPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel selectLabel = new JLabel(Localizer.get("settings.update_info.select_version"));
        selectLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        comboPanel.add(selectLabel);
        comboPanel.add(Box.createHorizontalStrut(8));

        JComboBox<String> infoCombo = getJComboBox();
        int length = UpdateInfo.values().length;
        infoCombo.setSelectedIndex(length-1);
        comboPanel.add(infoCombo);
        content.add(Box.createVerticalStrut(10));
        content.add(comboPanel);

        // JTextArea显示内容，初始为最后一个内容
        String firstContent = UpdateInfo.values().length > 0 ? UpdateInfo.values()[length-1].getFormattedLog() : "";
        JTextArea updateInfoArea = new JTextArea(firstContent);
        updateInfoArea.setEditable(false);
        updateInfoArea.setLineWrap(true);
        updateInfoArea.setWrapStyleWord(true);
        updateInfoArea.setOpaque(false);
        updateInfoArea.setBorder(null);
        updateInfoArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(Box.createVerticalStrut(10));
        content.add(updateInfoArea);

        // ComboBox切换事件
        infoCombo.addActionListener(e -> {
            int idx = infoCombo.getSelectedIndex();
            if (idx >= 0 && idx < UpdateInfo.values().length) {
                updateInfoArea.setText(UpdateInfo.values()[idx].getFormattedLog());
                updateInfoArea.setSize(340, Short.MAX_VALUE);
                updateInfoArea.revalidate();
                updateInfoArea.repaint();
                int hAdjust = 60;
                ComponentUtil.adjustFrameWithCache(this, hAdjust, new JComponent[]{updateInfoArea});
            }
        });

        add(content, BorderLayout.CENTER);
        ComponentUtil.setMode(getContentPane(),config.enableDarkMode?OtherConsts.DARK_MODE:OtherConsts.LIGHT_MODE);
        // 初始化时也设置内容区宽度，确保高度自适应
        updateInfoArea.setSize(340, Short.MAX_VALUE);
        updateInfoArea.revalidate();
        updateInfoArea.repaint();
        ComponentUtil.adjustFrameWithCache(this, 60, new JComponent[]{updateInfoArea});
        setLocationRelativeTo(this);
        addWindowListener(new WindowClosingAdapter());
    }

    //要求JComboBox的宽度恰好能显示list中最长的元素
    private static JComboBox<String> getJComboBox() {
        JComboBox<String> infoCombo = new JComboBox<>(UpdateInfo.getAllDisplayNames());
        infoCombo.setAlignmentY(Component.CENTER_ALIGNMENT);
        // 计算最大显示宽度
        int maxWidth = 0;
        FontMetrics fm = infoCombo.getFontMetrics(infoCombo.getFont());
        for (UpdateInfo info : UpdateInfo.values()) {
            int w = fm.stringWidth(info.getDisplayName());
            if (w > maxWidth) maxWidth = w;
        }
        // 适当加点padding
        maxWidth += 32;
        infoCombo.setMaximumSize(new Dimension(maxWidth, infoCombo.getPreferredSize().height));
        infoCombo.setPreferredSize(new Dimension(maxWidth, infoCombo.getPreferredSize().height));
        return infoCombo;
    }
}
