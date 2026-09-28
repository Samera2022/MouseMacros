package io.github.samera2022.mousemacros.app.ui.component;

import io.github.samera2022.mousemacros.app.Localizer;

import javax.swing.*;
import java.awt.*;

/**
 * Status bar showing current recorder state, action count, and timing.
 */
public class StatusBar extends JPanel {
    private final JLabel statusLabel;
    private final JLabel actionCountLabel;
    private final JLabel timerLabel;
    
    // Status colors
    private static final Color COLOR_RECORDING = new Color(239, 68, 68);
    private static final Color COLOR_EXECUTING = new Color(34, 197, 94);
    private static final Color COLOR_IDLE = new Color(156, 163, 175);
    
    public StatusBar() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 8));
        setPreferredSize(new Dimension(0, 40));
        
        statusLabel = new JLabel();
        statusLabel.setFont(statusLabel.getFont().deriveFont(13f));
        
        actionCountLabel = new JLabel();
        actionCountLabel.setFont(actionCountLabel.getFont().deriveFont(13f));
        
        timerLabel = new JLabel();
        timerLabel.setFont(timerLabel.getFont().deriveFont(13f));
        
        add(statusLabel);
        add(createSeparator());
        add(actionCountLabel);
        add(createSeparator());
        add(timerLabel);
        
        setIdle();
    }
    
    private JLabel createSeparator() {
        JLabel sep = new JLabel("|");
        sep.setForeground(new Color(75, 85, 99));
        return sep;
    }
    
    public void setIdle() {
        statusLabel.setText("● " + Localizer.get("status.idle"));
        statusLabel.setForeground(COLOR_IDLE);
        actionCountLabel.setText(Localizer.get("status.actions") + ": 0");
        timerLabel.setText("");
    }
    
    public void setRecording(int actionCount, String time) {
        statusLabel.setText("● " + Localizer.get("status.recording"));
        statusLabel.setForeground(COLOR_RECORDING);
        actionCountLabel.setText(Localizer.get("status.actions") + ": " + actionCount);
        timerLabel.setText(time);
    }
    
    public void setStopped(int actionCount) {
        statusLabel.setText("● " + Localizer.get("status.stopped"));
        statusLabel.setForeground(COLOR_IDLE);
        actionCountLabel.setText(Localizer.get("status.actions") + ": " + actionCount);
        timerLabel.setText("");
    }
    
    public void setExecuting(int current, int total) {
        statusLabel.setText("▶ " + Localizer.get("status.executing"));
        statusLabel.setForeground(COLOR_EXECUTING);
        actionCountLabel.setText(Localizer.get("status.step") + ": " + current + " / " + total);
    }
    
    public void setPaused(int current, int total) {
        statusLabel.setText("Ⅱ " + Localizer.get("status.paused"));
        statusLabel.setForeground(new Color(251, 146, 60));
        actionCountLabel.setText(Localizer.get("status.step") + ": " + current + " / " + total);
    }
}