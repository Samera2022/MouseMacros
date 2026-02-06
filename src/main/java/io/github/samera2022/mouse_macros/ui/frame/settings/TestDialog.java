package io.github.samera2022.mouse_macros.ui.frame.settings;

import io.github.samera2022.mouse_macros.Localizer;
import io.github.samera2022.mouse_macros.constant.IconConsts;
import io.github.samera2022.mouse_macros.constant.OtherConsts;
import io.github.samera2022.mouse_macros.manager.ConfigManager;
import io.github.samera2022.mouse_macros.util.ComponentUtil;
import io.github.samera2022.mouse_macros.util.SystemUtil;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

import static io.github.samera2022.mouse_macros.manager.ConfigManager.config;

public class TestDialog extends JDialog {
    public TestDialog(){
        setTitle(Localizer.get("settings"));
        setName("settings");
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/MouseMacros.png"))).getImage());
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

        content.add(subSettingsPanel);

        // 保存按钮单独底部居中
        JButton saveSettingsBtn = new JButton(Localizer.get("settings.save_settings"));
        saveSettingsBtn.addActionListener(e -> {
            config.repeatTime = 1;
            // 热键配置保存到config.keyMap（假设已有相关逻辑）
            ConfigManager.saveConfig(config);
            ConfigManager.reloadConfig();
            dispose();
        });
        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        savePanel.add(saveSettingsBtn);
        savePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // 联动逻辑：根据followSysBox状态控制langCombo和darkModeBox可编辑性，并同步系统设置
        java.awt.event.ItemListener followSysListener = e -> {
            boolean enabled = !followSysBox.isSelected();
            langCombo.setEnabled(enabled);
            if (!enabled) {
                // 跟随系统，自动设置语言和暗色模式
                String sysLang = SystemUtil.getSystemLang(ConfigManager.getAvailableLangs());
                boolean sysDark = SystemUtil.isSystemDarkMode();
                langCombo.setSelectedItem(sysLang);
            }
        };
        followSysBox.addItemListener(followSysListener);
        // 初始化时同步一次
        followSysListener.itemStateChanged(null);

        add(content, BorderLayout.CENTER);
        add(savePanel, BorderLayout.SOUTH);
        // 此处是初始化时设置暗色
        ComponentUtil.setMode(getContentPane(),config.enableDarkMode?OtherConsts.DARK_MODE:OtherConsts.LIGHT_MODE);
        ComponentUtil.adjustFrameWithCache(this, 0, new JComponent[]{content, savePanel});
        setLocationRelativeTo(this);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
}
