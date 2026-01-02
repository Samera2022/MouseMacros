package io.github.samera2022.mouse_macros.ui.frame;

import io.github.samera2022.mouse_macros.Localizer;
import io.github.samera2022.mouse_macros.adapter.MouseCheckAdapter;
import io.github.samera2022.mouse_macros.adapter.MouseCheckDisabledAdapter;
import io.github.samera2022.mouse_macros.adapter.WindowClosingAdapter;
import io.github.samera2022.mouse_macros.constant.ColorConsts;
import io.github.samera2022.mouse_macros.constant.IconConsts;
import io.github.samera2022.mouse_macros.constant.OtherConsts;
import io.github.samera2022.mouse_macros.manager.ConfigManager;
import io.github.samera2022.mouse_macros.ui.frame.settings.AboutDialog;
import io.github.samera2022.mouse_macros.ui.frame.settings.HotkeyDialog;
import io.github.samera2022.mouse_macros.ui.frame.settings.UpdateInfoDialog;
import io.github.samera2022.mouse_macros.util.ComponentUtil;
import io.github.samera2022.mouse_macros.util.SystemUtil;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

import static io.github.samera2022.mouse_macros.manager.ConfigManager.config;
import static io.github.samera2022.mouse_macros.ui.frame.MainFrame.MAIN_FRAME;

public class SettingsDialog extends JDialog {

    public SettingsDialog(){
        setTitle(Localizer.get("settings"));
        setName("settings");
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/MouseMacros.png"))).getImage());
        setModal(true);
        setLayout(new BorderLayout(10, 10));
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel settingTitle = new JLabel(Localizer.get("settings"));
        settingTitle.setFont(settingTitle.getFont().deriveFont(Font.BOLD, 18f));
        settingTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(settingTitle);
        content.add(Box.createVerticalStrut(10));
        content.add(new JSeparator());

        JLabel appearanceTitle = new JLabel(Localizer.get("settings.appearance_section"));
        appearanceTitle.setFont(settingTitle.getFont().deriveFont(Font.BOLD, 16f));
        appearanceTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(appearanceTitle);
        content.add(Box.createVerticalStrut(3));

        // --- 外观相关选项 ---

        // 1. 跟随系统设置 (一级)
        JPanel followSysPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel followSysLabel = new JLabel(Localizer.get("settings.follow_system_settings"));
        JCheckBox followSysBox = new JCheckBox(IconConsts.CHECK_BOX);
        followSysBox.setSelected(config.followSystemSettings);
        followSysPanel.add(followSysLabel);
        followSysPanel.add(Box.createHorizontalStrut(10));
        followSysPanel.add(followSysBox);
        followSysPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(Box.createVerticalStrut(10));
        content.add(followSysPanel);

        // 缩进的子面板 (仅包含语言和暗色模式)
        JPanel subSettingsPanel = new JPanel();
        subSettingsPanel.setLayout(new BoxLayout(subSettingsPanel, BoxLayout.Y_AXIS));
        subSettingsPanel.setBorder(BorderFactory.createEmptyBorder(0, 32, 0, 0)); // 保持缩进
        subSettingsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 语言选择 (二级，保持缩进)
        JLabel langLabel = new JLabel(Localizer.get("settings.switch_lang"));
        String[] langs = ConfigManager.getAvailableLangs();
        JComboBox<String> langCombo = new JComboBox<>(langs);
        langCombo.addMouseListener(new MouseCheckDisabledAdapter(Localizer.get("settings.enable_dark_mode.disabled.tooltip")));
        langCombo.setSelectedItem(config.lang);
        JPanel langPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        langPanel.add(langLabel);
        langPanel.add(Box.createHorizontalStrut(10));
        langPanel.add(langCombo);
        langPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        subSettingsPanel.add(langPanel);
        subSettingsPanel.add(Box.createVerticalStrut(10));

        // 暗色模式 (二级，保持缩进)
        JPanel darkModePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel darkModeLabel = new JLabel(Localizer.get("settings.enable_dark_mode"));
        JCheckBox darkModeBox = new JCheckBox(IconConsts.CHECK_BOX);
        darkModeBox.addMouseListener(new MouseCheckDisabledAdapter(Localizer.get("settings.enable_dark_mode.disabled.tooltip")));
        darkModeBox.setSelected(config.enableDarkMode);
        darkModePanel.add(darkModeLabel);
        darkModePanel.add(Box.createHorizontalStrut(10));
        darkModePanel.add(darkModeBox);
        darkModePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        subSettingsPanel.add(darkModePanel);

        // 将 subSettingsPanel 添加到 content
        content.add(subSettingsPanel);

        // [修改点 1] 允许长字符串 (移出 subSettingsPanel，变回一级对齐)
        JPanel allowLongStrPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel allowLongStrLabel = new JLabel(Localizer.get("settings.allow_long_str"));
        allowLongStrLabel.addMouseListener(new MouseCheckAdapter(Localizer.get("settings.allow_long_str.tooltip")));
        JCheckBox allowLongStrCheckBox = new JCheckBox(IconConsts.CHECK_BOX);
        allowLongStrCheckBox.setSelected(config.allowLongStr);
        allowLongStrPanel.add(allowLongStrLabel);
        allowLongStrPanel.add(Box.createHorizontalStrut(10));
        allowLongStrPanel.add(allowLongStrCheckBox);
        allowLongStrPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(Box.createVerticalStrut(10)); // 补充间距
        content.add(allowLongStrPanel); // 直接添加到 content

        // [修改点 1] 调整窗体大小模式 (移出 subSettingsPanel，变回一级对齐)
        JPanel rfmPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel rfmLabel = new JLabel(Localizer.get("settings.readjust_frame_mode"));
        rfmLabel.addMouseListener(new MouseCheckAdapter(Localizer.get("settings.readjust_frame_mode.tooltip")));
        String[] rfmModes = {ConfigManager.RFM_MIXED, ConfigManager.RFM_STANDARDIZED, ConfigManager.RFM_MEMORIZED};
        JComboBox<String> rfmCombo = new JComboBox<>(rfmModes);
        rfmCombo.setSelectedItem(config.readjustFrameMode);
        rfmPanel.add(rfmLabel);
        rfmPanel.add(Box.createHorizontalStrut(10));
        rfmPanel.add(rfmCombo);
        rfmPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(rfmPanel); // 直接添加到 content

        // 小分割线
        JSeparator smallSep = new JSeparator();
        smallSep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        content.add(Box.createVerticalStrut(8));
        content.add(smallSep);

        // --- 系统设置 ---
        JLabel systemTitle = new JLabel(Localizer.get("settings.system_section"));
        systemTitle.setFont(settingTitle.getFont().deriveFont(Font.BOLD, 16f));
        systemTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(systemTitle);
        content.add(Box.createVerticalStrut(3));

        JPanel quickModePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel quickModeLabel = new JLabel(Localizer.get("settings.enable_quick_mode"));
        quickModeLabel.addMouseListener(new MouseCheckAdapter(Localizer.get("settings.enable_quick_mode.tooltip")));
        JCheckBox quickModeBox = new JCheckBox(IconConsts.CHECK_BOX);
        quickModeBox.setSelected(config.enableQuickMode);
        quickModePanel.add(quickModeLabel);
        quickModePanel.add(Box.createHorizontalStrut(10));
        quickModePanel.add(quickModeBox);
        quickModePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(Box.createVerticalStrut(10));
        content.add(quickModePanel);

        // 默认存储模式 (现在在快速模式之后)
        JPanel enableDefaultStoragePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel enableDefaultStorageLabel = new JLabel(Localizer.get("settings.enable_default_storage"));
        enableDefaultStorageLabel.addMouseListener(new MouseCheckAdapter(Localizer.get("settings.default_mmc_storage_path.tooltip")));
        JCheckBox enableDefaultStorageBox = new JCheckBox(IconConsts.CHECK_BOX);
        enableDefaultStorageBox.setSelected(config.enableDefaultStorage);
        enableDefaultStoragePanel.add(enableDefaultStorageLabel);
        enableDefaultStoragePanel.add(Box.createHorizontalStrut(10));
        enableDefaultStoragePanel.add(enableDefaultStorageBox);
        enableDefaultStoragePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(Box.createVerticalStrut(10));
        content.add(enableDefaultStoragePanel);

        JPanel subSettingsPanel2 = new JPanel();
        subSettingsPanel2.setLayout(new BoxLayout(subSettingsPanel2, BoxLayout.Y_AXIS));
        subSettingsPanel2.setBorder(BorderFactory.createEmptyBorder(0, 32, 0, 0));
        subSettingsPanel2.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 默认存储路径 (保持作为默认存储的子项缩进)
        JLabel pathLabel = new JLabel(Localizer.get("settings.default_mmc_storage_path"));
        JTextField pathField = new JTextField(config.defaultMmcStoragePath, 20);
        JButton browseBtn = new JButton(Localizer.get("settings.browse"));
        browseBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (!pathField.getText().isEmpty())
                chooser.setCurrentDirectory(new java.io.File(pathField.getText()));
            int ret = chooser.showOpenDialog(this);
            if (ret == JFileChooser.APPROVE_OPTION) {
                pathField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });
        pathField.setEnabled(enableDefaultStorageBox.isSelected());
        pathField.addMouseListener(new MouseCheckDisabledAdapter(Localizer.get("settings.default_mmc_storage_path.disabled.tooltip")));
        browseBtn.setEnabled(enableDefaultStorageBox.isSelected());
        browseBtn.addMouseListener(new MouseCheckDisabledAdapter(Localizer.get("settings.default_mmc_storage_path.disabled.tooltip")));
        java.awt.event.ItemListener enableDefaultStorageListener = e -> {
            boolean enabled = enableDefaultStorageBox.isSelected();
            pathField.setEnabled(enabled);
            browseBtn.setEnabled(enabled);
            if (!enabled) {
                pathField.setBackground(config.enableDarkMode?ColorConsts.DARK_MODE_DISABLED_BACKGROUND:ColorConsts.LIGHT_MODE_DISABLED_BACKGROUND);
                pathField.setForeground(config.enableDarkMode?ColorConsts.DARK_MODE_DISABLED_FOREGROUND:ColorConsts.LIGHT_MODE_DISABLED_FOREGROUND);
            } else {
                pathField.setBackground(config.enableDarkMode?ColorConsts.DARK_MODE_PANEL_BACKGROUND:ColorConsts.LIGHT_MODE_PANEL_BACKGROUND);
                pathField.setForeground(config.enableDarkMode?ColorConsts.DARK_MODE_PANEL_FOREGROUND:ColorConsts.LIGHT_MODE_PANEL_FOREGROUND);
            }
        };
        enableDefaultStorageBox.addItemListener(enableDefaultStorageListener);
        enableDefaultStorageListener.itemStateChanged(null);
        JPanel pathPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pathPanel.add(pathLabel);
        pathPanel.add(Box.createHorizontalStrut(10));
        pathPanel.add(pathField);
        pathPanel.add(Box.createHorizontalStrut(10));
        pathPanel.add(browseBtn);
        pathPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        subSettingsPanel2.add(pathPanel);
        content.add(Box.createVerticalStrut(10));
        content.add(subSettingsPanel2);


        // 热键自定义 + 关于作者 + 更新日志 三列按钮
        JButton hotkeyBtn = new JButton(Localizer.get("settings.custom_hotkey"));
        hotkeyBtn.addActionListener(e -> SwingUtilities.invokeLater(() -> new HotkeyDialog().setVisible(true)));
        JButton aboutBtn = new JButton(Localizer.get("settings.about_author"));
        aboutBtn.addActionListener(e -> SwingUtilities.invokeLater(() -> new AboutDialog().setVisible(true)));
        JButton updateInfoBtn = new JButton(Localizer.get("settings.update_info"));
        updateInfoBtn.addActionListener(e -> SwingUtilities.invokeLater(() -> new UpdateInfoDialog().setVisible(true)));
        JPanel hotkeyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        hotkeyPanel.add(hotkeyBtn);
        hotkeyPanel.add(Box.createHorizontalStrut(10));
        hotkeyPanel.add(aboutBtn);
        hotkeyPanel.add(Box.createHorizontalStrut(10));
        hotkeyPanel.add(updateInfoBtn);
        hotkeyPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(Box.createVerticalStrut(10));
        content.add(hotkeyPanel);

        // 保存按钮单独底部居中
        JButton saveSettingsBtn = new JButton(Localizer.get("settings.save_settings"));
        saveSettingsBtn.addActionListener(e -> {
            config.followSystemSettings = followSysBox.isSelected();
            config.lang = (String) langCombo.getSelectedItem();
            config.defaultMmcStoragePath = pathField.getText();
            config.enableDarkMode = darkModeBox.isSelected();
            config.enableDefaultStorage = enableDefaultStorageBox.isSelected();
            config.enableQuickMode = quickModeBox.isSelected();
            config.allowLongStr = allowLongStrCheckBox.isSelected();
            config.readjustFrameMode = (String) rfmCombo.getSelectedItem();
            ConfigManager.saveConfig(config);
            ConfigManager.reloadConfig();
            Localizer.load(config.lang);
            MAIN_FRAME.refreshMainFrameTexts();
            // 此处是保存时使用暗色
            // Question: RootPane比ContentPane范围更广，那么此处用getRootPane是否更好？
            // 但是似乎用到ContentPane就已经把能看到的组件都设置好了
            ComponentUtil.setMode(getContentPane(),config.enableDarkMode?OtherConsts.DARK_MODE:OtherConsts.LIGHT_MODE);
            ComponentUtil.setMode(MAIN_FRAME.getContentPane(),config.enableDarkMode?OtherConsts.DARK_MODE:OtherConsts.LIGHT_MODE);
            dispose();
        });
        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        savePanel.add(saveSettingsBtn);
        savePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        add(content, BorderLayout.CENTER);
        add(savePanel, BorderLayout.SOUTH);
        // 此处是初始化时设置暗色
        ComponentUtil.setMode(getContentPane(),config.enableDarkMode?OtherConsts.DARK_MODE:OtherConsts.LIGHT_MODE);
        ComponentUtil.adjustFrameWithCache(this, 430,
                new JComponent[]{settingTitle},
                new JComponent[]{followSysLabel, followSysBox},
                new JComponent[]{subSettingsPanel},
                new JComponent[]{enableDefaultStorageLabel, enableDefaultStorageBox},
                new JComponent[]{subSettingsPanel2},
                new JComponent[]{quickModeLabel, quickModeBox},
                new JComponent[]{allowLongStrLabel, allowLongStrCheckBox},
                new JComponent[]{rfmLabel, rfmCombo},
                new JComponent[]{hotkeyBtn, aboutBtn, updateInfoBtn},
                new JComponent[]{saveSettingsBtn}
        );
        setLocationRelativeTo(this);
        addWindowListener(new WindowClosingAdapter());
    }
}