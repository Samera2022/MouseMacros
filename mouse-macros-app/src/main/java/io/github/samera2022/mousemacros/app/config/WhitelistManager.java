package io.github.samera2022.mousemacros.app.config;

import com.google.gson.annotations.SerializedName;
import io.github.samera2022.mousemacros.app.script.ScriptDescription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WhitelistManager {

    @SerializedName("authors")
    public static List<String> authors = new ArrayList<>();

    @SerializedName("scripts")
    public static Map<String, List<ScriptInfo>> scripts = new HashMap<>();

    public static class ScriptInfo {
        @SerializedName("author")
        private String author;

        @SerializedName("display_name")
        private String displayName;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }
    }

    /**
     * Checks if a given script is whitelisted.
     *
     * @param description The ScriptDescription of the script to check.
     * @return true if the script is whitelisted, false otherwise.
     */
    public static boolean isWhitelisted(ScriptDescription description) {
        if (authors.contains(description.getAuthor())) {
            return true;
        }

        List<ScriptInfo> scriptInfos = scripts.get(description.getRegisterName());
        if (scriptInfos != null) {
            for (ScriptInfo scriptInfo : scriptInfos) {
                if (scriptInfo.getAuthor().equals(description.getAuthor()) && scriptInfo.getDisplayName().equals(description.getDisplayName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Adds an author to the whitelist and saves the changes.
     *
     * @param author The author's name to add.
     */
    public static void addAuthorToWhitelist(String author) {
        if (!authors.contains(author)) {
            authors.add(author);
            ConfigManager.saveWhitelist();
        }
    }

    /**
     * Adds a script to the whitelist and saves the changes.
     *
     * @param registryName The registry name of the script.
     * @param author The author of the script.
     * @param displayName The display name of the script.
     */
    public static void addScriptToWhitelist(String registryName, String author, String displayName) {
        List<ScriptInfo> scriptInfos = scripts.computeIfAbsent(registryName, k -> new ArrayList<>());

        boolean alreadyExists = scriptInfos.stream()
                .anyMatch(info -> info.getAuthor().equals(author) && info.getDisplayName().equals(displayName));

        if (!alreadyExists) {
            ScriptInfo newScriptInfo = new ScriptInfo();
            newScriptInfo.setAuthor(author);
            newScriptInfo.setDisplayName(displayName);
            scriptInfos.add(newScriptInfo);
            ConfigManager.saveWhitelist();
        }
    }
}
