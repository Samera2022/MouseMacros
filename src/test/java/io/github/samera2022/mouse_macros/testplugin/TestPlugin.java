package io.github.samera2022.mousemacros.testplugin;

import io.github.samera2022.mousemacros.app.script.JavaPlugin;

public class TestPlugin extends JavaPlugin {
    public static boolean onLoadCalled = false;
    public static boolean onEnableCalled = false;
    public static boolean onDisableCalled = false;

    @Override
    public void onLoad() {
        onLoadCalled = true;
        System.out.println("TestPlugin: onLoad executed!");
    }

    @Override
    public void onEnable() {
        onEnableCalled = true;
        System.out.println("TestPlugin: onEnable executed!");
        System.out.println("TestPlugin: Name=" + getName() + ", Version=" + getVersion());
    }

    @Override
    public void onDisable() {
        onDisableCalled = true;
        System.out.println("TestPlugin: onDisable executed!");
    }
}
