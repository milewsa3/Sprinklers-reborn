package jimp;

import javafx.application.Application;
import javafx.stage.Stage;
import jimp.utils.StageManager;

public class Main extends Application {
    public static StageManager stageManager;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        stageManager = new StageManager(primaryStage,"main.fxml");
        primaryStage.setTitle("Sprinklers");
        primaryStage.show();
    }
}
