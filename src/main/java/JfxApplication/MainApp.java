package JfxApplication;

import AppService.Logger;
import AppService.SaverAndLoader;
import AppService.SettingManager;

import java.io.File;

public class MainApp {
    public static void main(String[] args) {
        System.setProperty("prism.lcdtext", "false");
        // region The initialize
        File directory = new File(SaverAndLoader.getFile(""));
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                SaverAndLoader.saveTo(SettingManager.i(), "settings.json");
                SaverAndLoader.saveTo(new int[]{}, "tableActives.json");
                SaverAndLoader.saveTo(new int[]{}, "tableBookings.json");
            }
        }
        // endregion
        SaverAndLoader.settingGetFrom("settings.json");
        Logger.i();
        AppLoader.main(args);
    }
}
