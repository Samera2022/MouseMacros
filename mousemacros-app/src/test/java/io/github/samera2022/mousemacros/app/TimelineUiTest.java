package io.github.samera2022.mousemacros.app;

import com.github.kwhat.jnativehook.GlobalScreen;
import io.github.samera2022.mousemacros.app.config.CacheManager;
import io.github.samera2022.mousemacros.app.config.ConfigManager;
import io.github.samera2022.mousemacros.app.macro.MouseAction;
import io.github.samera2022.mousemacros.app.ui.frame.MainFrame;
import io.github.samera2022.mousemacros.app.ui.frame.SettingsDialog;
import org.junit.jupiter.api.*;
import io.github.samera2022.mousemacros.app.ui.dialog.WelcomeDialog;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;
import static org.junit.jupiter.api.Assertions.*;

class TimelineUiTest {
    static void edt(Runnable task) throws Exception {
        AtomicReference<Throwable> error = new AtomicReference<>();
        SwingUtilities.invokeAndWait(() -> { try { task.run(); } catch (Throwable t) { error.set(t); } });
        if (error.get() != null) throw new AssertionError(error.get());
    }
    static <T extends Component> T named(Container root, String name, Class<T> type) {
        for (Component c : root.getComponents()) {
            if (type.isInstance(c) && name.equals(c.getName())) return type.cast(c);
            if (c instanceof Container) { T found = named((Container)c, name, type); if (found != null) return found; }
        }
        return null;
    }
    static JButton button(Container root, String text) {
        for (Component c : root.getComponents()) {
            if (c instanceof JButton && text.equals(((JButton)c).getText())) return (JButton)c;
            if (c instanceof Container) { JButton found = button((Container)c, text); if (found != null) return found; }
        }
        return null;
    }
    @BeforeAll static void start() throws Exception {
        edt(() -> {
            ConfigManager.set("follow_system_settings", false);
            ConfigManager.set("switch_lang", "en_us");
            MainFrame.MAIN_FRAME.setVisible(true);
        });
    }
    @BeforeEach void reset() throws Exception {
        edt(() -> {
            ConfigManager.set("follow_system_settings", false);
            ConfigManager.set("switch_lang", "en_us");
            ConfigManager.set("enable_dark_mode", false);
            ConfigManager.set("enable_custom_macro_settings", true);
            ConfigManager.set("repeat_times", 3);
            ConfigManager.set("repeat_delay", 0.25);
            ConfigManager.set("readjust_frame_mode", ConfigManager.RFM_MEMORIZED);
            CacheManager.cache.windowSizeMap.clear();
            Localizer.load("en_us");
            MainFrame.MAIN_FRAME.refreshMainFrameTexts();
            MainFrame.logArea.getRowSorter().setSortKeys(null);
            MainFrame.clearTimeline();
        });
        edt(() -> {});
    }
    @AfterEach void closeDialogs() throws Exception {
        edt(() -> { for (Window w : Window.getWindows()) if (w instanceof JDialog) w.dispose(); });
    }
    @AfterAll static void stop() throws Exception {
        edt(() -> MainFrame.MAIN_FRAME.dispose());
        if (GlobalScreen.isNativeHookRegistered()) GlobalScreen.unregisterNativeHook();
    }
    @Test void actionNumbersExcludeSystemMessagesAndSortNumerically() throws Exception {
        edt(() -> {
            MainFrame.appendSystemLog("Recording started");
            for (int i=1; i<=12; i++) MainFrame.appendAction(new MouseAction(i,i,0,0,i));
        });
        edt(() -> {
            JTable table = MainFrame.logArea;
            assertNull(table.getModel().getValueAt(0,0));
            assertEquals(1, table.getModel().getValueAt(1,0));
            table.getRowSorter().toggleSortOrder(0);
            assertEquals(12, table.getValueAt(12,0));
            table.getRowSorter().setSortKeys(null);
            table.getRowSorter().toggleSortOrder(5);
            assertEquals(12L, table.getValueAt(12,5));
        });
    }
    @Test void toolbarFitsEveryLanguageAtMinimumWidth() throws Exception {
        edt(() -> {
            for (String lang : ConfigManager.getAvailableLangs()) {
                Localizer.load(lang);
                CacheManager.cache.windowSizeMap.put("main_frame", "200,100");
                MainFrame.MAIN_FRAME.refreshMainFrameTexts();
                MainFrame.MAIN_FRAME.validate();
                assertButtonsFit(MainFrame.MAIN_FRAME.getContentPane());
            }
        });
    }
    static void assertButtonsFit(Container parent) {
        for (Component c : parent.getComponents()) {
            if (c instanceof JButton && c.isShowing()) {
                assertTrue(c.getX()>=0 && c.getX()+c.getWidth()<=parent.getWidth(), "Clipped: "+((JButton)c).getText());
                assertTrue(c.getWidth()>=c.getPreferredSize().width, "Truncated: "+((JButton)c).getText());
                assertTrue(c.getY()>=0 && c.getY()+c.getHeight()<=parent.getHeight());
            }
            if (c instanceof Container) assertButtonsFit((Container)c);
        }
    }
    @Test void lightAndDarkThemesKeepEnabledButtonsReadable() throws Exception {
        edt(() -> {
            for (boolean dark : new boolean[]{false,true,false}) {
                ConfigManager.set("enable_dark_mode",dark);
                MainFrame.MAIN_FRAME.applyTheme();
                JButton start = button(MainFrame.MAIN_FRAME, "● "+Localizer.get("main_frame.start_record")+"  F2");
                assertNotNull(start);
                assertEquals(255,start.getBackground().getAlpha());
                assertTrue(contrast(start.getForeground(),start.getBackground()) >= 4.5);
            }
        });
    }
    static double luminance(Color c) {
        double sum=0; double[] weights={0.2126,0.7152,0.0722}; int[] channels={c.getRed(),c.getGreen(),c.getBlue()};
        for(int i=0;i<3;i++){double v=channels[i]/255.0;sum+=weights[i]*(v<=0.04045?v/12.92:Math.pow((v+0.055)/1.055,2.4));} return sum;
    }
    static double contrast(Color a,Color b){double x=luminance(a),y=luminance(b);return (Math.max(x,y)+0.05)/(Math.min(x,y)+0.05);}
    @Test void headersAndStatusSwitchLanguage() throws Exception {
        edt(() -> {
            Localizer.load("zh_cn"); MainFrame.MAIN_FRAME.refreshMainFrameTexts();
            assertEquals("事件",MainFrame.logArea.getColumnModel().getColumn(2).getHeaderValue());
            Localizer.load("en_us"); MainFrame.MAIN_FRAME.refreshMainFrameTexts();
            assertEquals("Event",MainFrame.logArea.getColumnModel().getColumn(2).getHeaderValue());
        });
    }
    @Test void settingsCloseDoesNotApplyUnsavedToggles() throws Exception {
        edt(() -> {
            SettingsDialog d=new SettingsDialog(); named(d,"enable_dark_mode",JCheckBox.class).doClick(); d.dispose();
            assertFalse(ConfigManager.getBoolean("enable_dark_mode"));
        });
    }
    @Test void togglingExpandableSettingsPreservesValuesAndSavesNumbers() throws Exception {
        edt(() -> {
            SettingsDialog d=new SettingsDialog();
            JTextField count=named(d,"repeat_times",JTextField.class),delay=named(d,"repeat_delay",JTextField.class);
            count.setText("7"); delay.setText("0.125");
            JCheckBox toggle=named(d,"enable_custom_macro_settings",JCheckBox.class);toggle.doClick();toggle.doClick();
            assertEquals("7",count.getText());assertEquals("0.125",delay.getText());
            button(d,Localizer.get("settings.save_settings")).doClick();
            assertEquals(7,ConfigManager.getInt("repeat_times"));assertEquals(0.125,ConfigManager.getDouble("repeat_delay"),0.00001);
        });
    }
    @Test void malformedAndOversizedCacheRemainUsable() throws Exception {
        edt(() -> {
            for(String cache:new String[]{"broken","0,0","99999,99999"}) {
                CacheManager.cache.windowSizeMap.put("main_frame",cache);MainFrame.MAIN_FRAME.readjust();
                assertTrue(MainFrame.MAIN_FRAME.getWidth()<=MainFrame.MAIN_FRAME.getGraphicsConfiguration().getBounds().width);
                assertTrue(MainFrame.MAIN_FRAME.getHeight()>0);assertButtonsFit(MainFrame.MAIN_FRAME.getContentPane());
            }
        });
    }
    @Test void settingsNavigationHasTranslations() throws Exception {
        edt(() -> {
            for (String lang : new String[]{"en_us", "zh_cn"}) {
                Localizer.load(lang);
                for (String key : new String[]{"section.shortcuts", "section.about", "keyboard_shortcut",
                        "configure", "welcome_wizard", "open", "author", "update_information", "script_manager"})
                    assertNotEquals("settings." + key, Localizer.getS(key));
            }
        });
    }
    static String allText(Container parent) {
        StringBuilder text = new StringBuilder();
        for (Component c : parent.getComponents()) {
            if (c instanceof JTextArea) text.append(((JTextArea)c).getText());
            if (c instanceof Container) text.append(allText((Container)c));
        }
        return text.toString();
    }
    @Test void wizardDisplaysConfiguredHotkeys() throws Exception {
        edt(() -> {
            int original = MainFrame.keyRecord;
            try {
                MainFrame.keyRecord = NativeKeyEvent.VC_F6;
                WelcomeDialog d = new WelcomeDialog(MainFrame.MAIN_FRAME);
                String text = allText(d);
                assertTrue(text.contains("F6"));
                assertTrue(text.contains("F3"));
                assertFalse(text.contains("default F9"));
                d.dispose();
            } finally { MainFrame.keyRecord = original; }
        });
    }
    @Test void wizardNavigationAndOptOutPersist() throws Exception {
        edt(() -> {
            ConfigManager.set("welcome_shown", false);
            assertTrue(WelcomeDialog.shouldShow());
            WelcomeDialog d = new WelcomeDialog(MainFrame.MAIN_FRAME);
            JButton next = button(d, Localizer.get("welcome.next"));
            JButton previous = button(d, Localizer.get("welcome.previous"));
            assertFalse(previous.isEnabled());
            next.doClick(); assertTrue(previous.isEnabled());
            previous.doClick(); assertFalse(previous.isEnabled());
            next.doClick(); next.doClick(); next.doClick();
            assertFalse(next.isVisible());
            button(d, Localizer.get("welcome.finish")).doClick();
            ConfigManager.reload();
            assertFalse(WelcomeDialog.shouldShow());
        });
    }
    @Test void uncheckedOptOutAllowsWizardNextLaunchWithoutEarlyMutation() throws Exception {
        edt(() -> {
            ConfigManager.set("welcome_shown", true);
            WelcomeDialog d = new WelcomeDialog(MainFrame.MAIN_FRAME);
            JCheckBox option = named(d, "welcome.dont_show_again", JCheckBox.class);
            option.doClick();
            assertFalse(WelcomeDialog.shouldShow());
            JButton next = button(d, Localizer.get("welcome.next"));
            next.doClick(); next.doClick(); next.doClick();
            button(d, Localizer.get("welcome.finish")).doClick();
            ConfigManager.reload();
            assertTrue(WelcomeDialog.shouldShow());
        });
    }
    @Test void absentSystemTrayDoesNotDestroyWindow() throws Exception {
        Assumptions.assumeFalse(SystemTray.isSupported());
        edt(() -> {MainFrame.MAIN_FRAME.minimizeToTray();assertTrue(MainFrame.MAIN_FRAME.isDisplayable());});
    }
}
