package JfxApplication;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneLoader {

    public void Load(Stage stage, String path, boolean resizeAble) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(path));

            Scene scene = new Scene(root);
            stage.setResizable(resizeAble);
            stage.setScene(scene);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // for code init controller
    public void Load(Stage stage, String path, boolean resizeAble, String styleSheet, Object controller) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource(styleSheet).toExternalForm());
            stage.setResizable(resizeAble);
            stage.setScene(scene);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // for preserve scene size
    public void Load(Stage stage, String path, boolean resizeAble, String styleSheet, double width, double height) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(path));

            Scene scene = new Scene(root, width, height);
            scene.getStylesheets().add(getClass().getResource(styleSheet).toExternalForm());
            stage.setResizable(resizeAble);
            stage.setScene(scene);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
