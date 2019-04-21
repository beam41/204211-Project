package JfxApplication;

import AppService.SaverAndLoader;

public class MainApp {
    public static void main(String[] args) {
        System.setProperty("prism.lcdtext", "false");
        // region The initialize
        SaverAndLoader.init();
        AppLoader.main(args);
    }
}
