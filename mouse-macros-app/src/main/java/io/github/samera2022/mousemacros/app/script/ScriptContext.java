package io.github.samera2022.mousemacros.app.script;

import io.github.samera2022.mousemacros.api.action.IMouseAction;
import io.github.samera2022.mousemacros.api.config.IConfig;
import io.github.samera2022.mousemacros.api.script.IScriptContext;
import io.github.samera2022.mousemacros.app.config.ConfigManager;

import java.awt.Color;
import java.io.File;
import java.util.Map;

/**
 * A container that associates all aspects of a script: its source file, description, and instance.
 * It also implements the IScriptContext interface to expose safe methods to the script API.
 */
public class ScriptContext implements IScriptContext {
    private File file;
    private final ScriptDescription description;
    private boolean enabled;

    public ScriptContext(File file, ScriptDescription description) {
        this.file = file;
        this.description = description;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public ScriptDescription getDescription() {
        return description;
    }

    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDisplayName() {
        return description.getName();
    }

    // Implementation of IScriptContext methods

    @Override
    public void simulate(IMouseAction action) {
        // TODO: Implement the delegation to the appropriate controller, e.g., MouseController
        System.out.println("Simulating action: " + action.getClass().getSimpleName());
    }

    @Override
    public Color getPixelColor(int x, int y) {
        // TODO: Implement the delegation to the appropriate controller, e.g., MouseController
        System.err.println("getPixelColor is not implemented yet.");
        return null;
    }

    @Override
    public void showToast(String title, String msg) {
        // TODO: Implement the delegation to the appropriate UI component, e.g., Notifier
        System.err.println("showToast is not implemented yet. Title: " + title + ", Msg: " + msg);
    }

    @Override
    public IConfig getAppConfig() {
        return new IConfig() {
            @Override
            public boolean getBoolean(String key) {
                return ConfigManager.getBoolean(key);
            }

            @Override
            public int getInt(String key) {
                return ConfigManager.getInt(key);
            }

            @Override
            public double getDouble(String key) {
                return ConfigManager.getDouble(key);
            }

            @Override
            public String getString(String key) {
                return ConfigManager.getString(key);
            }

            @Override
            public Map<String, String> getKeyMap() {
                return ConfigManager.getKeyMap();
            }
        };
    }
}
