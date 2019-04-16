package AppUI;

import AppModel.Course;
import AppModel.Price;
import AppModel.TableActive;
import AppService.SettingManager;
import AppService.TableManager;
import AppUtil.Text;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class FXMLControllerCreateTable implements Initializable {

    @FXML
    Label amountLab;
    @FXML
    Label adultLab;
    @FXML
    Label kidLab;
    @FXML
    JFXButton backBtn;
    @FXML
    JFXButton okBtn;
    @FXML
    JFXTextField adultTxtF;
    @FXML
    JFXTextField kidTxtF;
    @FXML
    Label tbLab;
    @FXML
    JFXComboBox<Integer> tbNumCob;
    @FXML
    JFXComboBox<String> courseCob;
    @FXML
    Label errorLab;

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
    }

    @FXML
    void okClicked() {
        boolean isReady = true;
        JFXTextField[] txtFList = {adultTxtF, kidTxtF};
        for (JFXTextField txtF : txtFList) {
            txtF.setStyle("-fx-text-fill: #fff; -fx-prompt-text-fill:  #626262");
            if (txtF.getText().isEmpty()) {
                txtF.setStyle("-fx-background-color: rgba(198,40,40,0.2); -fx-text-fill: #fff; -fx-prompt-text-fill:  #626262");
                errorLab.setText("Where is people");
                errorLab.setVisible(true);
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
        ((Stage)backBtn.getScene().getWindow()).close();
    }
}