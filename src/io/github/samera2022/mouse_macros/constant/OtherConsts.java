package io.github.samera2022.mouse_macros.constant;

import io.github.samera2022.mouse_macros.UpdateInfo;

public class OtherConsts {
    public static final int DARK_MODE = 0;
    public static final int LIGHT_MODE = 1;

    public static final String ABOUT_AUTHOR = "    你好，我是MouseMacros的作者Samera2022。首先非常感谢能够使用本程序，谨在此致以最为诚挚的欢迎。\n" +
            "    你可以通过B站UID: 583460263 / QQ: 3517085924来找到我来反馈各类使用问题，当然闲聊之类的也是肯定可以的！不过反馈使用问题还是建议在Github提交Issues，这样我能更及时看得到。\n" +
            "    噢，还没有介绍这个项目的Github地址！但是想必聪明的你已经猜出来这个项目应该就是Samera2022/MouseMacros了。没错！本项目的地址为https://github.com/Samera2022/MouseMacros，如果帮到你的话还请不要吝啬你的star啦！\n";

    public static void main(String[] args) {
        System.out.println(UpdateInfo.VERSION_0_0_2.getFormattedLog());
    }
}
