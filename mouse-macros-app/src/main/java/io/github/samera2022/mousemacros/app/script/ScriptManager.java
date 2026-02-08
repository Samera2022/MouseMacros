package io.github.samera2022.mousemacros.app.script;

import io.github.samera2022.mousemacros.app.config.ConfigManager;
import io.github.samera2022.mousemacros.app.config.WhitelistManager;
import org.graalvm.polyglot.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.samera2022.mousemacros.app.manager.LogManager.log;

public class ScriptManager {
    private static final Map<ScriptPlugin, Context> scriptContexts = new ConcurrentHashMap<>();
    private static final List<ScriptPlugin> scripts = new ArrayList<>();
    public static String SCRIPT_PATH;

    static {
        if (isDevMode()) {
            String projectRoot = System.getProperty("user.dir");
            SCRIPT_PATH = java.nio.file.Paths.get(projectRoot, "mouse-macros-api", "src", "test", "resources", "scripts").toString();
            System.out.println("Development mode detected. Loading scripts from: " + SCRIPT_PATH);
        } else {
            SCRIPT_PATH = ConfigManager.CONFIG_DIR + "/scripts/";
        }
        File dir = new File(SCRIPT_PATH);
        if (!dir.exists() && !dir.mkdirs()) {
            System.err.println("Failed to create script directory: " + SCRIPT_PATH);
        }
    }

    private static boolean isDevMode() {
        URL resource = ScriptManager.class.getResource("ScriptManager.class");
        return resource != null && resource.getProtocol().equals("file");
    }

    public static synchronized void loadAndProcessScripts() {
        // 1. Cleanup existing scripts
        scriptContexts.values().forEach(Context::close);
        scriptContexts.clear();
        scripts.clear();

        // 2. Scan directory, read metadata, and auto-disable non-whitelisted native-access scripts
        File dir = new File(SCRIPT_PATH);
        if (!dir.exists()) return;

        File[] files = dir.listFiles((d, name) -> name.endsWith(".js") || name.endsWith(".js.disabled"));
        if (files == null) return;

        for (File file : files) {
            ScriptDescription metadata = readMetadataOnly(file);
            ScriptPlugin scriptPlugin = new ScriptPlugin(file, metadata);

            boolean whitelisted = WhitelistManager.isWhitelisted(metadata);

            // If a script requires native access, is NOT whitelisted, and is NOT already disabled, disable it now.
            if (metadata.isRequiresNativeAccess() && !whitelisted && !file.getName().endsWith(".disabled")) {
                System.out.println("Script '" + file.getName() + "' requires native access and is not whitelisted. Disabling by default.");
                File newFile = new File(file.getParent(), file.getName() + ".disabled");
                try {
                    Files.move(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    scriptPlugin.setFile(newFile);
                    scriptPlugin.setEnabled(false);
                } catch (IOException e) {
                    System.err.println("Failed to auto-disable script: " + file.getName());
                    e.printStackTrace();
                }
            }

            // Add issue for non-whitelisted native scripts so the UI can show it
            if (metadata.isRequiresNativeAccess() && !whitelisted) {
                scriptPlugin.addIssue(new ScriptIssue(ScriptProblem.REQUIRE_NATIVE_ACCESS_CONFIRMATION, null));
            }

            scripts.add(scriptPlugin);
        }

        // 3. Check dependencies for all discovered scripts
        checkDependencies();

        // 4. Enable all scripts that are not explicitly disabled
        for (ScriptPlugin script : scripts) {
            if (script.isEnabled()) { // isEnabled is determined by file name not ending with .disabled
                if (script.getDescription().isRequiresNativeAccess()) {
                    // It's a native script. It must be whitelisted to be enabled here.
                    if (WhitelistManager.isWhitelisted(script.getDescription())) {
                        enableScript(script, true); // Grant access
                    } else {
                        // This case should not be reached if the logic above is correct,
                        // as non-whitelisted native scripts should have been disabled.
                        // But as a safeguard:
                        System.err.println("Security Warning: Cannot auto-enable non-whitelisted native script: " + script.getName());
                    }
                } else {
                    // Not a native script, enable normally.
                    enableScript(script, false);
                }
            }
        }
    }

    public static void checkDependencies() {
        Map<String, ScriptPlugin> scriptMap = new HashMap<>();
        for (ScriptPlugin script : scripts) {
            script.clearIssues();
            scriptMap.put(script.getRegisterName(), script);
        }

        Set<ScriptPlugin> visited = new HashSet<>();
        Set<ScriptPlugin> recursionStack = new HashSet<>();

        for (ScriptPlugin script : scripts) {
            checkScriptHealth(script, scriptMap, visited, recursionStack);
        }
    }

    private static void checkScriptHealth(ScriptPlugin script, Map<String, ScriptPlugin> scriptMap, Set<ScriptPlugin> visited, Set<ScriptPlugin> recursionStack) {
        if (recursionStack.contains(script)) {
            return;
        }
        if (visited.contains(script)) return;

        recursionStack.add(script);

        for (String depName : script.getDescription().getHardDependencies()) {
            ScriptPlugin dep = scriptMap.get(depName);
            if (dep == null) {
                script.addIssue(new ScriptIssue(ScriptProblem.H_DEP_MISSING, new String[]{depName}));
            } else {
                checkScriptHealth(dep, scriptMap, visited, recursionStack);

                if (dep.hasSevereIssue()) {
                    script.addIssue(new ScriptIssue(ScriptProblem.H_DEP_PROBLEM_SEVERE, new String[]{depName}));
                } else if (dep.hasIssue()) {
                    script.addIssue(new ScriptIssue(ScriptProblem.H_DEP_PROBLEM_NOT_SEVERE, new String[]{depName}));
                }
            }
        }

        for (String depName : script.getDescription().getSoftDependencies()) {
            ScriptPlugin dep = scriptMap.get(depName);
            if (dep != null) {
                checkScriptHealth(dep, scriptMap, visited, recursionStack);
            }
        }

        visited.add(script);
        recursionStack.remove(script);
    }
    /*
    * 采用正则表达实在不方便维护……
    * */
    private static ScriptDescription readMetadataOnly(File scriptFile) {
        String fileName = scriptFile.getName();
        String baseName = fileName.endsWith(".disabled") ? fileName.substring(0, fileName.length() - ".disabled".length()) : fileName;
        String registerNameDefault = baseName.lastIndexOf('.') > 0 ? baseName.substring(0, baseName.lastIndexOf('.')) : baseName;

        String author = "Unknown", version = "0.0.0", description = "No description", displayName = baseName, registerName = registerNameDefault, availableVersion = "0.0.0";
        boolean requiresNativeAccess = false;
        String nativeAccessDescription = "";
        String[] softDependencies = new String[0];
        String[] hardDependencies = new String[0];

        try (Context tempContext = Context.create("js")) {
            Source source = Source.newBuilder("js", scriptFile).build();
            try {
                tempContext.eval(source);
            } catch (PolyglotException e) {
                // This is expected
            }

            Value bindings = tempContext.getBindings("js");
            if (bindings.hasMember("display_name")) displayName = bindings.getMember("display_name").asString();
            if (bindings.hasMember("register_name")) registerName = bindings.getMember("register_name").asString();
            if (bindings.hasMember("registry_name")) registerName = bindings.getMember("registry_name").asString();
            if (bindings.hasMember("author")) author = bindings.getMember("author").asString();
            if (bindings.hasMember("version")) version = bindings.getMember("version").asString();
            if (bindings.hasMember("description")) description = bindings.getMember("description").asString();
            if (bindings.hasMember("available_version")) availableVersion = bindings.getMember("available_version").asString();
            if (bindings.hasMember("requireNativeAccess")) {
                requiresNativeAccess = bindings.getMember("requireNativeAccess").asBoolean();
            }
            if (bindings.hasMember("requireNativeAccessDescription")) {
                nativeAccessDescription = bindings.getMember("requireNativeAccessDescription").asString();
            }
            softDependencies = readStringArray(bindings, "soft_dependencies");
            hardDependencies = readStringArray(bindings, "hard_dependencies");

        } catch (IOException e) {
            System.err.println("Error reading script file for metadata: " + fileName);
            e.printStackTrace();
        }

        return new ScriptDescription(baseName, version, author, description, displayName, registerName, availableVersion, softDependencies, hardDependencies, requiresNativeAccess, nativeAccessDescription);
    }

    private static String[] readStringArray(Value bindings, String key) {
        if (bindings == null || !bindings.hasMember(key)) {
            return new String[0];
        }
        Value value = bindings.getMember(key);
        if (value == null || value.isNull()) {
            return new String[0];
        }
        if (value.hasArrayElements()) {
            int size = (int) value.getArraySize();
            String[] result = new String[size];
            for (int i = 0; i < size; i++) {
                Value element = value.getArrayElement(i);
                result[i] = element != null && element.isString() ? element.asString() : String.valueOf(element);
            }
            return result;
        }
        if (value.isString()) {
            return new String[]{value.asString()};
        }
        return new String[]{String.valueOf(value)};
    }

    private static void loadScriptContext(ScriptPlugin script, Context context) throws IOException, PolyglotException {
        ScriptContext scriptContext = new ScriptContext(script.getFile(), script.getDescription());
        ScriptAPI api = new ScriptAPI(scriptContext);
        script.setApiInstance(api);
        context.getBindings("js").putMember("mm", api);
        Source source = Source.newBuilder("js", script.getFile()).build();
        context.eval(source);
    }

    public static void enableScript(ScriptPlugin script) {
        if (script.getDescription().isRequiresNativeAccess()) {
            System.err.println("Cannot enable script '" + script.getName() + "' without explicit user consent for native access.");
            return;
        }
        enableScript(script, false);
    }

    public static void enableScript(ScriptPlugin script, boolean grantNativeAccess) {
        if (script.isEnabled() && scriptContexts.containsKey(script)) return;

        if (grantNativeAccess && !script.getDescription().isRequiresNativeAccess()) {
            System.err.println("Security Warning: Attempted to grant native access to a script that did not request it. Aborting.");
            return;
        }

        File currentFile = script.getFile();
        if (currentFile.getName().endsWith(".disabled")) {
            String newFileName = currentFile.getName().substring(0, currentFile.getName().length() - ".disabled".length());
            File newFile = new File(currentFile.getParent(), newFileName);
            try {
                Files.move(currentFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                script.setFile(newFile);
            } catch (IOException e) {
                System.err.println("Failed to rename file for enabling script: " + currentFile.getName());
                e.printStackTrace();
                return;
            }
        }

        try {
            log("[Script] Enabling ... " + script.getName() + (grantNativeAccess ? " [Native Access Granted]" : ""));
            Context context = Context.newBuilder("js")
                    .allowHostAccess(HostAccess.ALL)
                    .allowHostClassLookup(className -> className.startsWith("io.github.samera2022.mousemacros.api"))
                    .allowIO(true)
                    .allowEnvironmentAccess(EnvironmentAccess.INHERIT)
                    .allowNativeAccess(grantNativeAccess)
                    .option("engine.WarnInterpreterOnly", "false")
                    .build();
            scriptContexts.put(script, context);
            loadScriptContext(script, context);
            script.setLoaded(true);
            script.setEnabled(true);
            log("[Script] Enabled √ " + script.getName() + (grantNativeAccess ? " [Native Access Granted]" : ""));
        } catch (IOException | PolyglotException e) {
            log("Failed to enable script: " + script.getName());
            e.printStackTrace();
            script.setLoaded(false);
            script.setEnabled(false);
            Context context = scriptContexts.remove(script);
            if (context != null) context.close();
        }
    }

    public static void disableScript(ScriptPlugin script) {
        if (!script.isEnabled()) return;

        log("[Script] Disabling ... " + script.getName());

        if (script.getApiInstance() != null) {
            script.getApiInstance().cleanup();
            script.setApiInstance(null);
        }

        Context context = scriptContexts.remove(script);
        if (context != null) context.close();

        File currentFile = script.getFile();
        if (!currentFile.getName().endsWith(".disabled")) {
            String newFileName = currentFile.getName() + ".disabled";
            File newFile = new File(currentFile.getParent(), newFileName);
            try {
                Files.move(currentFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                script.setFile(newFile);
                log("[Script] Disabled √ " + script.getName());
            } catch (IOException e) {
                log("Failed to rename script: " + script.getName());
                e.printStackTrace();
            }
        }

        script.setEnabled(false);
        script.setLoaded(false);
    }

    public static List<ScriptPlugin> getScripts() {
        return new ArrayList<>(scripts);
    }
}
