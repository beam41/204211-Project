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
import java.time.LocalDateTime;
import java.util.Map;
import java.util.ResourceBundle;

public class FXMLControllerBooking implements Initializable {

    @FXML
    private Label amountLab;
    @FXML
    private Label adultLab;
    @FXML
    private Label kidLab;
    @FXML
    private JFXButton backBtn;
    @FXML
    private JFXButton bookBtn;
    @FXML
    private JFXTextField adultTxtF;
    @FXML
    private JFXTextField kidTxtF;
    @FXML
    private Label nameLab;
    @FXML
    private JFXTextField nameTxtF;
    @FXML
    private Label timeLab;
    @FXML
    private JFXComboBox<Integer> dayCob;
    @FXML
    private JFXComboBox<String> monthCob;
    @FXML
    private JFXComboBox<String> yearCob;
    @FXML
    private JFXComboBox<String> hourCob;
    @FXML
    private JFXComboBox<String> minCob;
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
        LocalDateTime now = LocalDateTime.now();
        for (int i = 1; i <= 12; i++) {
            monthCob.getItems().add(String.valueOf(i));
        }
        for (int i = 0; i < 3; i++) {
            yearCob.getItems().add(String.valueOf(now.getYear() + i));
        }
        monthCob.getSelectionModel().select(now.getMonthValue() - 1);
        yearCob.getSelectionModel().selectFirst();
        monthCob.valueProperty().addListener(e -> updateDay());
        yearCob.valueProperty().addListener(e -> updateDay());
        updateDay();
        dayCob.getSelectionModel().select(now.getDayOfMonth() - 1);
        for (int i = 0; i <= 23; i++) {
            hourCob.getItems().add(String.format("%02d", i));
        }
        for (int i = 0; i <= 59; i++) {
            minCob.getItems().add(String.format("%02d", i));
        }
        hourCob.getSelectionModel().select(now.getHour());
        minCob.getSelectionModel().select(now.getMinute());
        tbLab.setText(Text.TTABLE.get());
        nameLab.setText(Text.NAME.get());
        amountLab.setText(Text.AMOUNT.get());
        adultLab.setText(Text.ADULT.get());
        kidLab.setText(Text.KID.get());
        timeLab.setText(Text.TIME.get());
        backBtn.setText(Text.BACK.get());
        bookBtn.setText(Text.BOOKING.get());
    }


    @FXML
    void bookClicked() {
        if (TableManager.i().tableEmpty()) {
            errorLab.setText(Text.ERR_TEMPTY.get());
            msgToVisible();
            return;
        }
        boolean isReady = true;
        JFXTextField[] txtFList = {nameTxtF, adultTxtF, kidTxtF};
        for (JFXTextField txtF : txtFList) {
            txtF.setStyle("-fx-text-fill: #fff; -fx-prompt-text-fill:  #626262");
            if (txtF.getText().isBlank()) {
                txtF.setStyle("-fx-background-color: rgba(198,40,40,0.2); -fx-text-fill: #fff; -fx-prompt-text-fill:  #626262");
                errorLab.setText(Text.ERR_PEMPTY.get());
                msgToVisible();
                isReady = false;
            }
        }
        LocalDateTime timeCheckin = LocalDateTime.of(Integer.parseInt(yearCob.getValue()),
                Integer.parseInt(monthCob.getValue()),
                dayCob.getValue(),
                Integer.parseInt(hourCob.getValue()),
                Integer.parseInt(minCob.getValue()));
        JFXComboBox[] cob = {yearCob, monthCob, dayCob, hourCob, minCob};
        for (JFXComboBox box : cob) {
            box.setStyle("-fx-background-color: transparent;");
        }
        if (timeCheckin.isBefore(LocalDateTime.now())) {
            for (JFXComboBox box : cob) {
                box.setStyle("-fx-background-color: rgba(239, 83, 80,0.3);");
            }
            errorLab.setText(isReady ? Text.ERR_TIMEIN.get() : Text.ERR_MUL.get());
            msgToVisible();
            isReady = false;
        }
        if (Integer.valueOf(adultTxtF.getText()) + Integer.valueOf(kidTxtF.getText()) > 1) {
            for (JFXTextField txtF : txtFList) {
                txtF.setStyle("-fx-text-fill: #fff; -fx-prompt-text-fill:  #626262");
                txtF.setStyle("-fx-background-color: rgba(239, 83, 80,0.3); -fx-text-fill: #fff; -fx-prompt-text-fill:  #626262");
                errorLab.setText(Text.ERR_MUL.get());
                msgToVisible();
                isReady = false;
            }
        }
        if (isReady) {
            TableManager.i().newTableBooking(tbNumCob.getValue(),
                    courseCob.getValue(),
                    nameTxtF.getText(),
                    Integer.parseInt(kidTxtF.getText()),
                    Integer.parseInt(adultTxtF.getText()),
                    timeCheckin);
            goBack();
        }

    }

    private void updateDay() {
        dayCob.getItems().clear();
        int month = Integer.parseInt(monthCob.getValue());
        int year = Integer.parseInt(yearCob.getValue());
        int day;
        switch (month) {
            case 2:
                if (year % 4 == 0) {
                    if (year % 100 == 0) {
                        if (year % 400 == 0) {
                            day = 29;
                            break;
                        }
                        day = 28;
                        break;
                    }
                    day = 29;
                    break;
                }
                day = 28;
                break;
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            default:
                day = 30;
        }
        for (int i = 1; i <= day; i++) {
            dayCob.getItems().add(i);
        }
        dayCob.getSelectionModel().selectFirst();
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
