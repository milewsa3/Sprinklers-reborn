package jimp.controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import jimp.Main;
import jimp.animations.ScalingAnimation;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private boolean isInHeadbar;

    public boolean isInHeadbar() {
        return isInHeadbar;
    }

    @FXML
    private BorderPane rootPane;

    @FXML
    private Label titleLabel;

    @FXML
    private ImageView minimizeBt;

    @FXML
    private ImageView exitBt;

    @FXML
    void exit(MouseEvent event) {
        Platform.exit();
    }

    @FXML
    private VBox headbar;

    @FXML
    private TextField xTextField;

    @FXML
    private TextField yTextField;

    @FXML
    private TextField cycleTimeField;

    @FXML
    private CheckBox odbicieCheck;

    @FXML
    private Button changeSizeButton;

    @FXML
    private Button startButton;

    @FXML
    private Slider cycleSlider;

    @FXML
    void startAction(ActionEvent event) {
        boolean isOk = true;
        Main.cycles = (int)cycleSlider.getValue();
        if(cycleTimeField.getText().equals("")){
            Main.cycleTime = 1;
        }
        else {
            try {
                int cT = Integer.parseInt(cycleTimeField.getText());
                if(cT < 0)
                    throw new NumberFormatException("The number has to be greater then 0");
                Main.cycleTime = cT;
            }catch (NumberFormatException e){
                isOk = false;
                Alert alert = new Alert(Alert.AlertType.ERROR, "You have to provide number!");
                alert.setHeaderText("Wrong number provided!");
                alert.setTitle("Number format exception!");
                alert.show();
            }
        }

        if(isOk)
            Main.startCalculations();

    }

    @FXML
    void odbicieKlik(ActionEvent event) {
        Main.reflect = !Main.reflect;
    }

    @FXML
    void headbarEntered(MouseEvent event){
        Main.setInHeadbar(true);
    }

    @FXML
    void headbarExited(MouseEvent event){
        Main.setInHeadbar(false);
    }

    @FXML
    void changeSizeGardenBox(ActionEvent event) throws NumberFormatException{
        try {
            Integer x = Integer.parseInt(xTextField.getText());
            Integer y = Integer.parseInt(yTextField.getText());

            if (x > 0 && x < 8001 && y > 0 && y < 4001)
                Main.changeGardenBox(x,y);
            else
                throw new NumberFormatException("Wrong size");
        }
        catch(NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "You have to provide number!");
            alert.setHeaderText("Wrong number provided!");
            alert.setTitle("Number format exception!");
            alert.show();
            //e.printStackTrace();
        }
    }
    @FXML
    void minimize(MouseEvent event) {
            Stage stage = (Stage)rootPane.getScene().getWindow();
            stage.setIconified(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ScalingAnimation labelScaling = new ScalingAnimation(titleLabel);
        labelScaling.start();
    }
}
