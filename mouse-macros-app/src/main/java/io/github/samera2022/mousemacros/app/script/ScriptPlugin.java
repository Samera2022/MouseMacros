package io.github.samera2022.mousemacros.app.script;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScriptPlugin {
    private File file;
    private boolean loaded;
    private boolean enabled;
    private ScriptAPI apiInstance;
    private final ScriptDescription description;
    private final List<ScriptIssue> issues = new ArrayList<>();

    public ScriptPlugin(File file, ScriptDescription description) {
        this.file = file;
        this.description = description;
        this.loaded = false;
        this.enabled = !file.getName().endsWith(".disabled");
    }

    public String getName() {
        return description.getDisplayName();
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getAuthor() {
        return description.getAuthor();
    }

    public String getVersion() {
        return description.getVersion();
    }

    public String getDescriptionText() {
        return description.getDescription();
    }

    public String getRegisterName() {
        return description.getRegisterName();
    }

    public ScriptDescription getDescription() {
        return description;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public ScriptAPI getApiInstance() {
        return apiInstance;
    }

    public void setApiInstance(ScriptAPI apiInstance) {
        this.apiInstance = apiInstance;
    }

    public void addIssue(ScriptIssue issue) {
        issues.add(issue);
    }

    public List<ScriptIssue> getIssues() {
        return new ArrayList<>(issues);
    }

    public boolean hasSevereIssue() {
        return issues.stream().anyMatch(ScriptIssue::isSevere);
    }

    public boolean hasIssue() {
        return !issues.isEmpty();
    }

    public void clearIssues() {
        issues.clear();
    }
}
