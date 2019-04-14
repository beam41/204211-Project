package AppUI;

import AppModel.Table;
import AppModel.TableActive;
import AppModel.TableBooking;
import AppService.SettingManager;
import AppService.TableManager;
import JfxApplication.SceneLoader;
import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.ResourceBundle;


public class FXMLControllerMain implements Initializable {
    @FXML
    Label mainLab;
    @FXML
    Label activeLab;
    @FXML
    Label bookLab;
    @FXML
    FlowPane activeFlow;
    @FXML
    FlowPane bookFlow;
    @FXML
    JFXButton newtableBtn;
    @FXML
    JFXButton newbookBtn;
    @FXML
    JFXButton settingBtn;

    static ArrayList<TableActive> copyActives = new ArrayList<>();
    static ArrayList<TableBooking> copyBookings = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        newbookBtn.setStyle(" -fx-font-family:  fontello;");
        newtableBtn.setStyle(" -fx-font-family:  fontello;");
        settingBtn.setStyle(" -fx-font-family:  fontello;");
        reloadActive();

    }

    @FXML
    void addActive() {
        Stage stage = new Stage();
        Stage currStage = (Stage) newtableBtn.getScene().getWindow();
        stage.initOwner(currStage);
        SceneLoader loader = new SceneLoader();
        loader.Load(stage, "tCreateTableScene.fxml", false, "Com-text.css");
        stage.setTitle("Create");
        stage.setOnHiding(e -> reloadActive());
        stage.show();
    }

    @FXML
    void toSetting() {
        Stage stage = (Stage) newtableBtn.getScene().getWindow();
        SceneLoader loader = new SceneLoader();
        loader.Load(stage, "settingScene.fxml", true, "styles.css", newtableBtn.getScene().getWidth(), newtableBtn.getScene().getHeight());
        stage.show();
    }

    void reloadActive() {
        activeFlow.getChildren().clear();
        copyActives.clear();
        for (Map.Entry<Integer, TableActive> table: TableManager.i().getTableActives().entrySet()) {
            copyActives.add(0, table.getValue());
        }
        for (TableActive table: copyActives) {
            createActiveTable(table.getId());
        }
    }

    private void createActiveTable(int id) {
        createTableBtn(id, activeFlow, "#9f2424").setOnMouseClicked(e -> openCheckBill(id));
    }

    private void openCheckBill(int id) {
        Stage stage = new Stage();
        Stage currStage = (Stage) newtableBtn.getScene().getWindow();
        stage.initOwner(currStage);
        SceneLoader loader = new SceneLoader();
        FXMLControllerCheckBill cont= new FXMLControllerCheckBill(id);
        loader.Load(stage, "tCheckbillScene.fxml", false, "Com-text.css", cont);
        stage.setTitle("Info");
        stage.setOnHiding(e -> reloadActive());
        stage.show();
    }

    private void createBookTable(int id) {
        //todo
    }

    private JFXButton createTableBtn(int id, FlowPane flowPane, String btnColor) {
        ObservableList<Node> nodes = flowPane.getChildren();

        Label nameLab = new Label(String.valueOf(TableManager.i().findById(id).getTableNum()));
        nameLab.setStyle("-fx-font-size: 18px;" +
                "-fx-text-fill: #ffffff");

        Label timeSpLab;
        try {
            timeSpLab = new Label(TableManager.i().findById(id).getTimeLab().getText());
        }
        catch (Exception e) {
            timeSpLab = new Label();
        }
        TableManager.i().findById(id).setTimeLab(timeSpLab);

        timeSpLab.setStyle("-fx-font-size: 10px;" +
                "-fx-text-fill: #ffffff");

        VBox theBox = new VBox(nameLab, timeSpLab);
        theBox.setPrefSize(80, 80);
        theBox.setAlignment(Pos.TOP_CENTER);
        VBox.setMargin(nameLab, new Insets(10, 0, 0, 0));
        VBox.setMargin(timeSpLab, new Insets(10, 0, 0, 0));

        JFXButton tableBtn = new JFXButton();
        tableBtn.setGraphic(theBox);
        tableBtn.setPrefSize(80, 80);
        tableBtn.setAlignment(Pos.CENTER_LEFT);
        tableBtn.setText("");
        tableBtn.setStyle("-fx-background-color:" + btnColor);
        FlowPane.setMargin(tableBtn, new Insets(20, 20, 0, 0));
        nodes.add(tableBtn);
        return tableBtn;
    }

    public static void clockUpdate() {
        for (TableActive table : copyActives) {
            table.updateTime();
        }
        for (TableBooking table : copyBookings) {
            table.updateTime();
        }
    }

}
