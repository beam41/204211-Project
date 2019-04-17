package AppUI;

import AppModel.Price;
import AppService.SettingManager;
import AppService.TableManager;
import AppUtil.Text;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class FXMLControllerCreateTable implements Initializable {

    @FXML
    private Label amountLab;
    @FXML
    private Label adultLab;
    @FXML
    private Label kidLab;
    @FXML
    private JFXButton backBtn;
    @FXML
    private JFXButton okBtn;
    @FXML
    private JFXTextField adultTxtF;
    @FXML
    private JFXTextField kidTxtF;
    @FXML
    private Label tbLab;
    @FXML
    private JFXComboBox<Integer> tbNumCob;
    @FXML
    private JFXComboBox<String> courseCob;
    @FXML
    private Label errorLab;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tbNumCob.getItems().addAll(TableManager.i().getTableNumSet());
        tbNumCob.getSelectionModel().selectFirst();
        JFXTextField[] txtFList = {adultTxtF, kidTxtF};
        for (JFXTextField txtF : txtFList)
            txtF.setStyle("-fx-text-fill: #fff; -fx-prompt-text-fill:  #626262");
        JFXTextField[] txtFListInt = {adultTxtF, kidTxtF};
        for (JFXTextField txtF : txtFListInt) {
            txtF.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    txtF.setText(newValue.replaceAll("\\D", ""));
                }
            });
        }
        for (Map.Entry<String, Price> pm : SettingManager.i().getPriceMap().entrySet()) {
            courseCob.getItems().add(pm.getValue().getName());
        }
        courseCob.getSelectionModel().selectFirst();
        tbLab.setText(Text.TTABLE.get());
        courseCob.setPromptText(Text.COURSE.get());
        amountLab.setText(Text.AMOUNT.get());
        adultLab.setText(Text.ADULT.get());
        kidLab.setText(Text.KID.get());
        errorLab.setText(Text.ERR_PEMPTY.get());
        backBtn.setText(Text.BACK.get());
        okBtn.setText(Text.OK.get());
    }

    @FXML
    void okClicked() {
        if (TableManager.i().tableEmpty()) {
            errorLab.setText(Text.ERR_TEMPTY.get());
            msgToVisible();
            return;
        }
        if (!TableManager.i().tableExist(tbNumCob.getValue())) {
            errorLab.setText(Text.ERR_TINVALID.get());
            msgToVisible();
            return;
        }
        boolean isReady = true;
        JFXTextField[] txtFList = {adultTxtF, kidTxtF};
        for (JFXTextField txtF : txtFList) {
            txtF.setStyle("-fx-text-fill: #fff; -fx-prompt-text-fill:  #626262");
            if (txtF.getText().isEmpty()) {
                txtF.setStyle("-fx-background-color: rgba(239, 83, 80,0.3); -fx-text-fill: #fff; -fx-prompt-text-fill:  #626262");
                errorLab.setText(Text.ERR_PEMPTY.get());
                msgToVisible();
                isReady = false;
            }
        }
        if (Integer.valueOf(adultTxtF.getText()) + Integer.valueOf(kidTxtF.getText()) < 1) {
            for (JFXTextField txtF : txtFList) {
                txtF.setStyle("-fx-text-fill: #fff; -fx-prompt-text-fill:  #626262");
                txtF.setStyle("-fx-background-color: rgba(239, 83, 80,0.3); -fx-text-fill: #fff; -fx-prompt-text-fill:  #626262");
                errorLab.setText(Text.ERR_MUL.get());
                msgToVisible();
                isReady = false;
            }
        }
        if (isReady) {
            TableManager.i().newTableActive(tbNumCob.getValue(),
                    courseCob.getValue(),
                    Integer.parseInt(kidTxtF.getText()),
                    Integer.parseInt(adultTxtF.getText()
                    ));
            goBack();
        }

    }

    @FXML
    void goBack() {
        ((Stage) backBtn.getScene().getWindow()).close();
    }

    private void msgToVisible() {
        errorLab.setOpacity(0);
        errorLab.setVisible(true);
        FadeTransition ft = new FadeTransition(Duration.millis(150), errorLab);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
        FadeTransition ft2 = new FadeTransition(Duration.millis(150), errorLab);
        ft2.setDelay(Duration.seconds(3));
        ft2.setFromValue(1);
        ft2.setToValue(0);
        ft2.play();
        ft2.setOnFinished(e -> errorLab.setVisible(false));
    }
}
