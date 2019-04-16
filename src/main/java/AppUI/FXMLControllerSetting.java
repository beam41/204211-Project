package AppUI;

import AppModel.Course;
import AppModel.Price;
import AppService.SettingManager;
import AppService.TableManager;
import AppUtil.Lang;
import AppUtil.Text;
import JfxApplication.SceneLoader;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.animation.FadeTransition;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

import static AppUtil.Converter.sepTime;

public class FXMLControllerSetting implements Initializable {

    @FXML
    private VBox tableBox;
    @FXML
    private Label setLab;
    @FXML
    private Label langLab;
    @FXML
    private Label tableLab;
    @FXML
    private Label timitLab;
    @FXML
    private Label excessLab;
    @FXML
    private Label perLab;
    @FXML
    private Label scLab;
    @FXML
    private Label courseLab;
    @FXML
    private Label priceLab;
    @FXML
    private JFXToggleButton timitTog;
    @FXML
    private JFXComboBox<String> langCob;
    @FXML
    private JFXTextField tableTxtF;
    @FXML
    private JFXTextField thourTxtF;
    @FXML
    private JFXTextField tminTxtF;
    @FXML
    private JFXTextField tsecTxtF;
    @FXML
    private JFXTextField excessTxtF;
    @FXML
    private JFXTextField ehourTxtF;
    @FXML
    private JFXTextField eminTxtF;
    @FXML
    private JFXTextField esecTxtF;
    @FXML
    private JFXTextField scTxtF;
    @FXML
    private Label col1;
    @FXML
    private Label col2;
    @FXML
    private Label col3;
    @FXML
    private Label col4;
    @FXML
    private JFXButton addBtn;
    @FXML
    private JFXButton backBtn;
    @FXML
    private JFXButton saveBtn;
    @FXML
    private Label messageLab;

    // region styles for box
    private String priceBoxStyle = "-fx-text-fill: -fx-primarytext;" +
            "-fx-prompt-text-fill:  #626262;" +
            "-fx-font-size : 14px;" +
            "-jfx-focus-color: #ff8d9c;" +
            "-jfx-unfocus-color: #ff616f;" +
            "-fx-min-height: 32;" +
            "-fx-min-width: 53;" +
            "-fx-max-height: 32;" +
            "-fx-max-width: 53;";

    private String courseBoxStyle = "-fx-text-fill: -fx-primarytext;" +
            "-fx-prompt-text-fill:  #626262;" +
            "-fx-font-size : 14px;" +
            "-jfx-focus-color: #ff8d9c;" +
            "-jfx-unfocus-color: #ff616f;" +
            "-fx-min-height: 32;" +
            "-fx-min-width: 100;" +
            "-fx-max-height: 32;" +
            "-fx-max-width: 100;";
    // endregion

    private ArrayList<Course> courseList = new ArrayList<>();

    private static long strToSecond(String hour, String minute, String second) {
        return (Long.parseLong(hour) * 3600) + (Long.parseLong(minute) * 60) + Long.parseLong(second);
    }

    private static String[] secondToStr(long second) {
        long[] time = sepTime(second);
        return new String[]{String.valueOf(time[0]), String.valueOf(time[1]), String.valueOf(time[2])};
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        langChange();
        langCob.getItems().addAll("ไทย", "English");
        langCob.getSelectionModel().select(SettingManager.i().getLanguage().ordinal());
        // region strict num only
        JFXTextField[] txtFListDouble = {excessTxtF, scTxtF};
        for (JFXTextField txtF : txtFListDouble) {
            txtF.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("[\\d.]*")) {
                    txtF.setText(newValue.replaceAll("[^\\d.]", ""));
                }
            });
        }
        JFXTextField[] txtFListInt = {tableTxtF, thourTxtF, tminTxtF, tsecTxtF, ehourTxtF, eminTxtF, esecTxtF};
        for (JFXTextField txtF : txtFListInt) {
            txtF.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    txtF.setText(newValue.replaceAll("\\D", ""));
                }
            });
        }
        // endregion
        // region get back from setting
        if (SettingManager.i().getTableCount() != -1) {
            tableTxtF.setText(String.valueOf(SettingManager.i().getTableCount()));
        }
        else {
            backBtn.setText(Text.START.get());
            backBtn.setStyle("-fx-background-color:  #00a856");
            backBtn.setVisible(false);
        }

        timitTog.setSelected(SettingManager.i().isLimitTime());
        if (SettingManager.i().isLimitTime()) {
            togOn();
        }
        timeLimitToggle();
        if (SettingManager.i().getServiceCharge() != -1)
            scTxtF.setText(String.valueOf(SettingManager.i().getServiceCharge()));
        for (Map.Entry<String, Price> pm : SettingManager.i().getPriceMap().entrySet()) {
            Course newCourse = addCourseTable();
            newCourse.getCourseTxtF().setText(pm.getValue().getName());
            newCourse.getAdultTxtF().setText(String.valueOf(pm.getValue().getPriceAdult()));
            newCourse.getKidsTxtF().setText(String.valueOf(pm.getValue().getPriceKids()));
        }
        // endregion

    }

    @FXML
    void changeLanguage() {
        switch (langCob.getValue()) {
            case "ไทย":
                SettingManager.i().setLanguage(Lang.Thai);
                break;
            case "English":
                SettingManager.i().setLanguage(Lang.English);
        }
        langChange();

    }

    @FXML
    void saveBtnClick() {
        boolean isReady = true;
        messageLab.setText("[MessageText]");
        messageLab.setStyle("-fx-text-fill: #e53935");
        // region text checking
        JFXTextField[] txtFList = {tableTxtF, scTxtF};
        for (JFXTextField txtF : txtFList) {
            txtF.setStyle("-fx-text-fill: #fff; -fx-prompt-text-fill:  #626262");
            if (txtF.getText().isEmpty()) {
                txtF.setStyle("-fx-background-color: rgba(239, 83, 80,0.3); -fx-text-fill: #fff; -fx-prompt-text-fill:  #626262");
                isReady = false;
                messageLab.setText(Text.MSG_EMPTY.get());
                msgToVisible();
            }
        }
        if (timitTog.isSelected()) {
            JFXTextField[] txtFList2 = {thourTxtF, tminTxtF, tsecTxtF, excessTxtF, ehourTxtF, eminTxtF, esecTxtF};
            for (JFXTextField txtF : txtFList2) {
                txtF.setStyle("-fx-text-fill: #fff; -fx-prompt-text-fill:  #626262");
                if (txtF.getText().isEmpty()) {
                    txtF.setStyle("-fx-background-color: rgba(198,40,40,0.2); -fx-text-fill: #fff; -fx-prompt-text-fill:  #626262");
                    isReady = false;
                    messageLab.setText(Text.MSG_EMPTY.get());
                    msgToVisible();
                }
            }
        }
        addBtn.setStyle("-fx-background-color: #464646; -fx-font-family:  fontello; -fx-font-size: 15px");
        if (courseList.isEmpty()) {
            if (!isReady)
                messageLab.setText(Text.MSG_EMPNOC.get());
            else
                messageLab.setText(Text.MSG_NOC.get());
            msgToVisible();
            isReady = false;
            addBtn.setStyle("-fx-background-color: rgba(198,40,40,0.5); -fx-font-family:  fontello; -fx-font-size: 15px");
        }
        else {
            for (Course course : courseList) {
                course.getCourseTxtF().setStyle(courseBoxStyle + "-fx-background-color: #393939");
                if (course.getCourseTxtF().getText().isBlank()) {
                    course.getCourseTxtF().setStyle(courseBoxStyle + "-fx-background-color: rgba(198,40,40,0.2)");
                    isReady = false;
                    messageLab.setText(Text.MSG_EMPTY.get());
                    msgToVisible();
                }
                course.getAdultTxtF().setStyle(priceBoxStyle + "-fx-background-color: #393939");
                if (course.getAdultTxtF().getText().isEmpty()) {
                    course.getAdultTxtF().setStyle(priceBoxStyle + "-fx-background-color: rgba(198,40,40,0.2)");
                    isReady = false;
                    messageLab.setText(Text.MSG_EMPTY.get());
                    msgToVisible();
                }
                course.getKidsTxtF().setStyle(priceBoxStyle + "-fx-background-color: #393939");
                if (course.getKidsTxtF().getText().isEmpty()) {
                    course.getKidsTxtF().setStyle(priceBoxStyle + "-fx-background-color: rgba(198,40,40,0.2)");
                    isReady = false;
                    messageLab.setText(Text.MSG_EMPTY.get());
                    msgToVisible();
                }
            }
        }
        // endregion

        if (isReady) {
            SettingManager.i().setTableCount(Integer.parseInt(tableTxtF.getText()));
            TableManager.i().reloadTableNum();
            SettingManager.i().setLimitTime(timitTog.isSelected());
            if (timitTog.isSelected()) {
                SettingManager.i().setTimeLimit(strToSecond(thourTxtF.getText(), tminTxtF.getText(), tsecTxtF.getText()));
                SettingManager.i().setExcessFine(Double.parseDouble(excessTxtF.getText()));
                SettingManager.i().setTimePerExcess(strToSecond(ehourTxtF.getText(), eminTxtF.getText(), esecTxtF.getText()));
            }

            SettingManager.i().setServiceCharge(Double.parseDouble(scTxtF.getText()));
            SettingManager.i().clearPrice();
            for (Course course : courseList) {
                SettingManager.i().addPrice(course.getCourseTxtF().getText(),
                        Double.parseDouble(course.getAdultTxtF().getText()),
                        Double.parseDouble(course.getKidsTxtF().getText()));
            }
            backBtn.setVisible(true);
            messageLab.setStyle("-fx-text-fill: #81c784");
            messageLab.setText(Text.MSG_SUCCESS.get());
            msgToVisible();
        }
    }

    @FXML
    void timeLimitToggle() {
        if (timitTog.isSelected()) {
            thourTxtF.setDisable(false);
            tminTxtF.setDisable(false);
            tsecTxtF.setDisable(false);
            excessLab.setDisable(false);
            excessTxtF.setDisable(false);
            perLab.setDisable(false);
            ehourTxtF.setDisable(false);
            eminTxtF.setDisable(false);
            esecTxtF.setDisable(false);
            col1.setDisable(false);
            col2.setDisable(false);
            col3.setDisable(false);
            col4.setDisable(false);
            togOn();
        }
        else {
            thourTxtF.setDisable(true);
            tminTxtF.setDisable(true);
            tsecTxtF.setDisable(true);
            excessLab.setDisable(true);
            excessTxtF.setDisable(true);
            perLab.setDisable(true);
            ehourTxtF.setDisable(true);
            eminTxtF.setDisable(true);
            esecTxtF.setDisable(true);
            col1.setDisable(true);
            col2.setDisable(true);
            col3.setDisable(true);
            col4.setDisable(true);
        }
    }

    private void togOn() {
        if (SettingManager.i().getTimeLimit() != -1) {
            String[] tiLim = secondToStr(SettingManager.i().getTimeLimit());
            thourTxtF.setText(tiLim[0]);
            tminTxtF.setText(tiLim[1]);
            tsecTxtF.setText(tiLim[2]);
        }
        if (SettingManager.i().getExcessFine() != -1)
            excessTxtF.setText(String.valueOf(SettingManager.i().getExcessFine()));
        if (SettingManager.i().getTimePerExcess() != -1) {
            String[] tiEx = secondToStr(SettingManager.i().getTimePerExcess());
            ehourTxtF.setText(tiEx[0]);
            eminTxtF.setText(tiEx[1]);
            esecTxtF.setText(tiEx[2]);
        }
    }

    @FXML
    void getTableCountField() {
        System.out.println("Hi");
        System.out.println(tableTxtF.getText());
    }

    @FXML
    Course addCourseTable() {
        Course newCourse = createCourseTable();
        courseList.add(newCourse);
        return newCourse;
    }

    @FXML
    void goBack() {
        Stage stage = (Stage) backBtn.getScene().getWindow();

        SceneLoader loader = new SceneLoader();
        loader.Load(stage, "mainScene.fxml", true, "styles.css", backBtn.getScene().getWidth(), backBtn.getScene().getHeight());
    }

    private void delCourseTable(Course currBox) {
        ObservableList<Node> nodes = tableBox.getChildren();

        nodes.removeAll(currBox.getKidsTxtF(),
                currBox.getAdultTxtF(),
                currBox.getCourseTxtF(),
                currBox.getDeleteBtn(),
                currBox.getCourseBox());
        courseList.remove(currBox);
        SettingManager.i().delPrice(currBox.getCourseTxtF().getText());
    }

    private void langChange() {
        setLab.setText(Text.SETTING.get());
        langLab.setText(Text.LANGUAGE.get());
        tableLab.setText(Text.TABLE.get());
        timitLab.setText(Text.TIMELIMIT.get());
        excessLab.setText(Text.EXCESSFINE.get());
        perLab.setText(Text.PER.get());
        scLab.setText(Text.SERVICECHARGE.get());
        courseLab.setText(Text.COURSE.get());
        priceLab.setText(Text.PRICE.get());
        tableTxtF.setPromptText(Text.TABLE.get());
        thourTxtF.setPromptText(Text.HOUR.get());
        tminTxtF.setPromptText(Text.MIN.get());
        tsecTxtF.setPromptText(Text.SEC.get());
        excessTxtF.setPromptText(Text.EXCESSFINE.get());
        ehourTxtF.setPromptText(Text.HOUR.get());
        eminTxtF.setPromptText(Text.MIN.get());
        esecTxtF.setPromptText(Text.SEC.get());
        scTxtF.setPromptText(Text.SERVICECHARGE.get());
        backBtn.setText(Text.BACK.get());
        saveBtn.setText(Text.SAVE.get());
    }

    private Course createCourseTable() {

        ObservableList<Node> nodes = tableBox.getChildren();

        JFXTextField courseTxtF = new JFXTextField();
        courseTxtF.setStyle(courseBoxStyle);
        courseTxtF.setPromptText(Text.COURSE.get());
        courseTxtF.setLabelFloat(true);


        JFXTextField adultTxtF = new JFXTextField();
        adultTxtF.setStyle(priceBoxStyle);
        adultTxtF.setPromptText(Text.ADULT.get());
        adultTxtF.setLabelFloat(true);


        JFXTextField kidsTxtF = new JFXTextField();
        kidsTxtF.setStyle(priceBoxStyle);
        kidsTxtF.setPromptText(Text.KID.get());
        kidsTxtF.setLabelFloat(true);

        JFXButton deleteBtn = new JFXButton("\uE801");
        deleteBtn.setStyle("-fx-text-fill: -fx-primarytext;" +
                "-fx-font-size : 14px;" +
                "-fx-font-family: fontello;"
        );

        JFXTextField[] txtFList = {adultTxtF, kidsTxtF};
        for (JFXTextField txtF : txtFList) {
            txtF.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("[\\d.]*")) {
                    txtF.setText(newValue.replaceAll("[^\\d.]", ""));
                }
            });
        }

        HBox courseBox = new HBox(courseTxtF, adultTxtF, kidsTxtF, deleteBtn);
        HBox.setMargin(courseTxtF, new Insets(27, 0, 0, 0));
        HBox.setMargin(adultTxtF, new Insets(27, 0, 0, 50));
        HBox.setMargin(kidsTxtF, new Insets(27, 0, 0, 20));
        HBox.setMargin(deleteBtn, new Insets(27, 0, 0, 20));

        nodes.addAll(courseBox);
        Course self = new Course(courseBox, courseTxtF, adultTxtF, kidsTxtF, deleteBtn);
        deleteBtn.setOnMouseClicked(e -> delCourseTable(self));
        return self;
    }

    private void msgToVisible() {
        messageLab.setOpacity(0);
        messageLab.setVisible(true);
        FadeTransition ft = new FadeTransition(Duration.millis(150), messageLab);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
        FadeTransition ft2 = new FadeTransition(Duration.millis(150), messageLab);
        ft2.setDelay(Duration.seconds(3));
        ft2.setFromValue(1);
        ft2.setToValue(0);
        ft2.play();
        ft2.setOnFinished(e -> messageLab.setVisible(false));
    }
}
