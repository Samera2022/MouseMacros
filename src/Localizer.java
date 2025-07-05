import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import java.io.File;

public class Localizer {
    private static Map<String, String> translations = new HashMap<>();
    private static String currentLang;
    private static boolean runtimeSwitch = false;

    static {
        // 自动检测系统语言
        String sysLang = java.util.Locale.getDefault().toString().toLowerCase();
        sysLang = sysLang.replace('-', '_');
        if (sysLang.startsWith("zh")) sysLang = "zh_cn";
        else if (sysLang.startsWith("en")) sysLang = "en_us";
        // 检查本地化文件是否存在，否则用en_us
        File langFile = new File("lang/" + sysLang + ".json");
        if (!langFile.exists()) langFile = new File("./lang/" + sysLang + ".json");
        if (!langFile.exists()) sysLang = "en_us";
        currentLang = sysLang;
        load(currentLang);
    }

    public static void setRuntimeSwitch(boolean enable) {
        runtimeSwitch = enable;
    }

    public static boolean isRuntimeSwitch() {
        return runtimeSwitch;
    }

    public static void load(String lang) {
        try {
            File file = new File("lang/" + lang + ".json");
            if (!file.exists()) file = new File("./lang/" + lang + ".json");
            translations = new Gson().fromJson(new FileReader(file), Map.class);
            currentLang = lang;
        } catch (Exception e) {
            translations = new HashMap<>();
        }
    }

    public static String get(String key) {
        String value = translations.get(key);
        if (value != null) return value;
        // 若当前语言缺失，尝试读取英文
        if (!"en_us".equals(currentLang)) {
            try {
                File file = new File("lang/en_us.json");
                if (!file.exists()) file = new File("./lang/en_us.json");
                Map<String, String> enMap = new Gson().fromJson(new FileReader(file), Map.class);
                String enValue = enMap.get(key);
                if (enValue != null) return enValue;
            } catch (Exception ignored) {}
        }
        return key;
    }

    public static String getCurrentLang() {
        return currentLang;
    }
}
