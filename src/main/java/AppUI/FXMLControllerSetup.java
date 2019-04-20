package AppUI;

import AppService.SettingManager;
import AppUtil.Lang;
import AppUtil.Text;
import JfxApplication.SceneLoader;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class FXMLControllerSetup implements Initializable {

    @FXML
    private JFXButton startBtn;
    @FXML
    private JFXButton THBtn;
    @FXML
    private JFXButton ENBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        startBtn.setText(Text.START.get());
    }

    @FXML
    private void THButtonClick() {
        SettingManager.i().setLanguage(Lang.Thai);
        startBtn.setText(Text.START.get());
    }

    @FXML
    private void ENButtonClick() {
        SettingManager.i().setLanguage(Lang.English);
        startBtn.setText(Text.START.get());
    }

    @FXML
    private void startBtnClick() {
        Stage stage = (Stage) startBtn.getScene().getWindow();

        SceneLoader.Load(stage, "settingScene.fxml", true);
    }
}
