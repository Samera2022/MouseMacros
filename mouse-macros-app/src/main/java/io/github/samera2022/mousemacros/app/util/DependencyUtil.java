package io.github.samera2022.mousemacros.app.util;

import io.github.samera2022.mousemacros.app.script.ScriptManager;
import io.github.samera2022.mousemacros.app.script.ScriptPlugin;
import io.github.samera2022.mousemacros.app.script.ScriptWrapper;

import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class DependencyUtil {
    public static String formatDependencies(String[] dependencies, String noneText, Map<String, ScriptWrapper> installedPlugins, Map<String, String> registryToDisplayNameMap, boolean isHard) {
        if (dependencies == null || dependencies.length == 0) return noneText;
        StringBuilder builder = new StringBuilder("<html>");
        boolean first = true;
        for (String dependency : dependencies) {
            if (dependency == null || dependency.trim().isEmpty()) continue;
            if (!first) builder.append("<br>");
            String trimmed = dependency.trim();
            String normalized = normalizeRegistryName(trimmed);
            
            String displayName = registryToDisplayNameMap != null ? registryToDisplayNameMap.getOrDefault(normalized, trimmed) : trimmed;

            ScriptWrapper installedWrapper = installedPlugins.get(normalized);
            boolean installed = installedWrapper != null && installedWrapper.isEnabled;
            
            if (isHard) {
                if (installed) {
                    if (installedWrapper.hasSevereProblem()) {
                        builder.append("<font color='#FF0000'>").append(escapeHtml(displayName)).append("</font>");
                    } else if (!installedWrapper.problems.isEmpty() || installedWrapper.versionMismatch) {
                        builder.append("<font color='#DAA520'>").append(escapeHtml(displayName)).append("</font>");
                    } else {
                        builder.append(escapeHtml(displayName));
                    }
                } else {
                    builder.append("<font color='#FF0000'>").append(escapeHtml(displayName)).append("</font>");
                }
            } else {
                String color;
                if (installed) {
                    color = (installedWrapper.problems.isEmpty() && !installedWrapper.versionMismatch) ? "#2E8B57" : "#DAA520";
                } else {
                    color = "#DAA520";
                }
                builder.append("<font color='").append(color).append("'>").append(escapeHtml(displayName)).append("</font>");
            }
            first = false;
        }
        builder.append("</html>");
        return first ? noneText : builder.toString();
    }
    public static boolean isInstalledRegistry(Set<String> installedRegistryNames, String registryName) {
        if (installedRegistryNames == null || registryName == null) return false;
        String normalized = normalizeRegistryName(registryName);
        return normalized != null && installedRegistryNames.contains(normalized);
    }
    public static String normalizeRegistryName(String name) {
        if (name == null) return null;
        String trimmed = name.trim();
        if (trimmed.isEmpty()) return null;
        return trimmed.toLowerCase(Locale.ROOT);
    }
    public static String escapeHtml(String input) {
        return input.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
    public static Set<String> buildInstalledRegistryNames() {
        Set<String> result = new HashSet<>();
        for (ScriptPlugin script : ScriptManager.getScripts()) {
            if (!script.isEnabled()) continue;
            String registryName = script.getRegisterName();
            if (registryName == null || registryName.trim().isEmpty()) {
                registryName = script.getName();
            }
            addRegistryName(result, registryName);
        }
        return result;
    }
    public static void addRegistryName(Set<String> target, String name) {
        String normalized = normalizeRegistryName(name);
        if (normalized != null) target.add(normalized);
    }
    public static boolean containsDependency(String[] dependencies, String registryName) {
        if (dependencies == null || registryName == null) return false;
        String normalizedTarget = normalizeRegistryName(registryName);
        if (normalizedTarget == null) return false;
        for (String dependency : dependencies) {
            String normalizedDependency = normalizeRegistryName(dependency);
            if (normalizedTarget.equals(normalizedDependency)) return true;
        }
        return false;
    }
}
