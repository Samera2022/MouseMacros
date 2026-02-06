package io.github.samera2022.mouse_macros;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

public class UpdateInfo {
    private static final List<UpdateInfo> VALUES = new ArrayList<>();

    static {
        try {
            InputStreamReader reader = new InputStreamReader(
                    Objects.requireNonNull(UpdateInfo.class.getResourceAsStream("/updates.json")), StandardCharsets.UTF_8);
            java.lang.reflect.Type type = new TypeToken<Map<String, Map<String, String>>>(){}.getType();
            Map<String, Map<String, String>> dataMap = new Gson().fromJson(reader, type);
            reader.close();
            List<UpdateInfo> list = new ArrayList<>();
            if (dataMap != null) {
                for (Map.Entry<String, Map<String, String>> entry : dataMap.entrySet()) {
                    Map<String, String> valueMap = entry.getValue();
                    String releaseDate = valueMap.getOrDefault("releaseDate", "");
                    String description = valueMap.getOrDefault("description", "");
                    list.add(new UpdateInfo(entry.getKey(), releaseDate, description));
                }
            }
            list.sort((a, b) -> compareVersions(a.version, b.version));
            VALUES.addAll(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private final String version;
    private final String releaseDate;
    private final String description;

    // Constructor
    public UpdateInfo(String version, String releaseDate, String description) {
        this.version = version;
        this.releaseDate = releaseDate;
        this.description = description;
    }

    public String getVersion() {return version;}
    public String getReleaseDate() {return releaseDate;}
    public String getDisplayName() {return String.format("[%s] %s",releaseDate,version);}
    public String getDescription() {return description;}

    // Custom method: get formatted log
    public String getFormattedLog() {
        return String.format("[%s] %s\n\n%s", releaseDate, version, description);
    }

    // Static method to mimic Enum.values()
    public static UpdateInfo[] values() {
        return VALUES.toArray(new UpdateInfo[0]);
    }

    // Search by version
    public static UpdateInfo findByVersion(String version) {
        return Stream.of(values())
                .filter(log -> log.version.equals(version))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid version: " + version));
    }

    // Get all versions as String array
    public static String[] getAllVersions() {
        return Stream.of(values()).map(log -> log.version).toArray(String[]::new);
    }

    public static String[] getAllDisplayNames() {
        return Stream.of(values()).map(UpdateInfo::getDisplayName).toArray(String[]::new);
    }

    // Latest version (last entry in the sorted list)
    public static String getLatestVersion() {
        UpdateInfo[] all = values();
        if (all.length == 0) return "0.0.0";
        return all[all.length - 1].getVersion();
    }

    /**
     * Compare two semantic-like version strings with optional pre-release suffix (dash).
     * Returns positive if a > b, negative if a < b, 0 if equal.
     * Pre-release is considered lower than the release (so "1.2.0" > "1.2.0-rc1").
     */
    public static int compareVersions(String a, String b) {
        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;
        try {
            String[] pa = a.split("-", 2);
            String[] pb = b.split("-", 2);
            String coreA = pa[0];
            String coreB = pb[0];
            String preA = pa.length > 1 ? pa[1] : null;
            String preB = pb.length > 1 ? pb[1] : null;

            String[] ca = coreA.split("\\.");
            String[] cb = coreB.split("\\.");
            for (int i = 0; i < Math.max(ca.length, cb.length); i++) {
                int va = i < ca.length ? Integer.parseInt(ca[i]) : 0;
                int vb = i < cb.length ? Integer.parseInt(cb[i]) : 0;
                if (va != vb) return Integer.compare(va, vb);
            }
            // cores equal; handle pre-release: absence of pre-release means greater (release > prerelease)
            if (preA == null && preB == null) return 0;
            if (preA == null) return 1;
            if (preB == null) return -1;
            return preA.compareTo(preB);
        } catch (Exception ex) {
            // fallback to string compare
            return a.compareTo(b);
        }
    }

    public static boolean isNewer(String a, String b) {
        return compareVersions(a, b) > 0;
    }

    public static void main(String[] args) {
        System.out.println("Latest version: " + getLatestVersion());
    }
}