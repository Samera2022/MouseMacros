package io.github.samera2022.mousemacros.app.ui.frame;

import io.github.samera2022.mousemacros.app.Localizer;
import io.github.samera2022.mousemacros.app.config.ConfigManager;
import io.github.samera2022.mousemacros.app.constant.OtherConsts;
import io.github.samera2022.mousemacros.app.ui.adapter.CompMouseAdapter;
import io.github.samera2022.mousemacros.app.ui.adapter.WindowClosingAdapter;
import io.github.samera2022.mousemacros.app.ui.component.DocumentInputFilter;
import io.github.samera2022.mousemacros.app.ui.frame.settings.*;
import io.github.samera2022.mousemacros.app.util.ComponentUtil;
import io.github.samera2022.mousemacros.app.util.ConsoleOutputCapturer;
import io.github.samera2022.mousemacros.app.util.FileUtil;
import io.github.samera2022.mousemacros.app.util.SystemUtil;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

import static io.github.samera2022.mousemacros.app.ui.frame.MainFrame.MAIN_FRAME;

/**
 * Modernized Settings dialog.
 *
 * Main goals:
 * 1. Keep the existing SettingsRegistry / ConfigManager logic.
 * 2. Preserve the hierarchy:
 *      Follow System Settings
 *          Switch Language
 *          Enable Dark Mode
 * 3. Remove the "stretched Swing panel" look caused by BoxLayout + unlimited JPanel heights.
 * 4. Use a compact centered content column, section cards, toggle switches and a thin scrollbar.
 * 5. Keep componentMap compatible with refreshUI(), applyValueToComponent() and saveSettings().
 */
public class SettingsDialog extends JDialog {

    public static final String REPORT_PATH = ConfigManager.CONFIG_DIR + "/reports";
    private static final File REPORTS_DIR = new File(REPORT_PATH);

    private static final int CONTENT_WIDTH = 720;
    private static final int ROW_HEIGHT = 46;
    private static final int CHILD_INDENT = 26;

    private static final int NORMAL_CONTROL_WIDTH = 190;
    private static final int SMALL_CONTROL_WIDTH = 110;
    private static final int FIELD_HEIGHT = 32;

    private final Map<String, JComponent> componentMap = new HashMap<>();

    static {
        if (!REPORTS_DIR.exists()) {
            REPORTS_DIR.mkdirs();
        }
    }

    public SettingsDialog() {
        super(MAIN_FRAME);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle(Localizer.get("settings"));
        setName("settings");

        setIconImage(
                new ImageIcon(
                        Objects.requireNonNull(
                                getClass().getResource(
                                        OtherConsts.RELATIVE_PATH + "icons/MouseMacros.png"
                                )
                        )
                ).getImage()
        );

        setModal(true);
        setLayout(new BorderLayout());

        JPanel content = createContentPanel();

        JScrollPane scrollPane = new JScrollPane(
                content,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        installModernScrollBar(scrollPane.getVerticalScrollBar());

        add(scrollPane, BorderLayout.CENTER);
        add(createSavePanel(), BorderLayout.SOUTH);

        refreshUI(true);

        setPreferredSize(new Dimension(840, 700));
        setMinimumSize(new Dimension(760, 580));
        setResizable(true);

        pack();
        setLocationRelativeTo(null);
        addWindowListener(new WindowClosingAdapter());

        ComponentUtil.setMode(
                getContentPane(),
                ConfigManager.getBoolean("enable_dark_mode")
                        ? OtherConsts.DARK_MODE
                        : OtherConsts.LIGHT_MODE
        );
    }

    // -------------------------------------------------------------------------
    // Main content
    // -------------------------------------------------------------------------

    private JPanel createContentPanel() {
        JPanel wrapper = new JPanel(new GridBagLayout());

        JPanel content = new ContentColumn();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(28, 0, 32, 0));
        content.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel settingTitle = new JLabel(Localizer.get("settings"));
        settingTitle.setFont(settingTitle.getFont().deriveFont(Font.BOLD, 26f));
        settingTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(settingTitle);
        content.add(Box.createVerticalStrut(26));

        SectionCard currentCard = null;
        boolean firstSection = true;

        for (SettingsRegistry setting : SettingsRegistry.values()) {
            if (setting.type == OtherConsts.SECTION) {
                if (!firstSection) {
                    content.add(Box.createVerticalStrut(26));
                }
                firstSection = false;

                JLabel sectionTitle = new JLabel(Localizer.getS(setting.i18nKey));
                sectionTitle.setFont(sectionTitle.getFont().deriveFont(Font.BOLD, 17f));
                sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

                content.add(sectionTitle);
                content.add(Box.createVerticalStrut(9));

                currentCard = new SectionCard();
                currentCard.setAlignmentX(Component.LEFT_ALIGNMENT);
                content.add(currentCard);
                continue;
            }

            if (currentCard == null) {
                continue;
            }

            // Expandable storage section:
            // "enable_default_storage" owns DEFAULT_MMC_STORAGE_PATH.
            if ("enable_default_storage".equals(setting.i18nKey)) {
                currentCard.add(createExpandableStorageSection(setting));
                continue;
            }

            if (setting == SettingsRegistry.DEFAULT_MMC_STORAGE_PATH) {
                continue; // already rendered in createExpandableStorageSection
            }

            // Expandable macro section:
            // "enable_custom_macro_settings" owns REPEAT_TIME + REPEAT_DELAY.
            if ("enable_custom_macro_settings".equals(setting.i18nKey)) {
                currentCard.add(createExpandableMacroSection(setting));
                continue;
            }

            if (setting == SettingsRegistry.REPEAT_TIME
                    || setting == SettingsRegistry.REPEAT_DELAY) {
                continue; // already rendered in createExpandableMacroSection
            }

            currentCard.add(createSettingComponent(setting));
        }

        content.add(Box.createVerticalStrut(28));
        content.add(createOtherButtonsPanel());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        wrapper.add(content, gbc);
        return wrapper;
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

            case OtherConsts.SECTION:
                return new JLabel();

            default:
                throw new IllegalArgumentException(
                        "Unsupported component type for " + setting.i18nKey
                );
        }

        componentMap.put(setting.i18nKey, component);
        return component;
    }

    // -------------------------------------------------------------------------
    // Common setting row helpers
    // -------------------------------------------------------------------------

    private JPanel createSettingRow(SettingsRegistry setting) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Important: prevent BoxLayout from vertically stretching each setting row.
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, ROW_HEIGHT));
        panel.setPreferredSize(new Dimension(CONTENT_WIDTH - 28, ROW_HEIGHT));

        if (setting.relateTo != null) {
            panel.setBorder(
                    BorderFactory.createEmptyBorder(
                            0,
                            CHILD_INDENT,
                            0,
                            0
                    )
            );
        }

        return panel;
    }

    private JLabel createSettingLabel(SettingsRegistry setting) {
        JLabel label = new JLabel(Localizer.getS(setting.i18nKey));
        label.setFont(label.getFont().deriveFont(Font.PLAIN, 14f));
        return label;
    }

    /**
     * Adds components in a fixed 3-component structure:
     *
     * index 0 = label
     * index 1 = spacer
     * index 2 = control
     *
     * This deliberately preserves compatibility with applyValueToComponent()
     * and saveSettings(), which access component index 2.
     */
    private void addLabelAndControl(
            JPanel panel,
            JLabel label,
            JComponent control
    ) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(Box.createHorizontalStrut(18), gbc);

        gbc.gridx = 2;
        panel.add(control, gbc);
    }

    // -------------------------------------------------------------------------
    // Standard setting components
    // -------------------------------------------------------------------------

    private JComponent createCheckBox(SettingsRegistry setting) {
        JPanel panel = createSettingRow(setting);
        JLabel label = createSettingLabel(setting);

        JCheckBox checkBox = new ToggleSwitch();
        checkBox.setName(setting.i18nKey);

        addTooltip(label, checkBox, setting);

        checkBox.addActionListener(e -> {
            refreshUI(false);
        });

        addLabelAndControl(panel, label, checkBox);
        return panel;
    }

    private JComponent createTextField(SettingsRegistry setting) {
        JPanel panel = createSettingRow(setting);
        JLabel label = createSettingLabel(setting);

        JTextField textField = new JTextField();
        textField.setName(setting.i18nKey);
        textField.setPreferredSize(
                new Dimension(NORMAL_CONTROL_WIDTH, FIELD_HEIGHT)
        );
        textField.setFont(textField.getFont().deriveFont(Font.PLAIN, 13f));
        textField.setMargin(new Insets(2, 8, 2, 8));
        textField.putClientProperty("JComponent.roundRect", true);

        addTooltip(label, textField, setting);
        addLabelAndControl(panel, label, textField);

        return panel;
    }

    private JComponent createSpecificTextField(SettingsRegistry setting) {
        JPanel panel = createSettingRow(setting);
        JLabel label = createSettingLabel(setting);

        JTextField textField = new JTextField();
        textField.setName(setting.i18nKey);
        textField.setPreferredSize(
                new Dimension(SMALL_CONTROL_WIDTH, FIELD_HEIGHT)
        );
        textField.setFont(textField.getFont().deriveFont(Font.PLAIN, 13f));
        textField.setMargin(new Insets(2, 8, 2, 8));
        textField.putClientProperty("JComponent.roundRect", true);

        DocumentInputFilter filter;

        if (setting == SettingsRegistry.REPEAT_TIME) {
            filter = new DocumentInputFilter() {
                @Override
                public boolean isValidContent(String text) {
                    return text.equals("-1")
                            || text.matches("\\d+")
                            || text.isEmpty()
                            || text.equals("-");
                }
            };
        } else if (setting == SettingsRegistry.REPEAT_DELAY) {
            filter = new DocumentInputFilter() {
                @Override
                public boolean isValidContent(String text) {
                    return text.matches("\\d*(\\.\\d{0,3})?")
                            || text.isEmpty();
                }
            };
        } else {
            filter = new DocumentInputFilter() {
                @Override
                public boolean isValidContent(String text) {
                    return true;
                }
            };
        }

        ((AbstractDocument) textField.getDocument()).setDocumentFilter(filter);

        addTooltip(label, textField, setting);
        addLabelAndControl(panel, label, textField);

        return panel;
    }

    private JComponent createFileChooserComponent(SettingsRegistry setting) {
        JPanel panel = createSettingRow(setting);
        JLabel label = createSettingLabel(setting);

        JTextField textField = new JTextField();
        textField.setName(setting.i18nKey);
        textField.setPreferredSize(new Dimension(250, FIELD_HEIGHT));
        textField.setFont(textField.getFont().deriveFont(Font.PLAIN, 13f));
        textField.setMargin(new Insets(2, 8, 2, 8));
        textField.putClientProperty("JComponent.roundRect", true);

        JButton browseBtn = new ModernButton(
                Localizer.get("settings.browse"),
                false
        );
        browseBtn.setPreferredSize(new Dimension(92, FIELD_HEIGHT));

        addTooltip(label, textField, setting, browseBtn);

        browseBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            if (!textField.getText().isEmpty()) {
                chooser.setCurrentDirectory(
                        new File(textField.getText())
                );
            }

            int ret = chooser.showOpenDialog(this);

            if (ret == JFileChooser.APPROVE_OPTION) {
                textField.setText(
                        chooser.getSelectedFile().getAbsolutePath()
                );
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(Box.createHorizontalStrut(18), gbc);

        // Important: textField stays at component index 2.
        gbc.gridx = 2;
        panel.add(textField, gbc);

        gbc.gridx = 3;
        panel.add(Box.createHorizontalStrut(8), gbc);

        gbc.gridx = 4;
        panel.add(browseBtn, gbc);

        return panel;
    }

    private JComponent createComboBox(SettingsRegistry setting) {
        JPanel panel = createSettingRow(setting);
        JLabel label = createSettingLabel(setting);

        Vector<String> items = new Vector<>();

        if (setting.size == -1) {
            items.addAll(
                    java.util.Arrays.asList(
                            ConfigManager.getAvailableLangs()
                    )
            );
        } else {
            for (int i = 1; i <= setting.size; i++) {
                items.add(
                        Localizer.getS(
                                setting.i18nKey + "." + i
                        )
                );
            }
        }

        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setName(setting.i18nKey);
        comboBox.setFont(
                comboBox.getFont().deriveFont(Font.PLAIN, 13f)
        );
        comboBox.setPreferredSize(
                new Dimension(NORMAL_CONTROL_WIDTH, FIELD_HEIGHT)
        );
        comboBox.putClientProperty("JComponent.roundRect", true);

        addTooltip(label, comboBox, setting);
        addLabelAndControl(panel, label, comboBox);

        return panel;
    }

    // -------------------------------------------------------------------------
    // Expandable settings
    // -------------------------------------------------------------------------

    private JPanel createExpandableStorageSection(SettingsRegistry parentSetting) {
        JPanel container = new JPanel();
        container.setOpaque(false);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE)
        );

        JComponent toggleRow = createCheckBox(parentSetting);
        componentMap.put(parentSetting.i18nKey, toggleRow);

        JComponent expandedContent =
                createSettingComponent(SettingsRegistry.DEFAULT_MMC_STORAGE_PATH);

        expandedContent.setVisible(
                ConfigManager.getBoolean(parentSetting.i18nKey)
        );

        JCheckBox toggle =
                (JCheckBox) ((JPanel) toggleRow).getComponent(2);

        toggle.addActionListener(e -> {
            boolean selected = toggle.isSelected();
            expandedContent.setVisible(selected);

            container.revalidate();
            container.repaint();
        });

        container.add(toggleRow);
        container.add(expandedContent);

        return container;
    }

    private JPanel createExpandableMacroSection(SettingsRegistry parentSetting) {
        JPanel container = new JPanel();
        container.setOpaque(false);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE)
        );

        JComponent toggleRow = createCheckBox(parentSetting);
        componentMap.put(parentSetting.i18nKey, toggleRow);

        JComponent repeatTime =
                createSettingComponent(SettingsRegistry.REPEAT_TIME);
        JComponent repeatDelay =
                createSettingComponent(SettingsRegistry.REPEAT_DELAY);

        JPanel expandedContent = new JPanel();
        expandedContent.setOpaque(false);
        expandedContent.setLayout(
                new BoxLayout(expandedContent, BoxLayout.Y_AXIS)
        );
        expandedContent.setAlignmentX(Component.LEFT_ALIGNMENT);

        expandedContent.add(repeatTime);
        expandedContent.add(repeatDelay);

        expandedContent.setVisible(
                ConfigManager.getBoolean(parentSetting.i18nKey)
        );

        JCheckBox toggle =
                (JCheckBox) ((JPanel) toggleRow).getComponent(2);

        toggle.addActionListener(e -> {
            boolean selected = toggle.isSelected();
            expandedContent.setVisible(selected);

            container.revalidate();
            container.repaint();
        });

        container.add(toggleRow);
        container.add(expandedContent);

        return container;
    }

    // -------------------------------------------------------------------------
    // Shortcuts / About / Advanced
    // -------------------------------------------------------------------------

    private JPanel createOtherButtonsPanel() {
        JPanel container = new JPanel();
        container.setOpaque(false);
        container.setLayout(
                new BoxLayout(container, BoxLayout.Y_AXIS)
        );
        container.setAlignmentX(Component.LEFT_ALIGNMENT);

        appendSection(
                container,
                Localizer.getS("section.shortcuts"),
                createActionRow(
                        Localizer.getS("keyboard_shortcut"),
                        Localizer.getS("configure"),
                        () -> SwingUtilities.invokeLater(
                                () -> new HotkeyDialog().setVisible(true)
                        )
                ),
                createActionRow(
                        Localizer.getS("welcome_wizard"),
                        Localizer.getS("open"),
                        () -> SwingUtilities.invokeLater(() -> {
                            io.github.samera2022.mousemacros.app.ui.dialog.WelcomeDialog dialog =
                                    new io.github.samera2022.mousemacros.app.ui.dialog.WelcomeDialog(
                                            MAIN_FRAME
                                    );
                            dialog.setVisible(true);
                        })
                )
        );

        container.add(Box.createVerticalStrut(26));

        appendSection(
                container,
                Localizer.getS("section.about"),
                createNavRow(
                        Localizer.getS("author"),
                        () -> SwingUtilities.invokeLater(
                                () -> new AboutDialog().setVisible(true)
                        )
                ),
                createNavRow(
                        Localizer.getS("update_information"),
                        () -> SwingUtilities.invokeLater(
                                () -> new UpdateInfoDialog().setVisible(true)
                        )
                ),
                createNavRow(
                        Localizer.get("settings.export_log"),
                        this::handleExportLog
                )
        );

        container.add(Box.createVerticalStrut(26));

        appendSection(
                container,
                Localizer.getS("section.advanced"),
                createNavRow(
                        Localizer.getS("script_manager"),
                        this::handleScriptManager
                )
        );

        return container;
    }

    private void appendSection(
            JPanel container,
            String title,
            JComponent... rows
    ) {
        JLabel sectionTitle = new JLabel(title);
        sectionTitle.setFont(
                sectionTitle.getFont().deriveFont(Font.BOLD, 17f)
        );
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        container.add(sectionTitle);
        container.add(Box.createVerticalStrut(9));

        SectionCard card = new SectionCard();
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (JComponent row : rows) {
            card.add(row);
        }

        container.add(card);
    }

    private JPanel createActionRow(
            String labelText,
            String buttonText,
            Runnable action
    ) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, ROW_HEIGHT)
        );
        row.setPreferredSize(
                new Dimension(CONTENT_WIDTH - 28, ROW_HEIGHT)
        );

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel label = new JLabel(labelText);
        label.setFont(
                label.getFont().deriveFont(Font.PLAIN, 14f)
        );

        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        row.add(label, gbc);

        JButton button = new ModernButton(buttonText, false);
        button.setPreferredSize(new Dimension(110, 32));
        button.addActionListener(e -> action.run());

        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        row.add(button, gbc);

        return row;
    }

    private JPanel createNavRow(
            String labelText,
            Runnable action
    ) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, ROW_HEIGHT)
        );
        row.setPreferredSize(
                new Dimension(CONTENT_WIDTH - 28, ROW_HEIGHT)
        );

        Cursor hand = Cursor.getPredefinedCursor(
                Cursor.HAND_CURSOR
        );

        row.setCursor(hand);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel label = new JLabel(labelText);
        label.setFont(
                label.getFont().deriveFont(Font.PLAIN, 14f)
        );
        label.setCursor(hand);

        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        row.add(label, gbc);

        JLabel arrow = new JLabel("\u203A"); // 鈥?
        arrow.setFont(
                arrow.getFont().deriveFont(Font.PLAIN, 24f)
        );
        arrow.setCursor(hand);

        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        row.add(arrow, gbc);

        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }
        };

        row.addMouseListener(adapter);
        label.addMouseListener(adapter);
        arrow.addMouseListener(adapter);

        return row;
    }

    // -------------------------------------------------------------------------
    // Other actions
    // -------------------------------------------------------------------------

    private void handleExportLog() {
        try {
            String timeStamp = LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
            );

            String fileName = "report-" + timeStamp + ".txt";

            String filePath =
                    REPORTS_DIR.getAbsolutePath()
                            + File.separator
                            + fileName;

            FileUtil.writeFile(
                    filePath,
                    ConsoleOutputCapturer.getOutput()
            );

            String message =
                    "<html>"
                            + Localizer.get("settings.export_log.success")
                            + "<br>"
                            + filePath
                            + "</html>";

            JOptionPane.showMessageDialog(
                    this,
                    new JLabel(message),
                    Localizer.get("settings.success"),
                    JOptionPane.INFORMATION_MESSAGE
            );

            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().open(REPORTS_DIR);
                } catch (IOException openEx) {
                    System.err.println(
                            "Failed to open reports directory: "
                                    + openEx.getMessage()
                    );
                }
            }

        } catch (IOException ex) {
            String message =
                    "<html>"
                            + Localizer.get("settings.export_log.failed")
                            + "<br>"
                            + ex.getMessage()
                            + "</html>";

            JOptionPane.showMessageDialog(
                    this,
                    new JLabel(message),
                    Localizer.get("settings.error"),
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void handleScriptManager() {
        JPanel panel = new JPanel(new BorderLayout());

        String message = String.format(
                "<html><div style='width:%dpx;'>%s</div></html>",
                350,
                Localizer.get(
                        "settings.scripts_manager.not_available.message"
                )
        );

        panel.add(
                new JLabel(message),
                BorderLayout.CENTER
        );

        int option = JOptionPane.showConfirmDialog(
                this,
                panel,
                Localizer.get(
                        "settings.scripts_manager.not_available.title"
                ),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (option == JOptionPane.OK_OPTION) {
            try {
                Desktop.getDesktop().browse(
                        new java.net.URI(
                                "https://github.com/Samera2022/saMacros"
                        )
                );
            } catch (Exception ignored) {
            }
        }
    }

    // -------------------------------------------------------------------------
    // Save area
    // -------------------------------------------------------------------------

    private JPanel createSavePanel() {
        JButton saveSettingsBtn =
                new ModernButton(
                        Localizer.get("settings.save_settings"),
                        true
                );

        Dimension savePreferred = saveSettingsBtn.getPreferredSize();
        saveSettingsBtn.setPreferredSize(
                new Dimension(
                        Math.max(170, savePreferred.width + 30),
                        36
                )
        );

        saveSettingsBtn.addActionListener(e -> {
            if (!saveSettings()) return;
            if (ConfigManager.getBoolean("follow_system_settings")) {
                ConfigManager.set("switch_lang", SystemUtil.getSystemLang(ConfigManager.getAvailableLangs()));
                ConfigManager.set("enable_dark_mode", SystemUtil.isSystemDarkMode());
            }
            ConfigManager.saveConfig();

            Localizer.load(
                    ConfigManager.getString("switch_lang")
            );

            MAIN_FRAME.refreshMainFrameTexts();

            ComponentUtil.setMode(
                    getContentPane(),
                    ConfigManager.getBoolean("enable_dark_mode")
                            ? OtherConsts.DARK_MODE
                            : OtherConsts.LIGHT_MODE
            );

            MAIN_FRAME.applyTheme();

            dispose();
        });

        JPanel savePanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 0, 0)
        );

        savePanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        30,
                        12,
                        30
                )
        );

        savePanel.add(saveSettingsBtn);
        return savePanel;
    }

    // -------------------------------------------------------------------------
    // Tooltip logic
    // -------------------------------------------------------------------------

    private void addTooltip(
            JComponent label,
            JComponent control,
            SettingsRegistry setting,
            JComponent... otherControls
    ) {
        boolean hasEnabledTooltip =
                Localizer.hasKey(
                        "settings."
                                + setting.i18nKey
                                + ".tooltip"
                );

        boolean hasDisabledTooltip =
                Localizer.hasKey(
                        "settings."
                                + setting.i18nKey
                                + ".tooltip.disabled"
                );

        int type;

        String enabledText = null;
        if (hasEnabledTooltip) {
            enabledText =
                    Localizer.getS(
                            setting.i18nKey + ".tooltip"
                    );
        }

        String disabledText = null;

        if (hasDisabledTooltip) {
            disabledText =
                    Localizer.getS(
                            setting.i18nKey
                                    + ".tooltip.disabled"
                    );
        } else if (setting.relateTo != null) {
            disabledText = String.format(
                    Localizer.get(
                            "lang.tooltip.disabled.default"
                    ),
                    Localizer.getS(setting.relateTo)
            );
        }

        if (hasEnabledTooltip
                && (hasDisabledTooltip
                || setting.relateTo != null)) {

            type = CompMouseAdapter.BOTH;

        } else if (hasEnabledTooltip) {
            type = CompMouseAdapter.ENABLED;

        } else {
            type = CompMouseAdapter.DISABLED;
        }

        CompMouseAdapter adapter =
                new CompMouseAdapter(
                        enabledText,
                        disabledText,
                        type
                );

        label.addMouseListener(adapter);
        control.addMouseListener(adapter);

        for (JComponent other : otherControls) {
            other.addMouseListener(adapter);
        }
    }

    // -------------------------------------------------------------------------
    // Existing config synchronization logic
    // -------------------------------------------------------------------------

    private void refreshUI(boolean forceUpdateFromConfig) {
        // Populate every control first so dependencies use the current dialog state.
        if (forceUpdateFromConfig) {
            for (SettingsRegistry setting : SettingsRegistry.values()) {
                if (setting.type == OtherConsts.SECTION) continue;
                Object value;
                if (setting.type == OtherConsts.CHECK_BOX) {
                    value = ConfigManager.getBoolean(setting.i18nKey);
                } else if (setting.type == OtherConsts.COMBO_BOX && setting.size != -1) {
                    value = ConfigManager.getInt(setting.i18nKey);
                } else {
                    value = ConfigManager.getString(setting.i18nKey);
                }
                applyValueToComponent(componentMap.get(setting.i18nKey), setting, value);
            }
        }
        for (SettingsRegistry setting : SettingsRegistry.values()) {
            if (setting.type == OtherConsts.SECTION) continue;
            JComponent component = componentMap.get(setting.i18nKey);
            boolean enabled = true;
            if (setting.relateTo != null) {
                JPanel parent = (JPanel) componentMap.get(setting.relateTo);
                boolean selected = ((JCheckBox) parent.getComponent(2)).isSelected();
                enabled = "follow_system_settings".equals(setting.relateTo) ? !selected : selected;
            }
            component.setEnabled(enabled);
            setEnabledRecursively(component, enabled);
        }
    }

    private void setEnabledRecursively(
            Component component,
            boolean enabled
    ) {
        component.setEnabled(enabled);

        if (component instanceof Container) {
            for (Component child :
                    ((Container) component).getComponents()) {

                setEnabledRecursively(
                        child,
                        enabled
                );
            }
        }
    }

    private void applyValueToComponent(
            JComponent component,
            SettingsRegistry setting,
            Object value
    ) {
        switch (setting.type) {
            case OtherConsts.CHECK_BOX: {
                JCheckBox checkBox =
                        (JCheckBox) ((JPanel) component)
                                .getComponent(2);

                checkBox.setSelected((Boolean) value);
                break;
            }

            case OtherConsts.TEXT_FIELD:
            case OtherConsts.FILE_CHOOSER:
            case OtherConsts.SPECIFIC_TEXT_FIELD: {
                JTextField textField =
                        (JTextField) ((JPanel) component)
                                .getComponent(2);

                textField.setText(
                        String.valueOf(value)
                );
                break;
            }

            case OtherConsts.COMBO_BOX: {
                JComboBox<?> comboBox =
                        (JComboBox<?>) ((JPanel) component)
                                .getComponent(2);

                if (value instanceof Integer) {
                    comboBox.setSelectedIndex(
                            Math.max(0, Math.min(comboBox.getItemCount() - 1, (Integer) value - 1))
                    );
                } else {
                    comboBox.setSelectedItem(value);
                }

                break;
            }
        }
    }

    private boolean saveSettings() {
        Map<String, Object> pending = new HashMap<>();
        for (SettingsRegistry setting : SettingsRegistry.values()) {
            if (setting.type == OtherConsts.SECTION) continue;
            JPanel component = (JPanel) componentMap.get(setting.i18nKey);
            if (!component.isEnabled()) continue;
            JComponent control = (JComponent) component.getComponent(2);
            switch (setting.type) {
                case OtherConsts.CHECK_BOX:
                    pending.put(setting.i18nKey, ((JCheckBox) control).isSelected());
                    break;
                case OtherConsts.SPECIFIC_TEXT_FIELD:
                    JTextField field = (JTextField) control;
                    try {
                        if (setting == SettingsRegistry.REPEAT_TIME) {
                            int count = Integer.parseInt(field.getText());
                            if (count != -1 && count < 1) throw new NumberFormatException();
                            pending.put(setting.i18nKey, count);
                        } else {
                            double delay = Double.parseDouble(field.getText());
                            if (!Double.isFinite(delay) || delay < 0) throw new NumberFormatException();
                            pending.put(setting.i18nKey, delay);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this,
                                Localizer.getS(setting.i18nKey) + ": " + Localizer.get("settings.invalid_number"),
                                Localizer.get("settings.error"), JOptionPane.ERROR_MESSAGE);
                        field.requestFocusInWindow();
                        field.selectAll();
                        return false;
                    }
                    break;
                case OtherConsts.TEXT_FIELD:
                case OtherConsts.FILE_CHOOSER:
                    pending.put(setting.i18nKey, ((JTextField) control).getText());
                    break;
                case OtherConsts.COMBO_BOX:
                    JComboBox<?> combo = (JComboBox<?>) control;
                    pending.put(setting.i18nKey, setting.size == -1 ? combo.getSelectedItem() : combo.getSelectedIndex() + 1);
                    break;
            }
        }
        pending.forEach(ConfigManager::set);
        return true;
    }

    // -------------------------------------------------------------------------
    // Modern UI helpers
    // -------------------------------------------------------------------------

    private boolean isDarkBackground(Component component) {
        Component current = component;

        while (current != null) {
            Color color = current.getBackground();

            if (color != null) {
                double luminance =
                        0.2126 * color.getRed()
                                + 0.7152 * color.getGreen()
                                + 0.0722 * color.getBlue();

                return luminance < 128;
            }

            current = current.getParent();
        }

        return ConfigManager.getBoolean(
                "enable_dark_mode"
        );
    }

    private void installModernScrollBar(JScrollBar bar) {
        bar.setPreferredSize(new Dimension(10, 0));
        bar.setOpaque(false);

        bar.setUI(new BasicScrollBarUI() {
            @Override
            protected void paintTrack(
                    Graphics g,
                    JComponent c,
                    Rectangle trackBounds
            ) {
                // Intentionally invisible.
            }

            @Override
            protected void paintThumb(
                    Graphics g,
                    JComponent c,
                    Rectangle thumbBounds
            ) {
                if (!scrollbar.isEnabled()
                        || thumbBounds.width <= 0
                        || thumbBounds.height <= 0) {
                    return;
                }

                Graphics2D g2 =
                        (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                boolean dark =
                        isDarkBackground(c);

                g2.setColor(
                        dark
                                ? new Color(100, 103, 111)
                                : new Color(175, 179, 187)
                );

                int margin = 2;

                g2.fillRoundRect(
                        thumbBounds.x + margin,
                        thumbBounds.y + margin,
                        Math.max(
                                2,
                                thumbBounds.width
                                        - margin * 2
                        ),
                        Math.max(
                                2,
                                thumbBounds.height
                                        - margin * 2
                        ),
                        6,
                        6
                );

                g2.dispose();
            }

            @Override
            protected JButton createDecreaseButton(
                    int orientation
            ) {
                return zeroButton();
            }

            @Override
            protected JButton createIncreaseButton(
                    int orientation
            ) {
                return zeroButton();
            }

            private JButton zeroButton() {
                JButton button = new JButton();
                Dimension zero = new Dimension(0, 0);

                button.setPreferredSize(zero);
                button.setMinimumSize(zero);
                button.setMaximumSize(zero);

                return button;
            }
        });
    }

    /**
     * Lightweight card used to visually group related settings.
     */
    /**
     * Fixed-width content column.
     *
     * Do NOT cache its preferred height: expandable settings can change their
     * visibility at runtime, so the height must always come from BoxLayout.
     */
    private class ContentColumn extends JPanel {
        @Override
        public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            d.width = CONTENT_WIDTH;
            return d;
        }

        @Override
        public Dimension getMinimumSize() {
            Dimension d = super.getMinimumSize();
            d.width = CONTENT_WIDTH;
            return d;
        }

        @Override
        public Dimension getMaximumSize() {
            Dimension d = getPreferredSize();
            return new Dimension(CONTENT_WIDTH, d.height);
        }
    }

    /**
     * Lightweight card used to visually group related settings.
     *
     * Important: the old version called setPreferredSize() in the constructor
     * while the card was still empty. That permanently stored a tiny height,
     * so BoxLayout later clipped every row and the card appeared as a thin bar.
     *
     * Here the preferred height is calculated dynamically from the children.
     */
    private class SectionCard extends JPanel {
        SectionCard() {
            setLayout(
                    new BoxLayout(
                            this,
                            BoxLayout.Y_AXIS
                    )
            );

            setOpaque(false);

            setBorder(
                    BorderFactory.createEmptyBorder(
                            5,
                            14,
                            5,
                            14
                    )
            );
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            d.width = CONTENT_WIDTH;
            return d;
        }

        @Override
        public Dimension getMinimumSize() {
            Dimension d = super.getMinimumSize();
            d.width = CONTENT_WIDTH;
            return d;
        }

        @Override
        public Dimension getMaximumSize() {
            Dimension d = getPreferredSize();

            // Lock the height to the current preferred height so BoxLayout
            // cannot stretch this card vertically, while keeping the width fixed.
            return new Dimension(CONTENT_WIDTH, d.height);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            boolean dark =
                    isDarkBackground(this);

            Color background =
                    dark
                            ? new Color(48, 50, 55)
                            : new Color(250, 250, 251);

            Color border =
                    dark
                            ? new Color(67, 70, 77)
                            : new Color(225, 227, 232);

            int arc = 12;

            g2.setColor(background);
            g2.fillRoundRect(
                    0,
                    0,
                    getWidth() - 1,
                    getHeight() - 1,
                    arc,
                    arc
            );

            g2.setColor(border);
            g2.drawRoundRect(
                    0,
                    0,
                    getWidth() - 1,
                    getHeight() - 1,
                    arc,
                    arc
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }

    /**
     * Modern toggle switch that still behaves like a JCheckBox.
     * This means existing config/save logic can remain unchanged.
     */
    private class ToggleSwitch extends JCheckBox {
        ToggleSwitch() {
            setOpaque(false);
            setFocusPainted(false);
            setBorderPainted(false);

            setCursor(
                    Cursor.getPredefinedCursor(
                            Cursor.HAND_CURSOR
                    )
            );

            Dimension size = new Dimension(42, 24);

            setPreferredSize(size);
            setMinimumSize(size);
            setMaximumSize(size);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int w = 38;
            int h = 20;

            int x =
                    (getWidth() - w) / 2;
            int y =
                    (getHeight() - h) / 2;

            boolean dark =
                    isDarkBackground(this);

            Color track;

            if (!isEnabled()) {
                // 绂佺敤鐘舵€佷笅锛屽鏋滆閫変腑鍒欎娇鐢ㄨ摑鐏拌壊锛屽惁鍒欎娇鐢ㄧ函鐏拌壊
                if (isSelected()) {
                    track =
                            dark
                                    ? new Color(75, 95, 130)  // 鏆楄壊妯″紡锛氳摑鐏拌壊
                                    : new Color(140, 165, 210); // 浜壊妯″紡锛氳摑鐏拌壊
                } else {
                    track =
                            dark
                                    ? new Color(80, 82, 86)
                                    : new Color(205, 208, 214);
                }

            } else if (isSelected()) {
                track =
                        new Color(83, 123, 255);

            } else {
                track =
                        dark
                                ? new Color(94, 97, 105)
                                : new Color(190, 194, 202);
            }

            g2.setColor(track);

            g2.fillRoundRect(
                    x,
                    y,
                    w,
                    h,
                    h,
                    h
            );

            int knob = 16;

            int knobX =
                    isSelected()
                            ? x + w - knob - 2
                            : x + 2;

            g2.setColor(
                    !isEnabled()
                            ? new Color(210, 210, 210)
                            : Color.WHITE
            );

            g2.fillOval(
                    knobX,
                    y + 2,
                    knob,
                    knob
            );

            g2.dispose();
        }
    }

    /**
     * Minimal rounded button.
     */
    private class ModernButton extends JButton {
        private boolean hovered = false;
        private final boolean primary;

        ModernButton(
                String text,
                boolean primary
        ) {
            super(text);

            this.primary = primary;

            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);

            setCursor(
                    Cursor.getPredefinedCursor(
                            Cursor.HAND_CURSOR
                    )
            );

            setFont(
                    getFont().deriveFont(
                            Font.PLAIN,
                            13f
                    )
            );

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            boolean dark =
                    isDarkBackground(this);

            Color background;
            Color border;

            if (primary) {
                background =
                        hovered
                                ? new Color(98, 137, 255)
                                : new Color(80, 120, 245);

                border = background;
                setForeground(Color.WHITE);

            } else if (dark) {
                background =
                        hovered
                                ? new Color(70, 73, 80)
                                : new Color(58, 61, 67);

                border =
                        new Color(90, 94, 104);

                setForeground(
                        new Color(235, 237, 241)
                );

            } else {
                background =
                        hovered
                                ? new Color(239, 241, 244)
                                : new Color(248, 249, 250);

                border =
                        new Color(208, 212, 220);

                setForeground(
                        new Color(35, 38, 44)
                );
            }

            if (!isEnabled()) {
                background =
                        dark
                                ? new Color(53, 55, 59)
                                : new Color(240, 241, 243);

                border =
                        dark
                                ? new Color(65, 67, 72)
                                : new Color(220, 223, 228);

                setForeground(
                        dark
                                ? new Color(130, 133, 140)
                                : new Color(160, 164, 171)
                );

            } else if (getModel().isPressed()) {
                background =
                        background.darker();
            }

            g2.setColor(background);

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth() - 1,
                    getHeight() - 1,
                    9,
                    9
            );

            g2.setColor(border);

            g2.drawRoundRect(
                    0,
                    0,
                    getWidth() - 1,
                    getHeight() - 1,
                    9,
                    9
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }
}
