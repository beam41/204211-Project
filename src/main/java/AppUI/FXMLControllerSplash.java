package AppUI;

import AppService.SettingManager;
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
    private ImageView logo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Timeline change = new Timeline(new KeyFrame(Duration.seconds(2)));
        change.setOnFinished(e -> {
            Stage currStage = (Stage) logo.getScene().getWindow();
            Stage stage = new Stage();
            SceneLoader.Load(stage, SettingManager.i().getTableCount() == -1 ? "setupScene.fxml" : "mainScene.fxml",
                    SettingManager.i().getTableCount() != -1);
            stage.setTitle("Table Manager");
            stage.setOnHidden(e2 -> Platform.exit());
            stage.initStyle(StageStyle.DECORATED);
            stage.show();
            currStage.close();
        });
        change.play();
    }
}
