package io.github.samera2022.mousemacros.app.script;

import io.github.samera2022.mousemacros.app.UpdateInfo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ScriptWrapper {
    public final ScriptDescription description;
    public boolean isEnabled;
    public final ScriptPlugin source;
    public boolean versionMismatch;
    public boolean hardDependenciesMissing;
    public Set<ScriptProblem> problems = new HashSet<>();
    public Map<ScriptProblem, String[]> problemExtraInfo = new HashMap<>();
    public String disambiguatedDisplayName;

    public ScriptWrapper(ScriptPlugin s) {
        this.description = s.getDescription();
        this.source = s;
        this.isEnabled = s.isEnabled();
        String currentAppVersion = UpdateInfo.getLatestVersion();
        this.versionMismatch = !isVersionCompatible(this.description.getAvailableVersion(), currentAppVersion);
        this.hardDependenciesMissing = false;
        this.disambiguatedDisplayName = this.description.getName();

        for (ScriptIssue issue : s.getIssues()) {
            this.problems.add(issue.getProblem());
            if (issue.getArgs() != null) {
                this.problemExtraInfo.put(issue.getProblem(), issue.getArgs());
            }
        }
    }

    public boolean hasSevereProblem() {
        for (ScriptProblem p : problems) {
            if (p.isSevere()) return true;
        }
        return false;
    }

    private static boolean isVersionCompatible(String availableVersion, String currentVersion) {
        if (availableVersion == null || availableVersion.isEmpty() || "0.0.0".equals(availableVersion) || availableVersion.equals("*")) {
            return true; // No requirement or "any", assume compatible.
        }

        // 1. Handle range (A~B)
        if (availableVersion.contains("~")) {
            String[] parts = availableVersion.split("~");
            if (parts.length == 2) {
                String startVersion = parts[0].trim();
                String endVersion = parts[1].trim();
                boolean afterOrEqualStart = UpdateInfo.compareVersions(currentVersion, startVersion) >= 0;
                boolean beforeOrEqualEnd = UpdateInfo.compareVersions(currentVersion, endVersion) <= 0;
                return afterOrEqualStart && beforeOrEqualEnd;
            }
            return false; // Malformed range
        }

        // 2. Handle wildcard (*)
        if (availableVersion.contains("*")) {
            String[] availableParts = availableVersion.split("\\.");
            String[] currentParts = currentVersion.split("-", 2)[0].split("\\."); // Ignore pre-release for wildcard matching

            for (int i = 0; i < availableParts.length; i++) {
                if (availableParts[i].equals("*")) {
                    return true; // Matched all parts before the wildcard
                }
                if (i >= currentParts.length || !availableParts[i].equals(currentParts[i])) {
                    return false; // Mismatch before wildcard
                }
            }
        }

        // 3. Handle exact match
        return UpdateInfo.compareVersions(currentVersion, availableVersion) == 0;
    }
}
