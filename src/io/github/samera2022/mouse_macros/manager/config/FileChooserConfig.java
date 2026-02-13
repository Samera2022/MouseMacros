package io.github.samera2022.mouse_macros.manager.config;

import java.io.File;

// 配置存储类
public class FileChooserConfig {
    private String lastLoadDirectory;
    private String lastSaveDirectory;

    // Gson 需要无参构造函数
    public FileChooserConfig() {}

    public File getLastLoadDirectory() {
        return lastLoadDirectory != null ? new File(lastLoadDirectory) : null;
    }

    public void setLastLoadDirectory(File directory) {
        this.lastLoadDirectory = directory != null ? directory.getAbsolutePath() : null;
    }

    public File getLastSaveDirectory() {
        return lastSaveDirectory != null ? new File(lastSaveDirectory) : null;
    }

    public void setLastSaveDirectory(File directory) {
        this.lastSaveDirectory = directory != null ? directory.getAbsolutePath() : null;
    }
}