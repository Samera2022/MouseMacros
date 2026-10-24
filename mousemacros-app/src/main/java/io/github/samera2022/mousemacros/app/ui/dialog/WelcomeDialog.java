package io.github.samera2022.mousemacros.app.ui.dialog;

import io.github.samera2022.mousemacros.app.Localizer;
import io.github.samera2022.mousemacros.app.config.ConfigManager;
import io.github.samera2022.mousemacros.app.constant.OtherConsts;
import io.github.samera2022.mousemacros.app.util.ComponentUtil;
import io.github.samera2022.mousemacros.app.util.OtherUtil;
import io.github.samera2022.mousemacros.app.ui.frame.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * Welcome wizard shown on first launch to help new users get started.
 */
public class WelcomeDialog extends JDialog {
    
    private int currentPage = 0;
    private final int totalPages = 4;
    
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JButton nextButton;
    private JButton prevButton;
    private JButton finishButton;
    private JLabel pageIndicator;
    private JCheckBox dontShowAgain;
    
    public WelcomeDialog(Frame parent) {
        super(parent, Localizer.get("welcome.title"), true);
        
        try {
            setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource(
                OtherConsts.RELATIVE_PATH + "icons/MouseMacros.png"))).getImage());
        } catch (Exception e) {
            // Icon loading failed, continue without icon
        }
        
        initComponents();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(680, 580);
        setMinimumSize(new Dimension(560, 420));
        setLocationRelativeTo(parent);
        setResizable(true);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        JLabel titleLabel = new JLabel(Localizer.get("welcome.header"));
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 24f));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Card panel for wizard pages
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        cardPanel.add(scrollPage(createPage1()), "page1");
        cardPanel.add(scrollPage(createPage2()), "page2");
        cardPanel.add(scrollPage(createPage3()), "page3");
        cardPanel.add(scrollPage(createPage4()), "page4");
        
        add(cardPanel, BorderLayout.CENTER);
        
        // Navigation buttons
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        
        prevButton = new JButton(Localizer.get("welcome.previous"));
        prevButton.setEnabled(false);
        prevButton.addActionListener(e -> previousPage());
        
        nextButton = new JButton(Localizer.get("welcome.next"));
        nextButton.addActionListener(e -> nextPage());
        
        finishButton = new JButton(Localizer.get("welcome.finish"));
        finishButton.setVisible(false);
        finishButton.addActionListener(e -> finish());
        
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(finishButton);
        
        pageIndicator = new JLabel(getPageText());
        
        navPanel.add(pageIndicator, BorderLayout.WEST);
        navPanel.add(buttonPanel, BorderLayout.EAST);
        
        add(navPanel, BorderLayout.SOUTH);
        
        // Apply theme
        ComponentUtil.setMode(getContentPane(), 
            ConfigManager.getBoolean("enable_dark_mode") ? OtherConsts.DARK_MODE : OtherConsts.LIGHT_MODE);
    }
    
    private JScrollPane scrollPage(JPanel panel) {
        JScrollPane scroll = new JScrollPane(panel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private JPanel createPage1() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        
        JTextArea text = new JTextArea(Localizer.get("welcome.page1.content"));
        text.setWrapStyleWord(true);
        text.setLineWrap(true);
        text.setEditable(false);
        text.setOpaque(false);
        text.setFont(text.getFont().deriveFont(14f));
        
        panel.add(text, BorderLayout.CENTER);

        // Add tooltip usage tip
        JTextArea tipText = new JTextArea(Localizer.get("welcome.tooltip_tip"));
        tipText.setWrapStyleWord(true);
        tipText.setLineWrap(true);
        tipText.setEditable(false);
        tipText.setOpaque(false);
        tipText.setFont(tipText.getFont().deriveFont(Font.ITALIC, 12f));
        tipText.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        
        panel.add(tipText, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createPage2() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        
        JLabel title = new JLabel(Localizer.get("welcome.page2.title"));
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        
        JTextArea text = new JTextArea(String.format(Localizer.get("welcome.page2.content"),
                OtherUtil.getNativeKeyDisplayText(MainFrame.keyRecord),
                OtherUtil.getNativeKeyDisplayText(MainFrame.keyStop),
                OtherUtil.getNativeKeyDisplayText(MainFrame.keyPlay),
                OtherUtil.getNativeKeyDisplayText(MainFrame.keyAbort)));
        text.setWrapStyleWord(true);
        text.setLineWrap(true);
        text.setEditable(false);
        text.setOpaque(false);
        text.setFont(text.getFont().deriveFont(14f));
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(text, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createPage3() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        
        JLabel title = new JLabel(Localizer.get("welcome.page3.title"));
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        
        JTextArea text = new JTextArea(Localizer.get("welcome.page3.content"));
        text.setWrapStyleWord(true);
        text.setLineWrap(true);
        text.setEditable(false);
        text.setOpaque(false);
        text.setFont(text.getFont().deriveFont(14f));
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(text, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createPage4() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        
        JLabel title = new JLabel(Localizer.get("welcome.page4.title"));
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        
        JTextArea text = new JTextArea(Localizer.get("welcome.page4.content"));
        text.setWrapStyleWord(true);
        text.setLineWrap(true);
        text.setEditable(false);
        text.setOpaque(false);
        text.setFont(text.getFont().deriveFont(14f));
        
        dontShowAgain = new JCheckBox(Localizer.get("welcome.dont_show_again"), true);
        dontShowAgain.setName("welcome.dont_show_again");
        
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 10));
        bottomPanel.add(text, BorderLayout.CENTER);
        bottomPanel.add(dontShowAgain, BorderLayout.SOUTH);
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(bottomPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void nextPage() {
        if (currentPage < totalPages - 1) {
            currentPage++;
            updateNavigation();
        }
    }
    
    private void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            updateNavigation();
        }
    }
    
    private void updateNavigation() {
        cardLayout.show(cardPanel, "page" + (currentPage + 1));
        pageIndicator.setText(getPageText());
        
        prevButton.setEnabled(currentPage > 0);
        
        if (currentPage == totalPages - 1) {
            nextButton.setVisible(false);
            finishButton.setVisible(true);
        } else {
            nextButton.setVisible(true);
            finishButton.setVisible(false);
        }
    }
    
    private String getPageText() {
        return String.format("%s %d / %d", 
            Localizer.get("welcome.page"), currentPage + 1, totalPages);
    }
    
    private void finish() {
        ConfigManager.set("welcome_shown", dontShowAgain.isSelected());
        ConfigManager.saveConfig();
        dispose();
    }
    
    /**
     * Check if welcome dialog should be shown.
     * @return true if this is first launch
     */
    public static boolean shouldShow() {
        return !ConfigManager.getBoolean("welcome_shown");
    }
}