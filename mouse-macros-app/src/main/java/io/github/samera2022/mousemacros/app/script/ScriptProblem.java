package io.github.samera2022.mousemacros.app.script;

import java.util.ArrayList;
import java.util.List;

public final class ScriptProblem {
    private static final List<ScriptProblem> VALUES = new ArrayList<>();

    public static final ScriptProblem MMC_VERSION_NOT_COMPATIBLE_EARLIER =
            new ScriptProblem("script.tooltip.mmc_vnc.earlier", false, new String[]{"currentVersion", "availableVersion"});
    public static final ScriptProblem MMC_VERSION_NOT_COMPATIBLE_LATER =
            new ScriptProblem("script.tooltip.mmc_vnc.later", false, new String[]{"currentVersion", "availableVersion"});
    public static final ScriptProblem H_DEP_VER_NOT_COMPATIBLE_EARLIER =
            new ScriptProblem("script.tooltip.hd_vnc.earlier", false, new String[]{"depName", "depVersion"});
    public static final ScriptProblem H_DEP_VER_NOT_COMPATIBLE_LATER =
            new ScriptProblem("script.tooltip.hd_vnc.later", false, new String[]{"depName", "depVersion"});
    public static final ScriptProblem H_DEP_MISSING =
            new ScriptProblem("script.tooltip.hd_missing", true, new String[]{"depName"});
    public static final ScriptProblem H_DEP_PROBLEM_SEVERE =
            new ScriptProblem("script.tooltip.hd_p.s", true, new String[]{"depName"});
    public static final ScriptProblem H_DEP_PROBLEM_NOT_SEVERE =
            new ScriptProblem("script.tooltip.hd_p.ns", false, new String[]{"depName"});
    public static final ScriptProblem REQUEST_EXTRA_PERMISSION =
            new ScriptProblem("script.tooltip.request", true, new String[]{"depName", "illustration"});
    public static final ScriptProblem REQUIRE_NATIVE_ACCESS_CONFIRMATION =
            new ScriptProblem("script.tooltip.req_native", false, null);

    private final String key;
    private final boolean severe;
    private final String[] extraInfoKeys;

    private ScriptProblem(String key, boolean severe, String[] extraInfoKeys) {
        this.key = key;
        this.severe = severe;
        this.extraInfoKeys = extraInfoKeys;
        VALUES.add(this);
    }

    public static ScriptProblem[] values() {
        return VALUES.toArray(new ScriptProblem[0]);
    }

    public String getKey() { return key; }
    public boolean isSevere() { return severe; }
    public String[] getExtraInfoKeys() { return extraInfoKeys == null ? null : extraInfoKeys.clone(); }
    public boolean isNeedExtraInfo() { return extraInfoKeys != null; }
}
