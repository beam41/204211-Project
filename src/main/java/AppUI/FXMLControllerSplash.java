package AppUI;

import JfxApplication.SceneLoader;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class FXMLControllerSplash implements Initializable {

    @FXML
    ImageView logo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Timeline change = new Timeline(new KeyFrame(Duration.seconds(2)));
        change.setOnFinished(e -> {
            Stage currStage = (Stage) logo.getScene().getWindow();
            SceneLoader loader = new SceneLoader();
            Stage stage = new Stage();
            loader.Load(stage, "SetupScene.fxml", false);
            stage.setTitle("Table Manager");
            stage.setOnHidden(e2 -> Platform.exit());
            stage.initStyle(StageStyle.DECORATED);
            currStage.close();
            stage.show();
        });
        change.play();
    }
}
