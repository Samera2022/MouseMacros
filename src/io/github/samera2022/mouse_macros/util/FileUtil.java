package io.github.samera2022.mouse_macros.util;

import io.github.samera2022.mouse_macros.constant.OtherConsts;
import io.github.samera2022.mouse_macros.fee.FeeConsts;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {
    // 读取文件内容，返回字符串，若不存在返回null
    public static String readFile(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) return null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        }
    }

    // 写入内容到文件，自动创建父目录
    public static void writeFile(String path, String content) throws IOException {
        File file = new File(path);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) parent.mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write(content);
        }
    }

    // 新增：读取目录下所有文件名（不含路径）
    public static String[] listFileNames(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists() || !dir.isDirectory()) return new String[0];
        File[] files = dir.listFiles();
        if (files == null) return new String[0];
        String[] names = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            names[i] = files[i].getName();
        }
        return names;
    }

    // 获取本地存储路径
    public static Path getLocalStoragePath() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            String localAppData = System.getenv("LOCALAPPDATA");
            return Paths.get(localAppData, OtherConsts.APP_NAME);
        } else if (os.contains("mac")) {
            return Paths.get(System.getProperty("user.home"), "Library", "Application Support", OtherConsts.APP_NAME);
        } else {
            return Paths.get(System.getProperty("user.home"), ".local", "share", OtherConsts.APP_NAME);
        }
    }
}
