package JfxApplication;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneLoader {

    public static void Load(Stage stage, String path, boolean resizeAble) {
        try {
            Parent root = FXMLLoader.load(SceneLoader.class.getResource(path));

            Scene scene = new Scene(root);
            stage.getIcons().clear();
            stage.getIcons().add(new Image(SceneLoader.class.getResourceAsStream("logotm_nHy_icon.png")));
            stage.setResizable(resizeAble);
            stage.setScene(scene);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // for code init controller
    public static void Load(Stage stage, String path, boolean resizeAble, String styleSheet, Object controller) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneLoader.class.getResource(path));
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(SceneLoader.class.getResource(styleSheet).toExternalForm());
            stage.getIcons().clear();
            stage.getIcons().add(new Image(SceneLoader.class.getResourceAsStream("logotm_nHy_icon.png")));
            stage.setResizable(resizeAble);
            stage.setScene(scene);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // for preserve scene size
    public static void Load(Stage stage, String path, boolean resizeAble, String styleSheet, double width, double height) {
        try {
            Parent root = FXMLLoader.load(SceneLoader.class.getResource(path));

            Scene scene = new Scene(root, width, height);
            scene.getStylesheets().add(SceneLoader.class.getResource(styleSheet).toExternalForm());
            stage.getIcons().clear();
            stage.getIcons().add(new Image(SceneLoader.class.getResourceAsStream("logotm_nHy_icon.png")));
            stage.setResizable(resizeAble);
            stage.setScene(scene);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
