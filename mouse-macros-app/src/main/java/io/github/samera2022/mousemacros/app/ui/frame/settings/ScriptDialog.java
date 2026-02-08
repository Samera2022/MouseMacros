package io.github.samera2022.mousemacros.app.ui.frame.settings;

import io.github.samera2022.mousemacros.app.Localizer;
import io.github.samera2022.mousemacros.app.UpdateInfo;
import io.github.samera2022.mousemacros.app.adapter.CompMouseAdapter;
import io.github.samera2022.mousemacros.app.adapter.WindowClosingAdapter;
import io.github.samera2022.mousemacros.app.config.ConfigManager;
import io.github.samera2022.mousemacros.app.config.WhitelistManager;
import io.github.samera2022.mousemacros.app.constant.OtherConsts;
import io.github.samera2022.mousemacros.app.script.ScriptManager;
import io.github.samera2022.mousemacros.app.script.ScriptPlugin;
import io.github.samera2022.mousemacros.app.script.ScriptProblem;
import io.github.samera2022.mousemacros.app.script.ScriptWrapper;
import io.github.samera2022.mousemacros.app.util.DependencyUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ScriptDialog extends JDialog {
    private final DefaultListModel<ScriptWrapper> listModel = new DefaultListModel<>();
    private final JList<ScriptWrapper> scriptList = new JList<>(listModel);

    private final JLabel titleLabel = new JLabel();
    private final JTextPane descriptionPane = new JTextPane();
    private final JButton actionBtn = new JButton();
    private final JButton refreshBtn = new JButton();
    private final JButton openBtn = new JButton();

    private final JLabel authorValueLabel = new JLabel();
    private final JLabel typeValueLabel = new JLabel();
    private final JLabel versionValueLabel = new JLabel();
    private final JLabel mmcVersionValueLabel = new JLabel();
    private final JLabel softDependenciesValueLabel = new JLabel();
    private final JLabel hardDependenciesValueLabel = new JLabel();

    private final JPanel emptyStatePanel = new JPanel();
    private final CardLayout detailCardLayout;
    private final JPanel detailPanel;
    private static final String DETAIL_CARD = "detail";
    private static final String EMPTY_CARD = "empty";

    private ScriptWrapper currentSelectedWrapper = null;

    private CompMouseAdapter versionMismatchTitleTip; // always enabled

    private final Map<String, String[]> softDependenciesByRegistryName = new HashMap<>();
    private final Map<String, String[]> hardDependenciesByRegistryName = new HashMap<>();
    private final Map<String, ScriptWrapper> installedScripts = new HashMap<>();
    private final Map<String, String> registryToDisplayNameMap = new HashMap<>();

    public ScriptDialog() {
        setTitle(Localizer.get("settings.scripts_manager"));
        setName("settings.scripts_manager");
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource(OtherConsts.RELATIVE_PATH + "icons/MouseMacros.png"))).getImage());
        setModal(true);

        scriptList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scriptList.setCellRenderer(new ScriptListRenderer());
        scriptList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) updateDetails(scriptList.getSelectedValue());
        });

        JScrollPane listScrollPane = new JScrollPane(scriptList);
        listScrollPane.setPreferredSize(new Dimension(220, 0));
        listScrollPane.setBorder(null);

        detailCardLayout = new CardLayout();
        detailPanel = new JPanel(detailCardLayout);

        JPanel detailContentPanel = new JPanel(new BorderLayout(0, 15));
        detailContentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        JPanel metaPanel = new JPanel(new GridBagLayout());
        metaPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 0, 2, 15);

        metaPanel.add(new JLabel(Localizer.get("script.author") + ":"), gbc);
        gbc.gridx = 1;
        metaPanel.add(authorValueLabel, gbc);

        gbc.gridx = 2;
        metaPanel.add(new JLabel(Localizer.get("script.type") + ":"), gbc);
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        metaPanel.add(typeValueLabel, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.weightx = 0;
        metaPanel.add(new JLabel(Localizer.get("script.version") + ":"), gbc);
        gbc.gridx = 1;
        metaPanel.add(versionValueLabel, gbc);

        gbc.gridx = 2;
        metaPanel.add(new JLabel(Localizer.get("script.mmc_version") + ":"), gbc);
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        metaPanel.add(mmcVersionValueLabel, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.weightx = 0;
        metaPanel.add(new JLabel(Localizer.get("script.soft_dependencies") + ":"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        metaPanel.add(softDependenciesValueLabel, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.weightx = 0;
        metaPanel.add(new JLabel(Localizer.get("script.hard_dependencies") + ":"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        metaPanel.add(hardDependenciesValueLabel, gbc);


        JPanel headerPanel = new JPanel(new BorderLayout(0, 10));
        headerPanel.setOpaque(false);
        headerPanel.add(titlePanel, BorderLayout.NORTH);
        headerPanel.add(metaPanel, BorderLayout.CENTER);


        descriptionPane.setEditable(false);
        descriptionPane.setContentType("text/html");
        descriptionPane.setOpaque(false);
        descriptionPane.setBorder(null);

        JScrollPane descScrollPane = new JScrollPane(descriptionPane);
        descScrollPane.setBorder(null);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        actionPanel.setOpaque(false);
        actionPanel.add(actionBtn);

        detailContentPanel.add(headerPanel, BorderLayout.NORTH);
        detailContentPanel.add(descScrollPane, BorderLayout.CENTER);
        detailContentPanel.add(actionPanel, BorderLayout.SOUTH);

        emptyStatePanel.setLayout(new BorderLayout());
        emptyStatePanel.setOpaque(false);
        updateEmptyStatePanel();

        detailPanel.add(detailContentPanel, DETAIL_CARD);
        detailPanel.add(emptyStatePanel, EMPTY_CARD);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane, detailPanel);
        splitPane.setDividerLocation(220);
        splitPane.setDividerSize(2);
        splitPane.setBorder(null);
        splitPane.setContinuousLayout(true);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottomPanel.setOpaque(false);
        refreshBtn.setText(Localizer.get("script.action.refresh"));
        openBtn.setText(Localizer.get("script.action.open"));
        bottomPanel.add(refreshBtn);
        bottomPanel.add(openBtn);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setOpaque(false);
        southPanel.add(new JSeparator(), BorderLayout.NORTH);
        southPanel.add(bottomPanel, BorderLayout.CENTER);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(splitPane, BorderLayout.CENTER);
        contentPanel.add(southPanel, BorderLayout.SOUTH);
        setContentPane(contentPanel);

        refreshBtn.addActionListener(e -> refreshScripts());
        openBtn.addActionListener(e -> openFolderPath(ScriptManager.SCRIPT_PATH));
        actionBtn.addActionListener(e -> {
            if (currentSelectedWrapper != null) {
                toggleScriptState(currentSelectedWrapper);
            }
        });

        initScripts();
        if (!listModel.isEmpty()) {
            scriptList.setSelectedIndex(0);
        }

        
        setSize(800, 500);
        setLocationRelativeTo(null);
        addWindowListener(new WindowClosingAdapter());
    }
    private void initScripts() {
        listModel.clear();
        installedScripts.clear();
        registryToDisplayNameMap.clear();
        for (ScriptPlugin s : ScriptManager.getScripts()) {
            ScriptWrapper wrapper = new ScriptWrapper(s);
            listModel.addElement(wrapper);
        }

        Map<String, Integer> displayNameCounts = new HashMap<>();
        Map<String, Integer> registryNameCounts = new HashMap<>();
        for (int i = 0; i < listModel.size(); i++) {
            ScriptWrapper w = listModel.getElementAt(i);
            displayNameCounts.put(w.description.getName(), displayNameCounts.getOrDefault(w.description.getName(), 0) + 1);
            registryNameCounts.put(w.description.getRegisterName(), registryNameCounts.getOrDefault(w.description.getRegisterName(), 0) + 1);
        }

        for (int i = 0; i < listModel.size(); i++) {
            ScriptWrapper wrapper = listModel.getElementAt(i);
            String displayName = wrapper.description.getName();
            String registryName = wrapper.description.getRegisterName();
            
            StringBuilder finalName = new StringBuilder(displayName);
            if (displayNameCounts.getOrDefault(displayName, 1) > 1) {
                finalName.append(" (").append(registryName).append(")");
            }
            if (registryNameCounts.getOrDefault(registryName, 1) > 1) {
                finalName.append(" (").append(wrapper.source.getFile().getName()).append(")");
            }
            wrapper.disambiguatedDisplayName = finalName.toString();

            if (registryName != null) {
                installedScripts.put(DependencyUtil.normalizeRegistryName(registryName), wrapper);
                registryToDisplayNameMap.put(DependencyUtil.normalizeRegistryName(registryName), displayName);
            }
        }

        rebuildDependencyMaps();
        updateHardDependencyWarnings();
        for (int i = 0; i < listModel.size(); i++) {
            updateScriptStateVisuals(listModel.getElementAt(i));
        }
        if (listModel.isEmpty()) {
            detailCardLayout.show(detailPanel, EMPTY_CARD);
        } else {
            detailCardLayout.show(detailPanel, DETAIL_CARD);
        }
    }

    private void setLabelTextWithTooltip(JLabel label, String fullText, int maxLength) {
        if (fullText == null) {
            fullText = "";
        }
        if (fullText.length() > maxLength) {
            label.setText(fullText.substring(0, maxLength) + "...");
            label.setToolTipText(fullText);
        } else {
            label.setText(fullText);
            label.setToolTipText(null);
        }
    }

    private void updateDetails(ScriptWrapper wrapper) {
        if (wrapper == null) {
            titleLabel.setText("");
            descriptionPane.setText("");
            actionBtn.setVisible(false);
            setLabelTextWithTooltip(authorValueLabel, "", 30);
            setLabelTextWithTooltip(typeValueLabel, "", 30);
            setLabelTextWithTooltip(versionValueLabel, "", 30);
            setLabelTextWithTooltip(mmcVersionValueLabel, "", 30);
            softDependenciesValueLabel.setText("");
            softDependenciesValueLabel.setToolTipText(null);
            hardDependenciesValueLabel.setText("");
            hardDependenciesValueLabel.setToolTipText(null);
            currentSelectedWrapper = null;
            clearVersionMismatchTips();
            return;
        }

        currentSelectedWrapper = wrapper;
        actionBtn.setVisible(true);
        boolean isDarkMode = ConfigManager.getBoolean("enable_dark_mode");

        updateScriptStateVisuals(wrapper);

        String author = wrapper.description.getAuthor();
        if (WhitelistManager.isWhitelisted(wrapper.description)) {
            authorValueLabel.setText("<html><font color='green'>" + author + "</font></html>");
        } else {
            setLabelTextWithTooltip(authorValueLabel, author, 20);
        }
        String type = Localizer.get("script.type.script");
        setLabelTextWithTooltip(typeValueLabel, type, 20);
        setLabelTextWithTooltip(versionValueLabel, wrapper.description.getVersion(), 20);
        setLabelTextWithTooltip(mmcVersionValueLabel, wrapper.description.getAvailableVersion(), 20);
        String noneText = Localizer.get("script.dependencies.none");

        softDependenciesValueLabel.setText(DependencyUtil.formatDependencies(wrapper.description.getSoftDependencies(), noneText, installedScripts, registryToDisplayNameMap, false));
        hardDependenciesValueLabel.setText(DependencyUtil.formatDependencies(wrapper.description.getHardDependencies(), noneText, installedScripts, registryToDisplayNameMap, true));

        wrapper.problems.clear();
        String[] hardDependencies = wrapper.description.getHardDependencies();
        if (hardDependencies != null) {
            for (String depName : hardDependencies) {
                String normalizedDepName = DependencyUtil.normalizeRegistryName(depName);
                ScriptWrapper depWrapper = installedScripts.get(normalizedDepName);
                if (depWrapper == null || !depWrapper.isEnabled) {
                    wrapper.problems.add(ScriptProblem.H_DEP_MISSING);
                    wrapper.problemExtraInfo.put(ScriptProblem.H_DEP_MISSING, new String[]{depName});
                } else if (depWrapper.hasSevereProblem()) {
                    wrapper.problems.add(ScriptProblem.H_DEP_PROBLEM_SEVERE);
                    wrapper.problemExtraInfo.put(ScriptProblem.H_DEP_PROBLEM_SEVERE, new String[]{depName});
                } else if (!depWrapper.problems.isEmpty() || depWrapper.versionMismatch) {
                    wrapper.problems.add(ScriptProblem.H_DEP_PROBLEM_NOT_SEVERE);
                    wrapper.problemExtraInfo.put(ScriptProblem.H_DEP_PROBLEM_NOT_SEVERE, new String[]{depName});
                }
            }
        }
        updateScriptStateVisuals(wrapper);
        softDependenciesValueLabel.setToolTipText(null);
        hardDependenciesValueLabel.setToolTipText(null);

        applyVersionMismatchTips(wrapper);

        String textColor = isDarkMode ? "#BBBBBB" : "#333333";
        String descriptionText = (wrapper.description.getDescription() != null)
                ? wrapper.description.getDescription()
                : "No description available.";
        descriptionPane.setText(String.format("<html><body style='font-family:SansSerif; font-size:11pt; color:%s;'>%s</body></html>",
                textColor, descriptionText.replace("\n", "<br>")));

        actionBtn.setText(wrapper.isEnabled ? Localizer.get("script.status.disable") : Localizer.get("script.status.enable"));

        applyScriptProblemsMouseCheck(wrapper);
    }

    private void clearVersionMismatchTips() {
        if (versionMismatchTitleTip != null) {
            titleLabel.removeMouseListener(versionMismatchTitleTip);
            versionMismatchTitleTip = null;
        }
    }

    private void applyVersionMismatchTips(ScriptWrapper wrapper) {
        clearVersionMismatchTips();
        if (!wrapper.versionMismatch) {
            return;
        }
        String tipText = getVersionMismatchTip(wrapper.description.getAvailableVersion(), UpdateInfo.getLatestVersion());
        if (tipText == null || tipText.isEmpty()) {
            return;
        }
        tipText = tipText.replace("\n", " ").replace("<br>", " ");
        
        versionMismatchTitleTip = new CompMouseAdapter(tipText, null, CompMouseAdapter.ENABLED, true);
        titleLabel.addMouseListener(versionMismatchTitleTip);
    }

    private String getVersionMismatchTip(String availableVersion, String currentVersion) {
        int type = getVersionMismatchType(availableVersion, currentVersion);
        if (type == 0) {
            return null;
        }
        String key = type == -1
                ? "script.version_mismatch.earlier"
                : "script.version_mismatch.later";
        return String.format(Localizer.get(key), currentVersion, availableVersion);
    }

    private static int getVersionMismatchType(String availableVersion, String currentVersion) {
        if (availableVersion == null || availableVersion.isEmpty() || "0.0.0".equals(availableVersion) || "*".equals(availableVersion)) {
            return 0;
        }
        if (availableVersion.contains("~")) {
            String[] parts = availableVersion.split("~");
            if (parts.length == 2) {
                String startVersion = parts[0].trim();
                String endVersion = parts[1].trim();
                if (UpdateInfo.compareVersions(currentVersion, startVersion) < 0) {
                    return -1;
                }
                if (UpdateInfo.compareVersions(currentVersion, endVersion) > 0) {
                    return 1;
                }
            }
            return 0;
        }
        if (availableVersion.contains("*")) {
            String[] availableParts = availableVersion.split("\\.");
            String[] currentParts = currentVersion.split("-", 2)[0].split("\\.");
            for (int i = 0; i < availableParts.length; i++) {
                String part = availableParts[i];
                if ("*".equals(part)) {
                    break;
                }
                if (i >= currentParts.length) {
                    return -1;
                }
                try {
                    int av = Integer.parseInt(part);
                    int cv = Integer.parseInt(currentParts[i]);
                    if (cv < av) {
                        return -1;
                    }
                    if (cv > av) {
                        return 1;
                    }
                } catch (NumberFormatException ex) {
                    break;
                }
            }
            int wildcardCompare = UpdateInfo.compareVersions(currentVersion, availableVersion.replace("*", "0"));
            return Integer.compare(wildcardCompare, 0);
        }
        int compare = UpdateInfo.compareVersions(currentVersion, availableVersion);
        return Integer.compare(compare, 0);
    }

    private void updateScriptStateVisuals(ScriptWrapper wrapper) {
        boolean isDarkMode = ConfigManager.getBoolean("enable_dark_mode");
        String nameColor = isDarkMode ? "white" : "black";
        String disabledColor = "gray";
        String warningColor = "#DAA520";
        String hardMissingColor = "#FF0000";

        StringBuilder titleHtml = new StringBuilder("<html>");
        if (!wrapper.isEnabled) {
            titleHtml.append("<s>");
        }
        if (!wrapper.problems.isEmpty()) {
            String exclamColor = wrapper.hasSevereProblem() ? hardMissingColor : warningColor;
            titleHtml.append(String.format("<font color='%s'>[!] </font>", exclamColor));
        } else if (wrapper.hardDependenciesMissing) {
            titleHtml.append(String.format("<font color='%s'>[!] </font>", hardMissingColor));
        } else if (wrapper.versionMismatch) {
            titleHtml.append(String.format("<font color='%s'>[!] </font>", warningColor));
        }
        String color = wrapper.isEnabled ? nameColor : disabledColor;
        titleHtml.append(String.format("<font color='%s'>%s</font>", color, wrapper.disambiguatedDisplayName));
        if (!wrapper.isEnabled) {
            titleHtml.append("</s>");
        }
        titleHtml.append("</html>");

        titleLabel.setText(titleHtml.toString());

        int index = listModel.indexOf(wrapper);
        if (index != -1) {
            listModel.set(index, wrapper);
        }
    }

    private void toggleScriptState(ScriptWrapper wrapper) {
        ScriptPlugin s = wrapper.source;
        if (s.isEnabled()) {
            ScriptManager.disableScript(s);
        } else {
            if (wrapper.description.isRequiresNativeAccess()) {
                if (WhitelistManager.isWhitelisted(wrapper.description)) {
                    ScriptManager.enableScript(s, true);
                } else {
                    showNativeAccessWarning(wrapper);
                    return;
                }
            } else {
                ScriptManager.enableScript(s, false);
            }
        }
        wrapper.isEnabled = s.isEnabled();
        String registryName = s.getRegisterName();
        if (registryName == null || registryName.trim().isEmpty()) {
            registryName = s.getName();
        }
        updateDependencyColorsForRegistryName(registryName);

        updateHardDependencyWarnings();
        updateScriptStateVisuals(wrapper);
        actionBtn.setText(wrapper.isEnabled ? Localizer.get("script.status.disable") : Localizer.get("script.status.enable"));
    }

    private void showNativeAccessWarning(ScriptWrapper wrapper) {
        ScriptPlugin script = wrapper.source;
        String scriptName = wrapper.disambiguatedDisplayName;
        String description = wrapper.description.getNativeAccessDescription();
        String authorName = wrapper.source.getAuthor();

        String title = String.format(Localizer.get("script.native_access.title"), scriptName);
        String message = String.format(Localizer.get("script.native_access.message"), scriptName, authorName, description);

        JCheckBox whitelistAuthor = new JCheckBox(String.format(Localizer.get("script.whitelist.author"), wrapper.description.getAuthor()));
        JCheckBox whitelistScript = new JCheckBox(String.format(Localizer.get("script.whitelist.script"), wrapper.description.getDisplayName()));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(message), BorderLayout.CENTER);
        JPanel checkboxPanel = new JPanel(new GridLayout(2, 1));
        checkboxPanel.add(whitelistAuthor);
        checkboxPanel.add(whitelistScript);
        panel.add(checkboxPanel, BorderLayout.SOUTH);

        int result = JOptionPane.showConfirmDialog(
            this,
            panel,
            title,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            if (whitelistAuthor.isSelected()) {
                WhitelistManager.addAuthorToWhitelist(wrapper.description.getAuthor());
            }
            if (whitelistScript.isSelected()) {
                WhitelistManager.addScriptToWhitelist(wrapper.description.getRegisterName(), wrapper.description.getAuthor(), wrapper.description.getDisplayName());
            }

            ScriptManager.enableScript(script, true);
            wrapper.isEnabled = script.isEnabled();
            
            updateHardDependencyWarnings();
            updateScriptStateVisuals(wrapper);
            actionBtn.setText(wrapper.isEnabled ? Localizer.get("script.status.disable") : Localizer.get("script.status.enable"));
        }
    }

    private void refreshScripts() {
        Set<String> previousInstalled = DependencyUtil.buildInstalledRegistryNames();

        ScriptManager.loadAndProcessScripts();

        initScripts();
        if (!listModel.isEmpty()) {
            scriptList.setSelectedIndex(0);
        } else {
            scriptList.clearSelection();
            updateDetails(null);
        }

        updateHardDependencyWarnings();

        Set<String> currentInstalled = DependencyUtil.buildInstalledRegistryNames();
        Set<String> removed = new HashSet<>(previousInstalled);
        removed.removeAll(currentInstalled);
        Set<String> added = new HashSet<>(currentInstalled);
        added.removeAll(previousInstalled);

        for (String registryName : removed) {
            updateDependencyColorsForRegistryName(registryName);
        }
        for (String registryName : added) {
            updateDependencyColorsForRegistryName(registryName);
        }
    }

    private void openFolderPath(String folderPath) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new File(folderPath));
            }
        } catch (Exception ex) {
            System.err.println("Failed to open folder via Desktop: " + ex.getMessage());
        }
    }

    private void updateEmptyStatePanel() {
        emptyStatePanel.removeAll();

        boolean isDarkMode = ConfigManager.getBoolean("enable_dark_mode");
        Color textColor = isDarkMode ? new Color(200, 200, 200) : new Color(100, 100, 100);
        Color linkColor = isDarkMode ? new Color(100, 150, 255) : new Color(0, 51, 204);

        emptyStatePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;

        String prefix = Localizer.get("script.empty_state");
        String scriptText = Localizer.get("script.empty_state.script");

        String htmlContent = String.format(
                "<html><body style='font-family:SansSerif; font-size:%dpt; color:%s; text-align:center; line-height:1.5;'>" +
                        "%s<a href='script' style='color:%s; text-decoration:underline;'>%s</a>" +
                        "</body></html>",
                22,
                String.format("rgb(%d,%d,%d)", textColor.getRed(), textColor.getGreen(), textColor.getBlue()),
                prefix,
                String.format("rgb(%d,%d,%d)", linkColor.getRed(), linkColor.getGreen(), linkColor.getBlue()),
                scriptText
        );

        JEditorPane editorPane = createHyperlinkEditorPane(htmlContent);

        emptyStatePanel.add(editorPane, gbc);
    }

    private JEditorPane createHyperlinkEditorPane(String htmlContent) {
        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setText(htmlContent);
        editorPane.setEditable(false);
        editorPane.setOpaque(false);
        editorPane.setBorder(null);

        editorPane.addHyperlinkListener(e -> {
            if (e.getEventType() == javax.swing.event.HyperlinkEvent.EventType.ACTIVATED) {
                openFolderPath(ScriptManager.SCRIPT_PATH);
            }
        });
        return editorPane;
    }

    private static class ScriptListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            ScriptWrapper wrapper = (ScriptWrapper) value;
            StringBuilder html = new StringBuilder("<html>  ");
            if (!wrapper.isEnabled) {
                html.append("<s>");
            }
            if (!wrapper.problems.isEmpty()) {
                String exclamColor = wrapper.hasSevereProblem() ? "#FF0000" : "#DAA520";
                html.append(String.format("<font color='%s'>[!] </font>", exclamColor));
            } else if (wrapper.hardDependenciesMissing) {
                html.append("<font color='#FF0000'>[!] </font>");
            } else if (wrapper.versionMismatch) {
                html.append("<font color='#DAA520'>[!] </font>");
            }
            html.append(wrapper.disambiguatedDisplayName);
            if (!wrapper.isEnabled) {
                html.append("</s>");
            }
            html.append("  </html>");
            setText(html.toString());
            setBorder(new EmptyBorder(10, 5, 10, 5));
            if (!wrapper.isEnabled) {
                setForeground(Color.GRAY);
            }
            Font font = getFont();
            if (isSelected) {
                setFont(font.deriveFont(font.getStyle() | Font.BOLD));
            } else {
                setFont(font.deriveFont(font.getStyle() & ~Font.BOLD));
            }
            return this;
        }
    }

    private void rebuildDependencyMaps() {
        softDependenciesByRegistryName.clear();
        hardDependenciesByRegistryName.clear();
        for (ScriptPlugin script : ScriptManager.getScripts()) {
            String registryName = DependencyUtil.normalizeRegistryName(script.getRegisterName());
            if (registryName == null) {
                registryName = DependencyUtil.normalizeRegistryName(script.getName());
            }
            if (registryName == null) {
                continue;
            }
            softDependenciesByRegistryName.put(registryName, script.getDescription().getSoftDependencies());
            hardDependenciesByRegistryName.put(registryName, script.getDescription().getHardDependencies());
        }
    }

    private void updateDependencyColorsForRegistryName(String registryName) {
        if (currentSelectedWrapper == null) {
            return;
        }
        String normalizedRegistryName = DependencyUtil.normalizeRegistryName(registryName);
        if (normalizedRegistryName == null) {
            return;
        }
        String selectedRegistryName = DependencyUtil.normalizeRegistryName(currentSelectedWrapper.description != null
                ? currentSelectedWrapper.description.getRegisterName()
                : currentSelectedWrapper.source.getName());
        if (selectedRegistryName == null) {
            return;
        }
        String[] softDependencies = softDependenciesByRegistryName.get(selectedRegistryName);
        String[] hardDependencies = hardDependenciesByRegistryName.get(selectedRegistryName);
        if (!DependencyUtil.containsDependency(softDependencies, normalizedRegistryName) && !DependencyUtil.containsDependency(hardDependencies, normalizedRegistryName)) {
            return;
        }
        String noneText = Localizer.get("script.dependencies.none");
        softDependenciesValueLabel.setText(DependencyUtil.formatDependencies(softDependencies, noneText, installedScripts, registryToDisplayNameMap, false));
        hardDependenciesValueLabel.setText(DependencyUtil.formatDependencies(hardDependencies, noneText, installedScripts, registryToDisplayNameMap, true));
        currentSelectedWrapper.hardDependenciesMissing = hasMissingHardDependencies(hardDependencies);
        updateScriptStateVisuals(currentSelectedWrapper);
        softDependenciesValueLabel.setToolTipText(null);
        hardDependenciesValueLabel.setToolTipText(null);
    }

    private void updateHardDependencyWarnings() {
        for (int i = 0; i < listModel.size(); i++) {
            ScriptWrapper wrapper = listModel.getElementAt(i);
            wrapper.problems.clear();
            String[] hardDependencies = wrapper.description.getHardDependencies();
            if (hardDependencies != null) {
                for (String depName : hardDependencies) {
                    String normalizedDepName = DependencyUtil.normalizeRegistryName(depName);
                    ScriptWrapper depWrapper = installedScripts.get(normalizedDepName);
                    if (depWrapper == null || !depWrapper.isEnabled) {
                        wrapper.problems.add(ScriptProblem.H_DEP_MISSING);
                        wrapper.problemExtraInfo.put(ScriptProblem.H_DEP_MISSING, new String[]{depName});
                    } else if (depWrapper.hasSevereProblem()) {
                        wrapper.problems.add(ScriptProblem.H_DEP_PROBLEM_SEVERE);
                        wrapper.problemExtraInfo.put(ScriptProblem.H_DEP_PROBLEM_SEVERE, new String[]{depName});
                    } else if (!depWrapper.problems.isEmpty() || depWrapper.versionMismatch) {
                        wrapper.problems.add(ScriptProblem.H_DEP_PROBLEM_NOT_SEVERE);
                        wrapper.problemExtraInfo.put(ScriptProblem.H_DEP_PROBLEM_NOT_SEVERE, new String[]{depName});
                    }
                }
            }
            updateScriptStateVisuals(wrapper);
            listModel.set(i, wrapper);
        }
        if (currentSelectedWrapper != null) {
            updateScriptStateVisuals(currentSelectedWrapper);
            applyScriptProblemsMouseCheck(currentSelectedWrapper);
        }
    }

    private void applyScriptProblemsMouseCheck(ScriptWrapper wrapper) {
        removeAllMouseCheckAdapter(titleLabel);
        removeAllMouseCheckAdapter(mmcVersionValueLabel);
        if (wrapper == null) return;
        boolean hasProblem = !wrapper.problems.isEmpty();
        boolean hasVersionMismatch = wrapper.versionMismatch;
        if (!hasProblem && !hasVersionMismatch) return;
        StringBuilder html = new StringBuilder();
        for (ScriptProblem problem : ScriptProblem.values()) {
            if (wrapper.problems.contains(problem)) {
                String color = problem.isSevere() ? "#FF0000" : "#DAA520";
                html.append(String.format("<font color='%s'>[!]</font> ", color));
                String msg = Localizer.get(problem.getKey());
                
                String[] extra = getProblemFormattedExtra(wrapper, problem);
                if (extra != null) {
                    msg = String.format(msg, (Object[]) extra);
                }
                
                msg = msg.replace("\n", " ").replace("<br>", " ");
                html.append(msg).append("<br>");
            }
        }
        if (hasVersionMismatch) {
            String color = "#DAA520";
            html.append(String.format("<font color='%s'>[!]</font> ", color));
            int type = getVersionMismatchType(wrapper.description.getAvailableVersion(), UpdateInfo.getLatestVersion());
            String key = type == -1 ? "script.version_mismatch.earlier" : "script.version_mismatch.later";
            String msg = Localizer.get(key);
            
            ScriptProblem mismatchProblem = (type == -1) ? ScriptProblem.MMC_VERSION_NOT_COMPATIBLE_EARLIER : ScriptProblem.MMC_VERSION_NOT_COMPATIBLE_LATER;
            String[] extra = getProblemFormattedExtra(wrapper, mismatchProblem);
            if (extra != null) {
                msg = String.format(msg, (Object[]) extra);
            }
            
            msg = msg.replace("\n", " ").replace("<br>", " ");
            html.append(msg).append("<br>");
        }
        
        String finalHtml = html.toString();
        if (finalHtml.endsWith("<br>")) {
            finalHtml = finalHtml.substring(0, finalHtml.length() - 4);
        }
        
        CompMouseAdapter tip = new CompMouseAdapter(finalHtml, null, CompMouseAdapter.ENABLED, true);
        titleLabel.addMouseListener(tip);
    }

    private void removeAllMouseCheckAdapter(JComponent comp) {
        for (java.awt.event.MouseListener l : comp.getMouseListeners()) {
            if (l instanceof CompMouseAdapter) comp.removeMouseListener(l);
        }
    }

    private String[] getProblemFormattedExtra(ScriptWrapper wrapper, ScriptProblem problem) {
        if (!problem.isNeedExtraInfo()) {
            return null;
        }

        String[] extra = wrapper.problemExtraInfo.get(problem);

        if (extra == null) {
            String[] infoKeys = problem.getExtraInfoKeys();
            if (infoKeys == null || infoKeys.length == 0) {
                return null;
            }
            extra = new String[infoKeys.length];
            for (int i = 0; i < infoKeys.length; i++) {
                if ("currentVersion".equals(infoKeys[i])) {
                    extra[i] = UpdateInfo.getLatestVersion();
                } else if ("availableVersion".equals(infoKeys[i])) {
                    extra[i] = wrapper.description.getAvailableVersion();
                } else if ("depName".equals(infoKeys[i])) {
                    extra[i] = "";
                } else if ("depVersion".equals(infoKeys[i])) {
                    extra[i] = "";
                } else {
                    extra[i] = "";
                }
            }
        }
        return extra;
    }

    private boolean hasMissingHardDependencies(String[] dependencies) {
        if (dependencies == null) {
            return false;
        }
        for (String dependency : dependencies) {
            if (dependency == null || dependency.trim().isEmpty()) {
                continue;
            }
            if (!DependencyUtil.isInstalledRegistry(installedScripts.keySet(), dependency.trim())) {
                return true;
            }
        }
        return false;
    }
}
