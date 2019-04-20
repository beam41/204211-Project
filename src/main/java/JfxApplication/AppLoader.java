package JfxApplication;

import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class AppLoader extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Font.loadFont(AppLoader.class.getResource("fontello.ttf").toExternalForm(), 16.0);
        SceneLoader.Load(stage, "SplashScene.fxml", false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
}
