package io.github.samera2022.mouse_macros.ui.frame;

import io.github.samera2022.mouse_macros.Localizer;
import io.github.samera2022.mouse_macros.adapter.WindowClosingAdapter;
import io.github.samera2022.mouse_macros.cache.SizeCache;
import io.github.samera2022.mouse_macros.constant.ColorConsts;
import io.github.samera2022.mouse_macros.constant.IconConsts;
import io.github.samera2022.mouse_macros.constant.OtherConsts;
import io.github.samera2022.mouse_macros.manager.CacheManager;
import io.github.samera2022.mouse_macros.manager.ConfigManager;
import io.github.samera2022.mouse_macros.ui.component.CustomFileChooser;
import io.github.samera2022.mouse_macros.ui.frame.settings.AboutDialog;
import io.github.samera2022.mouse_macros.ui.frame.settings.HotkeyDialog;
import io.github.samera2022.mouse_macros.ui.frame.settings.UpdateInfoDialog;
import io.github.samera2022.mouse_macros.util.ComponentUtil;
import io.github.samera2022.mouse_macros.util.SystemUtil;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import static io.github.samera2022.mouse_macros.manager.ConfigManager.config;
import static io.github.samera2022.mouse_macros.ui.frame.MainFrame.MAIN_FRAME;

public class SettingsDialog extends JDialog {

    public SettingsDialog(){
        setTitle(Localizer.get("settings"));
        setName("settings");
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

        // 跟随系统设置（文字在左，勾选框在右）
        JPanel followSysPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel followSysLabel = new JLabel(Localizer.get("settings.follow_system_settings"));
        JCheckBox followSysBox = new JCheckBox(IconConsts.CHECK_BOX);
        followSysBox.setSelected(config.followSystemSettings);
//        followSysBox.setIcon(IconConsts.UNSELECTED_ICON);
//        followSysBox.setSelectedIcon(IconConsts.SELECTED_ICON);
        followSysPanel.add(followSysLabel);
        followSysPanel.add(Box.createHorizontalStrut(10));
        followSysPanel.add(followSysBox);
        followSysPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(Box.createVerticalStrut(10));
        content.add(followSysPanel);

        // 二级设置面板（缩进）
        JPanel subSettingsPanel = new JPanel();
        subSettingsPanel.setLayout(new BoxLayout(subSettingsPanel, BoxLayout.Y_AXIS));
        subSettingsPanel.setBorder(BorderFactory.createEmptyBorder(0, 32, 0, 0)); // 四个空格缩进
        subSettingsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 语言选择
        JLabel langLabel = new JLabel(Localizer.get("settings.switch_lang"));
        String[] langs = ConfigManager.getAvailableLangs();
        JComboBox<String> langCombo = new JComboBox<>(langs);
        langCombo.setSelectedItem(config.lang);
        JPanel langPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        langPanel.add(langLabel);
        langPanel.add(Box.createHorizontalStrut(10));
        langPanel.add(langCombo);
        langPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        subSettingsPanel.add(langPanel);
        subSettingsPanel.add(Box.createVerticalStrut(10));

        // 暗色模式（文字在左，勾选框在右）
        JPanel darkModePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel darkModeLabel = new JLabel(Localizer.get("settings.enable_dark_mode"));
        JCheckBox darkModeBox = new JCheckBox(IconConsts.CHECK_BOX);
        darkModeBox.setSelected(config.enableDarkMode);
//        darkModeBox.setIcon(IconConsts.UNSELECTED_ICON);
//        darkModeBox.setSelectedIcon(IconConsts.SELECTED_ICON);
        darkModePanel.add(darkModeLabel);
        darkModePanel.add(Box.createHorizontalStrut(10));
        darkModePanel.add(darkModeBox);
        darkModePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        subSettingsPanel.add(darkModePanel);
        subSettingsPanel.add(Box.createVerticalStrut(10));

        content.add(subSettingsPanel);

        // 默认存储路径启用开关（无缩进，文字在左，勾选框在右）
        JPanel enableDefaultStoragePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel enableDefaultStorageLabel = new JLabel(Localizer.get("settings.enable_default_storage"));
        JCheckBox enableDefaultStorageBox = new JCheckBox(IconConsts.CHECK_BOX);
        enableDefaultStorageBox.setSelected(config.enableDefaultStorage); // 需要在ConfigManager.config中有此字段
        enableDefaultStoragePanel.add(enableDefaultStorageLabel);
        enableDefaultStoragePanel.add(Box.createHorizontalStrut(10));
        enableDefaultStoragePanel.add(enableDefaultStorageBox);
        enableDefaultStoragePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(Box.createVerticalStrut(10));
        content.add(enableDefaultStoragePanel);

        // 默认存储路径（缩进）
        JLabel pathLabel = new JLabel(Localizer.get("settings.default_mmc_storage_path"));
        JTextField pathField = new JTextField(config.defaultMmcStoragePath, 20);
        JButton browseBtn = new JButton(Localizer.get("settings.browse"));
        browseBtn.addActionListener(e -> {
//            CustomFileChooser chooser = new CustomFileChooser(config.enableDarkMode? OtherConsts.DARK_MODE:OtherConsts.LIGHT_MODE);
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (!pathField.getText().isEmpty())
                chooser.setCurrentDirectory(new java.io.File(pathField.getText()));
            int ret = chooser.showOpenDialog(this);
            if (ret == JFileChooser.APPROVE_OPTION) {
                pathField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });
        // 联动逻辑：enableDefaultStorage控制pathField和browseBtn的可用性
        pathField.setEnabled(enableDefaultStorageBox.isSelected());
        browseBtn.setEnabled(enableDefaultStorageBox.isSelected());
        java.awt.event.ItemListener enableDefaultStorageListener = e -> {
            boolean enabled = enableDefaultStorageBox.isSelected();
            pathField.setEnabled(enabled);
            browseBtn.setEnabled(enabled);
            //MetalLookAndFeel没有关于disabledBackground或者类似的属性……所以只能在这里硬改了
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
        // 新增：加缩进
        JPanel pathIndentPanel = new JPanel();
        pathIndentPanel.setLayout(new BoxLayout(pathIndentPanel, BoxLayout.Y_AXIS));
        pathIndentPanel.setBorder(BorderFactory.createEmptyBorder(0, 32, 0, 0)); // 四个空格缩进
        pathIndentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        pathIndentPanel.add(pathPanel);
        content.add(Box.createVerticalStrut(10));
        content.add(pathIndentPanel);

        // 快速模式勾选框（参照follow_system_settings样式）
        JPanel quickModePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel quickModeLabel = new JLabel(Localizer.get("settings.enable_quick_mode"));
        JCheckBox quickModeBox = new JCheckBox(IconConsts.CHECK_BOX);
        quickModeBox.setSelected(config.enableQuickMode);
        quickModePanel.add(quickModeLabel);
        quickModePanel.add(Box.createHorizontalStrut(10));
        quickModePanel.add(quickModeBox);
        quickModePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(Box.createVerticalStrut(10));
        content.add(quickModePanel);

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
            config.enableDefaultStorage = enableDefaultStorageBox.isSelected(); // 新增保存
            config.enableQuickMode = quickModeBox.isSelected(); // 新增保存
            // 热键配置保存到config.keyMap（假设已有相关逻辑）
            ConfigManager.saveConfig(config);
            ConfigManager.reloadConfig();
            Localizer.load(config.lang);
            MAIN_FRAME.refreshMainFrameTexts();
            // 此处是保存时使用暗色
            // Question: RootPane比ContentPane范围更广，那么此处用getRootPane是否更好？
            // 但是似乎用到ContentPane就已经把能看到的组件都设置好了
            ComponentUtil.setMode(getContentPane(),config.enableDarkMode?OtherConsts.DARK_MODE:OtherConsts.LIGHT_MODE);
            ComponentUtil.setMode(MAIN_FRAME.getContentPane(),config.enableDarkMode?OtherConsts.DARK_MODE:OtherConsts.LIGHT_MODE);
            MainFrame.adjustFrameWidth();
            dispose();
        });
        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        savePanel.add(saveSettingsBtn);
        savePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // 联动逻辑：根据followSysBox状态控制langCombo和darkModeBox可编辑性，并同步系统设置
        java.awt.event.ItemListener followSysListener = e -> {
            boolean enabled = !followSysBox.isSelected();
            langCombo.setEnabled(enabled);
            darkModeBox.setEnabled(enabled);
            if (!enabled) {
                // 跟随系统，自动设置语言和暗色模式
                String sysLang = SystemUtil.getSystemLang(ConfigManager.getAvailableLangs());
                boolean sysDark = SystemUtil.isSystemDarkMode();
                langCombo.setSelectedItem(sysLang);
                darkModeBox.setSelected(sysDark);
            }
        };
        followSysBox.addItemListener(followSysListener);
        // 初始化时同步一次
        followSysListener.itemStateChanged(null);

        add(content, BorderLayout.CENTER);
        add(savePanel, BorderLayout.SOUTH);
        // 此处是初始化时设置暗色
        ComponentUtil.setMode(getContentPane(),config.enableDarkMode?OtherConsts.DARK_MODE:OtherConsts.LIGHT_MODE);
        ComponentUtil.applyWindowSizeCache(this, "settings", 521, 359);
        setLocationRelativeTo(this);
        addWindowListener(new WindowClosingAdapter());
    }
}
