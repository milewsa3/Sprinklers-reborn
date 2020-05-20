package jimp.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import jimp.Main;
import jimp.model.GardenBox;

import java.net.URL;
import java.util.ResourceBundle;


public class MainController implements Initializable {
    private GardenBox gardenBox;

    @FXML
    private BorderPane rootPane;

    @FXML
    private HBox topPane;

    @FXML
    private Label jimpLabel;

    @FXML
    private ImageView minimizeBt;

    @FXML
    private ImageView exitBt;

    @FXML
    private Slider cycleSlider;

    @FXML
    private TextField xTextField;

    @FXML
    private TextField yTextField;

    @FXML
    private Button changeSizeButton;

    @FXML
    private Button startButton;

    @FXML
    private Canvas canvas;

    @FXML
    private Label mousePosLabel;

    @FXML
    private Label alertLabel;

    @FXML
    void changeSizeGardenBox(ActionEvent event) {
        try {
            Integer x = Integer.parseInt(xTextField.getText());
            Integer y = Integer.parseInt(yTextField.getText());

            if (x > 0 && x <= 8000 && y > 0 && y <= 4000) {
                System.out.println("Change garden size: " +
                        x + ", " + y);
                alertLabel.setVisible(false);
            }
            else {
                alertLabel.setText("Wrong size");
                alertLabel.setVisible(true);
            }
        }
        catch(NumberFormatException e){
            alertLabel.setText("Wrong size");
            alertLabel.setVisible(true);
        }
    }

    @FXML
    void exit(MouseEvent event) {
        Platform.exit();
    }

    @FXML
    void minimize(MouseEvent event) {
        Stage stage = (Stage)rootPane.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void startAction(ActionEvent event) {
        System.out.println("Calculactions started");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //A piece of code that make window dragabble
        Platform.runLater(() -> Main.stageManager.makeMovement(topPane));

        alertLabel.setVisible(false);
        gardenBox = new GardenBox(canvas,mousePosLabel,alertLabel);
    }
}
