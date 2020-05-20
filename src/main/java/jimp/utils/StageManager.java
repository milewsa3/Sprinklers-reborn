package jimp.utils;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jimp.Main;

import java.io.IOException;

public class StageManager {
    private Stage primaryStage;
    private Scene currScene;

    private double xOffset = 0;
    private double yOffset = 0;


    public StageManager(Stage primaryStage, String initFxmlName) {
        this.primaryStage = primaryStage;
        Parent root = loadFxml(initFxmlName);
        currScene = new Scene(root);
        makeBorderless(primaryStage);
        primaryStage.setScene(currScene);
    }

    private Parent loadFxml(String name) {
        try {
            return FXMLLoader.load(Main.class.getResource("fxml/"+ name));
        } catch (IOException e) {
            System.err.println("Failed init fxml");
            e.printStackTrace();
        }
        return null;
    }

    private void makeBorderless(Stage stage) {
        stage.initStyle(StageStyle.UNDECORATED);
    }

    public void makeMovement(Node root) {
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() - xOffset);
                primaryStage.setY(event.getScreenY() - yOffset);
            }
        });
    }
}
