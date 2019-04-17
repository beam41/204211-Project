package AppUI;

import AppModel.TableActive;
import AppService.SettingManager;
import AppService.TableManager;
import AppUtil.Text;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class FXMLControllerCheckBill implements Initializable {

    TableActive table;
    @FXML
    private Label courseTxtLab;
    @FXML
    private Label timeLab;
    @FXML
    private Label fineLab;
    @FXML
    private Label serviceChargeLab;
    @FXML
    private Label adultLab;
    @FXML
    private Label kidNumLab;
    @FXML
    private Label adultNumLab;
    @FXML
    private Label kidLab;
    @FXML
    private Label totalLab;
    @FXML
    private JFXButton backBtn;
    @FXML
    private JFXButton stopNBillBtn;
    @FXML
    private Label showFineLab;
    @FXML
    private Label showSCPerLab;
    @FXML
    private Label showSCLab;
    @FXML
    private Label showTotalLab;
    @FXML
    private JFXTextField otherFineAmountTxtF;
    @FXML
    private Label otherFineLab;
    @FXML
    private Label totalTimeLab;
    @FXML
    private Label adultPriceLab;
    @FXML
    private Label kidPriceLab;
    @FXML
    private Label overTimeLab;
    @FXML
    private Label tbLab;
    @FXML
    private Label tbNameLab;
    @FXML
    private Label courseLab;

    FXMLControllerCheckBill(int id) {
        table = (TableActive) TableManager.i().findById(id);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // region table Label
        tbNameLab.setText(String.valueOf(table.getTableNum()));
        courseLab.setText(table.getCourse());
        adultNumLab.setText(String.valueOf(table.getAdultNumber()));
        kidNumLab.setText(String.valueOf(table.getKidNumber()));
        adultPriceLab.setText(String.format("%.2f", table.calAdultsPrice()));
        kidPriceLab.setText(String.format("%.2f", table.calKidsPrice()));
        // endregion

        backBtn.setOnMouseClicked(e -> goBack());
        if (SettingManager.i().isLimitTime()) {
            if (table.isFinished()) {
                stopTimer();
            }
            else {
                stopNBillBtn.setText(Text.STOP.get());
                stopNBillBtn.setStyle("-fx-background-color: #f65662");
                stopNBillBtn.setOnMouseClicked(e -> stopTimer());
                totalTimeLab.setText(Text.MSG_STOPCLOCK.get());
                totalTimeLab.setStyle("-fx-text-fill: #ff8a80");
                showSCLab.setVisible(false);
                showTotalLab.setVisible(false);
                overTimeLab.setVisible(false);
                showFineLab.setVisible(false);
                otherFineAmountTxtF.setDisable(true);
            }
        }
        else {
            stopNBillBtn.setText(Text.END.get());
            stopNBillBtn.setStyle("-fx-background-color:  #01a1ad");
            stopNBillBtn.setOnMouseClicked(e -> endTable());
            fineLab.setVisible(false);
            overTimeLab.setVisible(false);
            showFineLab.setVisible(false);
            timeLab.setVisible(false);
            totalTimeLab.setVisible(false);
            showSCLab.setText(String.format("%.2f", calSC()));
            showTotalLab.setText(String.format("%.2f", calFinalPrice()));
        }
        showSCPerLab.setText(SettingManager.i().getServiceCharge() + " %");
        otherFineAmountTxtF.textProperty().addListener(e -> amountChange());

        tbLab.setText(Text.TTABLE.get());
        courseTxtLab.setText(Text.COURSE.get());
        adultLab.setText(Text.ADULT.get());
        kidLab.setText(Text.KID.get());
        timeLab.setText(Text.TIME.get());
        fineLab.setText(Text.FINE.get());
        serviceChargeLab.setText(Text.SERVICECHARGE.get());
        otherFineLab.setText(Text.OTHER.get());
        totalLab.setText(Text.TOTAL.get());
        backBtn.setText(Text.BACK.get());
        otherFineAmountTxtF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[\\d.]*")) {
                otherFineAmountTxtF.setText(newValue.replaceAll("[^\\d.]", ""));
            }
        });
    }

    private void stopTimer() {
        table.setFinished();
        totalTimeLab.setText(table.getTxtTime('t'));
        totalTimeLab.setStyle("-fx-text-fill: #ffffff");
        overTimeLab.setText(table.getTxtTime('o'));
        showFineLab.setText(String.format("%.2f", table.calExcessFine()));
        showSCLab.setText(String.format("%.2f", calSC()));
        showTotalLab.setText(String.format("%.2f", calFinalPrice()));
        otherFineAmountTxtF.setDisable(false);
        stopNBillBtn.setText(Text.END.get());
        stopNBillBtn.setStyle("-fx-background-color: #01a1ad");
        stopNBillBtn.setOnMouseClicked(e -> endTable());
        Label[] labs = {overTimeLab, showFineLab, showSCLab, showTotalLab};
        for (Label lab: labs) {
            lab.setVisible(true);
            FadeTransition ft = new FadeTransition(Duration.millis(150), lab);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
        }
        table.setTotalPrice(calFinalPrice());
        table.toLog();
    }

    private void endTable() {
        table.setFinished();
        if (!SettingManager.i().isLimitTime()) {
            table.setTotalPrice(calFinalPrice());
            table.toLog();
        }
        TableManager.i().delTableActive(table.getId());
        goBack();
    }

    private double calSC() {
        return calPrice() * (SettingManager.i().getServiceCharge() / 100);
    }

    private double calPrice() {
        if (SettingManager.i().isLimitTime())
            return table.calAdultsPrice() + table.calKidsPrice() + table.calExcessFine();
        else
            return table.calAdultsPrice() + table.calKidsPrice();
    }

    private double calFinalPrice() {
        try {
            return calSC() + calPrice() + Double.parseDouble(otherFineAmountTxtF.getText());
        }
        catch (Exception e) {
            return calSC() + calPrice();
        }
    }

    private void amountChange() {
        showTotalLab.setText(String.format("%.2f", calFinalPrice()));
    }

    private void goBack() {
        ((Stage) backBtn.getScene().getWindow()).close();
    }
}
