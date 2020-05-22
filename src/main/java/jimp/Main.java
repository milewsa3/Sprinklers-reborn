package jimp;

import javafx.application.Application;
import javafx.stage.Stage;
import jimp.model.GardenBox;
import jimp.utils.StageManager;

import java.util.ArrayList;

public class Main extends Application {
    public static StageManager stageManager;
    public GardenBox gardenBox;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        stageManager = new StageManager(primaryStage,"main.fxml");
        primaryStage.setTitle("Sprinklers");
        primaryStage.show();

    }


    public static void startCalculations(GardenBox gardenBox) {
        System.out.println("qqq");
        gardenBox.drawRectangle();
        gardenBox.onSave("obrazek");
    }
}
