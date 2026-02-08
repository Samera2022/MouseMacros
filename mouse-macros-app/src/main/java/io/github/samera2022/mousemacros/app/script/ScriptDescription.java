package io.github.samera2022.mousemacros.app.script;

public class ScriptDescription {
    private String name;
    private String version;
    private String author;
    private String description;
    private String displayName;
    private String registerName;
    private String availableVersion;
    private String[] softDependencies = new String[0];
    private String[] hardDependencies = new String[0];
    private boolean requiresNativeAccess;
    private String nativeAccessDescription;

    // Default constructor for Gson
    public ScriptDescription() {}

    // Constructor for scripts and general use
    public ScriptDescription(String name, String version, String author, String description, String displayName, String registerName, String availableVersion) {
        this(name, version, author, description, displayName, registerName, availableVersion, new String[0], new String[0], false, null);
    }

    public ScriptDescription(String name, String version, String author, String description, String displayName, String registerName, String availableVersion, String[] softDependencies, String[] hardDependencies) {
        this(name, version, author, description, displayName, registerName, availableVersion, softDependencies, hardDependencies, false, null);
    }

    public ScriptDescription(String name, String version, String author, String description, String displayName, String registerName, String availableVersion, String[] softDependencies, String[] hardDependencies, boolean requiresNativeAccess, String nativeAccessDescription) {
        this.name = name;
        this.version = version;
        this.author = author;
        this.description = description;
        this.displayName = displayName;
        this.registerName = registerName;
        this.availableVersion = availableVersion;
        this.softDependencies = softDependencies != null ? softDependencies : new String[0];
        this.hardDependencies = hardDependencies != null ? hardDependencies : new String[0];
        this.requiresNativeAccess = requiresNativeAccess;
        this.nativeAccessDescription = nativeAccessDescription;
    }

    public String getName() {
        return (displayName != null && !displayName.trim().isEmpty()) ? displayName : name;
    }

    public String getVersion() {
        return version;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getRegisterName() {
        return registerName;
    }

    public String getAvailableVersion() {
        return availableVersion;
    }

    public String[] getSoftDependencies() {
        return softDependencies;
    }

    public String[] getHardDependencies() {
        return hardDependencies;
    }

    public boolean isRequiresNativeAccess() {
        return requiresNativeAccess;
    }

    public String getNativeAccessDescription() {
        return nativeAccessDescription == null || nativeAccessDescription.trim().isEmpty()
                ? "作者未提供额外说明。"
                : nativeAccessDescription;
    }
}
