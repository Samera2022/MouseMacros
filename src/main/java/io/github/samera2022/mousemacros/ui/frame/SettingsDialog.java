package io.github.samera2022.mousemacros.ui.frame;


import io.github.samera2022.mousemacros.Localizer;
import io.github.samera2022.mousemacros.adapter.CompMouseAdapter;
import io.github.samera2022.mousemacros.adapter.WindowClosingAdapter;
import io.github.samera2022.mousemacros.config.ConfigManager;
import io.github.samera2022.mousemacros.constant.IconConsts;
import io.github.samera2022.mousemacros.constant.OtherConsts;
import io.github.samera2022.mousemacros.ui.component.DocumentInputFilter;
import io.github.samera2022.mousemacros.ui.frame.settings.*;
import io.github.samera2022.mousemacros.util.ComponentUtil;
import io.github.samera2022.mousemacros.util.ConsoleOutputCapturer;
import io.github.samera2022.mousemacros.util.FileUtil;
import io.github.samera2022.mousemacros.util.SystemUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

import static io.github.samera2022.mousemacros.ui.frame.MainFrame.MAIN_FRAME;

public class SettingsDialog extends JDialog {
    public static final String REPORT_PATH = ConfigManager.CONFIG_DIR + "/reports";
    private static final File REPORTS_DIR = new File(REPORT_PATH);

    private final Map<String, JComponent> componentMap = new HashMap<>();

    static {
        if (!REPORTS_DIR.exists()) {
            REPORTS_DIR.mkdirs();
        }
    }

    public SettingsDialog() {
        setTitle(Localizer.get("settings"));
        setName("settings");
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource(OtherConsts.RELATIVE_PATH + "icons/MouseMacros.png"))).getImage());
        setModal(true);
        setLayout(new BorderLayout(10, 10));

        JPanel content = createContentPanel();
        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);

        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Improve scroll speed

        add(scrollPane, BorderLayout.CENTER);

        JPanel savePanel = createSavePanel();
        add(savePanel, BorderLayout.SOUTH);
        refreshUI(true);
        setPreferredSize(new Dimension(750, 500));
        pack();
        setLocationRelativeTo(null);
        addWindowListener(new WindowClosingAdapter());

        ComponentUtil.setMode(getContentPane(), ConfigManager.getBoolean("enable_dark_mode") ?OtherConsts.DARK_MODE:OtherConsts.LIGHT_MODE);

    }

    private JPanel createContentPanel() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel settingTitle = new JLabel(Localizer.get("settings"));
        settingTitle.setFont(settingTitle.getFont().deriveFont(Font.BOLD, 22f));
        settingTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(settingTitle);
        content.add(Box.createVerticalStrut(10));

        for (SettingsRegistry setting : SettingsRegistry.values()) {
            if (setting.type == OtherConsts.SECTION) {
                content.add(Box.createVerticalStrut(10));
                content.add(new JSeparator());
                content.add(Box.createVerticalStrut(10));
                JLabel sectionTitle = new JLabel(Localizer.getS(setting.i18nKey));
                sectionTitle.setFont(settingTitle.getFont().deriveFont(Font.BOLD, 18f));
                sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
                content.add(sectionTitle);
                content.add(Box.createVerticalStrut(3));
            } else {
                content.add(createSettingComponent(setting));
            }
        }
        
        content.add(Box.createVerticalStrut(10));
        content.add(createOtherButtonsPanel());

        return content;
    }

    private JComponent createSettingComponent(SettingsRegistry setting) {
        JComponent component;
        switch (setting.type) {
            case OtherConsts.CHECK_BOX:
                component = createCheckBox(setting);
                break;
            case OtherConsts.TEXT_FIELD:
                component = createTextField(setting);
                break;
            case OtherConsts.COMBO_BOX:
                component = createComboBox(setting);
                break;
            case OtherConsts.FILE_CHOOSER:
                component = createFileChooserComponent(setting);
                break;
            case OtherConsts.SPECIFIC_TEXT_FIELD:
                component = createSpecificTextField(setting);
                break;
            case OtherConsts.SECTION: // Should not be called for SECTION type, but for safety
                return new JLabel(); // Return an empty component or throw an error
            default:
                throw new IllegalArgumentException("Unsupported component type for " + setting.i18nKey);
        }
        componentMap.put(setting.i18nKey, component);
        return component;
    }

    private JComponent createCheckBox(SettingsRegistry setting) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel label = new JLabel(Localizer.getS(setting.i18nKey));
        JCheckBox checkBox = new JCheckBox(IconConsts.CHECK_BOX);
        checkBox.setName(setting.i18nKey);

        addTooltip(label, checkBox, setting);

        checkBox.addActionListener(e -> {
            ConfigManager.set(setting.i18nKey, checkBox.isSelected());
            refreshUI(false);
        });

        panel.add(label);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(checkBox);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        if (setting.relateTo != null) {
            panel.setBorder(BorderFactory.createEmptyBorder(0, 32, 0, 0));
        }

        return panel;
    }

    private JComponent createTextField(SettingsRegistry setting) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel label = new JLabel(Localizer.getS(setting.i18nKey));
        JTextField textField = new JTextField(setting.columns);
        textField.setName(setting.i18nKey);
        
        addTooltip(label, textField, setting);
        panel.add(label);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(textField);
        
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (setting.relateTo != null) {
            panel.setBorder(BorderFactory.createEmptyBorder(0, 32, 0, 0));
        }

        return panel;
    }
    
    private JComponent createSpecificTextField(SettingsRegistry setting) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel label = new JLabel(Localizer.getS(setting.i18nKey));
        JTextField textField = new JTextField(setting.columns);
        textField.setName(setting.i18nKey);

        DocumentInputFilter filter;
        if (setting == SettingsRegistry.REPEAT_TIME) {
            filter = new DocumentInputFilter() {
                @Override
                public boolean isValidContent(String text) {
                    return text.equals("-1") || text.matches("\\d+") || text.isEmpty() || text.equals("-");
                }
            };
        } else if (setting == SettingsRegistry.REPEAT_DELAY) {
            filter = new DocumentInputFilter() {
                @Override
                public boolean isValidContent(String text) {
                    return text.matches("\\d*(\\.\\d{0,3})?") || text.isEmpty();
                }
            };
        } else {
            // Default filter if no specific one is defined
            filter = new DocumentInputFilter() {
                @Override
                public boolean isValidContent(String text) {
                    return true;
                }
            };
        }

        ((javax.swing.text.AbstractDocument) textField.getDocument()).setDocumentFilter(filter);
        
        addTooltip(label, textField, setting);
        panel.add(label);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(textField);
        
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (setting.relateTo != null) {
            panel.setBorder(BorderFactory.createEmptyBorder(0, 32, 0, 0));
        }

        return panel;
    }

    private JComponent createFileChooserComponent(SettingsRegistry setting) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel label = new JLabel(Localizer.getS(setting.i18nKey));
        JTextField textField = new JTextField(setting.columns);
        textField.setName(setting.i18nKey);
        JButton browseBtn = new JButton(Localizer.get("settings.browse"));

        addTooltip(label, textField, setting, browseBtn);

        browseBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (!textField.getText().isEmpty())
                chooser.setCurrentDirectory(new File(textField.getText()));
            int ret = chooser.showOpenDialog(this);
            if (ret == JFileChooser.APPROVE_OPTION) {
                textField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        panel.add(label);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(textField);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(browseBtn);
        
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (setting.relateTo != null) {
            panel.setBorder(BorderFactory.createEmptyBorder(0, 32, 0, 0));
        }

        return panel;
    }

    private JComponent createComboBox(SettingsRegistry setting) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel label = new JLabel(Localizer.getS(setting.i18nKey));
        
        Vector<String> items = new Vector<>();
        if (setting.size == -1) {
            items.addAll(java.util.Arrays.asList(ConfigManager.getAvailableLangs()));
        } else {
            for (int i = 1; i <= setting.size; i++) {
                items.add(Localizer.getS(setting.i18nKey + "." + i));
            }
        }
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setName(setting.i18nKey);

        addTooltip(label, comboBox, setting);

        panel.add(label);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(comboBox);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (setting.relateTo != null) {
            panel.setBorder(BorderFactory.createEmptyBorder(0, 32, 0, 0));
        }

        return panel;
    }
    
    private JPanel createOtherButtonsPanel() {
        JButton hotkeyBtn = new JButton(Localizer.get("settings.custom_hotkey"));
        hotkeyBtn.addActionListener(e -> SwingUtilities.invokeLater(() -> new HotkeyDialog().setVisible(true)));
        JButton scriptBtn = new JButton(Localizer.get("settings.scripts_manager"));
        scriptBtn.addActionListener(e -> {
            JPanel panel = new JPanel(new BorderLayout());
            String message = String.format(
                    "<html><div style='width:%dpx;'>%s</div></html>",
                    350,
                    Localizer.get("settings.scripts_manager.not_available.message")
            );
            panel.add(new JLabel(Localizer.get(message)), BorderLayout.CENTER);

            int option = JOptionPane.showConfirmDialog(this, panel, Localizer.get("settings.scripts_manager.not_available.title"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    Desktop.getDesktop().browse(new java.net.URI("https://github.com/Samera2022/saMacros"));
                } catch (Exception ignored) {}
            }
        });
        JButton aboutBtn = new JButton(Localizer.get("settings.about_author"));
        aboutBtn.addActionListener(e -> SwingUtilities.invokeLater(() -> new AboutDialog().setVisible(true)));
        JButton updateInfoBtn = new JButton(Localizer.get("settings.update_info"));
        updateInfoBtn.addActionListener(e -> SwingUtilities.invokeLater(() -> new UpdateInfoDialog().setVisible(true)));
        JButton exportLogBtn = new JButton(Localizer.get("settings.export_log"));
        exportLogBtn.addActionListener(e -> {
            try {
                String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                String fileName = "report-" + timeStamp + ".txt";
                String filePath = REPORTS_DIR.getAbsolutePath() + File.separator + fileName;
                FileUtil.writeFile(filePath, ConsoleOutputCapturer.getOutput());
                String message = "<html>" + Localizer.get("settings.export_log.success") + "<br>" + filePath + "</html>";
                JOptionPane.showMessageDialog(this, new JLabel(message), Localizer.get("settings.success"), JOptionPane.INFORMATION_MESSAGE);

                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().open(REPORTS_DIR);
                    } catch (IOException openEx) {
                        System.err.println("Failed to open reports directory: " + openEx.getMessage());
                    }
                }
            } catch (IOException ex) {
                String message = "<html>" + Localizer.get("settings.export_log.failed") + "<br>" + ex.getMessage() + "</html>";
                JOptionPane.showMessageDialog(this, new JLabel(message), Localizer.getS("settings.error"), JOptionPane.ERROR_MESSAGE);
            }
        });
        JPanel hotkeyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        hotkeyPanel.add(hotkeyBtn);
        hotkeyPanel.add(Box.createHorizontalStrut(10));
        hotkeyPanel.add(scriptBtn);
        hotkeyPanel.add(Box.createHorizontalStrut(10));
        hotkeyPanel.add(aboutBtn);
        hotkeyPanel.add(Box.createHorizontalStrut(10));
        hotkeyPanel.add(updateInfoBtn);
        hotkeyPanel.add(Box.createHorizontalStrut(10));
        hotkeyPanel.add(exportLogBtn);
        hotkeyPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return hotkeyPanel;
    }

    private JPanel createSavePanel() {
        JButton saveSettingsBtn = new JButton(Localizer.get("settings.save_settings"));
        saveSettingsBtn.addActionListener(e -> {
            saveSettings();
            ConfigManager.saveConfig();
            Localizer.load(ConfigManager.getString("switch_lang"));
            MAIN_FRAME.refreshMainFrameTexts();
            ComponentUtil.setMode(getContentPane(), ConfigManager.getBoolean("enable_dark_mode") ?OtherConsts.DARK_MODE:OtherConsts.LIGHT_MODE);
            ComponentUtil.setMode(MAIN_FRAME.getContentPane(), ConfigManager.getBoolean("enable_dark_mode") ?OtherConsts.DARK_MODE:OtherConsts.LIGHT_MODE);
            dispose();
        });
        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        savePanel.add(saveSettingsBtn);
        savePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        return savePanel;
    }

    private void addTooltip(JComponent label, JComponent control, SettingsRegistry setting, JComponent... otherControls) {
        boolean hasEnabledTooltip = Localizer.hasKey("settings."+setting.i18nKey + ".tooltip");
        boolean hasDisabledTooltip = Localizer.hasKey("settings."+setting.i18nKey + ".tooltip.disabled");
        int type;
        
        String enabledText = null;
        if (hasEnabledTooltip)
            enabledText = Localizer.getS(setting.i18nKey + ".tooltip");
        String disabledText = null;
        if (hasDisabledTooltip)
            disabledText = Localizer.getS(setting.i18nKey + ".tooltip.disabled");
        else if (setting.relateTo!=null)
            disabledText = String.format(Localizer.get("lang.tooltip.disabled.default"), Localizer.getS(setting.relateTo));
        if (hasEnabledTooltip&&(hasDisabledTooltip||setting.relateTo!=null))
            type = CompMouseAdapter.BOTH;
        else if (hasEnabledTooltip)
            type = CompMouseAdapter.ENABLED;
        else
            type = CompMouseAdapter.DISABLED;
        CompMouseAdapter adapter = new CompMouseAdapter(enabledText, disabledText, type);
        label.addMouseListener(adapter);
        control.addMouseListener(adapter);
        for (JComponent other : otherControls) {
            other.addMouseListener(adapter);
        }
    }

    private void refreshUI(boolean forceUpdateFromConfig) {
        for (SettingsRegistry setting : SettingsRegistry.values()) {
            if (setting.type == OtherConsts.SECTION) continue; // Skip section headers

            JComponent component = componentMap.get(setting.i18nKey);
            if (component == null) continue;

            boolean isEnabled = true;
            if (setting.relateTo != null) {
                boolean parentValue = ConfigManager.getBoolean(setting.relateTo);
                isEnabled = "follow_system_settings".equals(setting.relateTo) != parentValue;
            }

            component.setEnabled(isEnabled);
            for(Component c : ((JPanel)component).getComponents()){
                c.setEnabled(isEnabled);
            }

            if (forceUpdateFromConfig || !isEnabled) {
                Object valueToSet;
                String configKey = setting.i18nKey;
                if (!isEnabled) {
                    if (setting == SettingsRegistry.SWITCH_LANG) {
                        valueToSet = SystemUtil.getSystemLang(ConfigManager.getAvailableLangs());
                    } else if (setting == SettingsRegistry.ENABLE_DARK_MODE) {
                        valueToSet = SystemUtil.isSystemDarkMode();
                    } else {
                        valueToSet = setting.defaultValue;
                    }
                } else {
                    if (setting.type == OtherConsts.CHECK_BOX) {
                        valueToSet = ConfigManager.getBoolean(configKey);
                    } else if (setting.type == OtherConsts.COMBO_BOX) {
                        if (setting.size == -1) {
                            valueToSet = ConfigManager.getString(configKey);
                        } else {
                            valueToSet = ConfigManager.getInt(configKey);
                        }
                    } else { // TEXT_FIELD, FILE_CHOOSER, SPECIFIC_TEXT_FIELD
                        valueToSet = ConfigManager.getString(configKey);
                    }
                }
                applyValueToComponent(component, setting, valueToSet);
            }
        }
    }

    private void applyValueToComponent(JComponent component, SettingsRegistry setting, Object value) {
        switch (setting.type) {
            case OtherConsts.CHECK_BOX:
                JCheckBox checkBox = (JCheckBox) ((JPanel) component).getComponent(2);
                checkBox.setSelected((Boolean) value);
                break;
            case OtherConsts.TEXT_FIELD:
            case OtherConsts.FILE_CHOOSER:
            case OtherConsts.SPECIFIC_TEXT_FIELD:
                JTextField textField = (JTextField) ((JPanel) component).getComponent(2);
                textField.setText(String.valueOf(value));
                break;
            case OtherConsts.COMBO_BOX:
                JComboBox<?> comboBox = (JComboBox<?>) ((JPanel) component).getComponent(2);
                if (value instanceof Integer) {
                    comboBox.setSelectedIndex((Integer) value - 1);
                } else {
                    comboBox.setSelectedItem(value);
                }
                break;
        }
    }

    private void saveSettings() {
        for (SettingsRegistry setting : SettingsRegistry.values()) {
            if (setting.type == OtherConsts.SECTION) continue; // Skip section headers

            JComponent component = componentMap.get(setting.i18nKey);
            if (component == null) continue;

            if (component.isEnabled()) {
                 switch (setting.type) {
                    case OtherConsts.CHECK_BOX:
                        // Checkbox state is handled by its ActionListener, no need to save here
                        break;
                    case OtherConsts.TEXT_FIELD:
                    case OtherConsts.FILE_CHOOSER:
                    case OtherConsts.SPECIFIC_TEXT_FIELD:
                        JTextField textField = (JTextField) ((JPanel) component).getComponent(2);
                        ConfigManager.set(setting.i18nKey, textField.getText());
                        break;
                    case OtherConsts.COMBO_BOX:
                        JComboBox<?> comboBox = (JComboBox<?>) ((JPanel) component).getComponent(2);
                        if (setting.size != -1) {
                            ConfigManager.set(setting.i18nKey, comboBox.getSelectedIndex() + 1);
                        } else {
                            ConfigManager.set(setting.i18nKey, comboBox.getSelectedItem());
                        }
                        break;
                }
            }
        }
    }
}
