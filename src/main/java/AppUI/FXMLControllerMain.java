package AppUI;

import AppModel.Table;
import AppModel.TableActive;
import AppModel.TableBooking;
import AppService.TableManager;
import AppUtil.Text;
import JfxApplication.SceneLoader;
import com.jfoenix.controls.JFXButton;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;


public class FXMLControllerMain implements Initializable {
    @FXML
    private Label mainLab;
    @FXML
    private Label activeLab;
    @FXML
    private Label bookLab;
    @FXML
    private FlowPane activeFlow;
    @FXML
    private FlowPane bookFlow;
    @FXML
    private JFXButton newtableBtn;
    @FXML
    private JFXButton newbookBtn;
    @FXML
    private JFXButton settingBtn;
    @FXML
    private AnchorPane apPane;
    @FXML
    private StackPane spPane;
    @FXML
    private VBox aVbox;
    @FXML
    private VBox bVbox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainLab.setText(Text.MAIN.get());
        activeLab.setText(Text.ACTIVE.get());
        bookLab.setText(Text.BOOKING.get());
        TableManager.i().getFile();
        newbookBtn.setStyle("-fx-font-family:  fontello;");
        newtableBtn.setStyle("-fx-font-family:  fontello;");
        settingBtn.setStyle("-fx-font-family:  fontello;");
        reloadActive();
        reloadBooked();
        spPane.widthProperty().addListener(e -> {
            double w = spPane.getWidth() - 1000;
            apPane.setPrefWidth(20 + w / 5);
            aVbox.setMinWidth(505 + w / 2.5);
            aVbox.setMaxWidth(500 + w / 2.5);
            bVbox.setMinWidth(505 + w / 2.5);
            bVbox.setMaxWidth(500 + w / 2.5);
            activeFlow.setMinWidth(505 + w / 2.5);
            activeFlow.setMaxWidth(500 + w / 2.5);
            bookFlow.setMinWidth(505 + w / 2.5);
            bookFlow.setMaxWidth(510 + w / 2.5);
        });
    }

    @FXML
    void addActive() {
        Stage stage = new Stage();
        Stage currStage = (Stage) newtableBtn.getScene().getWindow();
        stage.initOwner(currStage);
        SceneLoader.Load(stage, "tCreateTableScene.fxml", false);
        stage.setTitle("Create");
        stage.setOnHiding(e -> reloadActive());
        stage.show();
    }

    @FXML
    void toSetting() {
        Stage stage = (Stage) newtableBtn.getScene().getWindow();
        SceneLoader.Load(stage, "settingScene.fxml", true, "styles.css", newtableBtn.getScene().getWidth(), newtableBtn.getScene().getHeight());
        stage.show();
    }

    private void reloadActive() {
        activeFlow.getChildren().clear();
        int i = 0;
        for (Map.Entry<Integer, TableActive> table : TableManager.i().getTableActives().entrySet()) {
            createActiveTable(table.getKey(), i);
            i++;
        }
    }

    private void createActiveTable(int id, int index) {
        JFXButton btn = createTableBtn(id, activeFlow, "#0277bd");
        btn.setOpacity(0);
        btn.setOnMouseClicked(e -> openCheckBill(id));
        FadeTransition ft = new FadeTransition(Duration.millis(500), btn);
        ft.setDelay(Duration.millis(100 * index));
        ft.setFromValue(0);
        ft.setToValue(1);

        ft.play();
    }

    private void openCheckBill(int id) {
        Stage stage = new Stage();
        Stage currStage = (Stage) newtableBtn.getScene().getWindow();
        stage.initOwner(currStage);
        FXMLControllerCheckBill cont = new FXMLControllerCheckBill(id);
        SceneLoader.Load(stage, "tCheckbillScene.fxml", false, "Com-text.css", cont);
        stage.setTitle("Info");
        stage.setOnHiding(e -> reloadActive());
        stage.show();
    }

    @FXML
    void addBooking() {
        Stage stage = new Stage();
        Stage currStage = (Stage) newbookBtn.getScene().getWindow();
        stage.initOwner(currStage);
        SceneLoader.Load(stage, "tBookingScene.fxml", false);
        stage.setTitle("Booking");
        stage.setOnHiding(e -> reloadBooked());
        stage.show();
    }

    private void createBookTable(int id, int index) {
        JFXButton btn = createTableBtn(id, bookFlow, "#00695c");
        btn.setOpacity(0);
        btn.setOnMouseClicked(e -> openBooked(id));
        FadeTransition ft = new FadeTransition(Duration.millis(500), btn);
        ft.setDelay(Duration.millis(100 * index));
        ft.setFromValue(0);
        ft.setToValue(1);

        ft.play();
    }

    private void openBooked(int id) {
        Stage stage = new Stage();
        Stage currStage = (Stage) newbookBtn.getScene().getWindow();
        stage.initOwner(currStage);
        FXMLControllerBooked cont = new FXMLControllerBooked(id);
        SceneLoader.Load(stage, "tBookedScene.fxml", false, "Com-text.css", cont);
        stage.setTitle("Info");
        stage.setOnHiding(e -> {
            reloadBooked();
            reloadActive();
        });
        stage.show();
    }

    private void reloadBooked() {
        bookFlow.getChildren().clear();
        int i = 0;
        for (Map.Entry<Integer, TableBooking> table : TableManager.i().getTableBookings().entrySet()) {
            createBookTable(table.getKey(), i);
            i++;
        }
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
        Table tab = TableManager.i().findById(id);
        tab.setTimeLab(timeSpLab);
        timeSpLab.setStyle("-fx-font-size: 10px;" +
                "-fx-text-fill: #ffffff;" +
                "-fx-text-alignment: center");

        VBox theBox = new VBox(nameLab, timeSpLab);
        theBox.setPrefSize(80, 80);
        theBox.setAlignment(Pos.TOP_CENTER);
        VBox.setMargin(nameLab, new Insets(10, 0, 0, 0));
        VBox.setMargin(timeSpLab, new Insets(5, 0, 0, 0));

        JFXButton tableBtn = new JFXButton();
        tableBtn.setGraphic(theBox);
        tableBtn.setPrefSize(80, 80);
        tableBtn.setAlignment(Pos.CENTER_LEFT);
        tableBtn.setText("");
        if (tab.getTime() < 0) {
            Color vColor = new Color(198.0 / 255.0, 40.0 / 255.0, 40.0 / 255.0, 1);
            tableBtn.setStyle("-fx-background-color:" + mixRGB(Color.web(btnColor), vColor));
        }
        else {
            tableBtn.setStyle("-fx-background-color:" + btnColor);
        }
        FlowPane.setMargin(tableBtn, new Insets(20, 20, 0, 0));
        nodes.add(tableBtn);
        Animation animation = new Transition() {

            {
                setCycleDuration(Duration.millis(700));
                setInterpolator(Interpolator.EASE_IN);
            }

            @Override
            protected void interpolate(double frac) {
                Color vColor = new Color(198.0 / 255.0, 40.0 / 255.0, 40.0 / 255.0, frac);
                tableBtn.setStyle("-fx-background-color:" + mixRGB(Color.web(btnColor), vColor));
            }
        };
        timeSpLab.textProperty().addListener(e -> {
            if (tab.getTime() == 0) {
                animation.play();
            }
            else if (tab.getTime() < 0) {
                Color vColor = new Color(198.0 / 255.0, 40.0 / 255.0, 40.0 / 255.0, 1);
                tableBtn.setStyle("-fx-background-color:" + mixRGB(Color.web(btnColor), vColor));
            }
        });
        return tableBtn;
    }

    private String mixRGB(Color currColor, Color colorNext) {
        int red = (int) (((currColor.getRed() * (1 - colorNext.getOpacity())) + (colorNext.getRed() * colorNext.getOpacity())) * 255);
        int green = (int) (((currColor.getGreen() * (1 - colorNext.getOpacity())) + (colorNext.getGreen() * colorNext.getOpacity())) * 255);
        int blue = (int) (((currColor.getBlue() * (1 - colorNext.getOpacity())) + (colorNext.getBlue() * colorNext.getOpacity())) * 255);
        return String.format("#%02X%02X%02X", red, green, blue);
    }
}
