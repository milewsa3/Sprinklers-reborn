package jimp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jimp.controllers.GardenBox;
import jimp.model.*;

import javax.swing.*;
import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Main extends Application{
    //ogarnianie borderless
    private double xOffset = 0;
    private double yOffset = 0;
    private static boolean isInHeadbar;

    private static Parent root;
    private static GardenBox gardenBox;

    //odbicia wody
    public static boolean reflect = false;
    //ile ma być "klatek" w animacji zraszaczy
    public static int cycles;
    //odstępy w milisekundach
    public static int cycleTime;

    public static void main(String[] args) {
        launch(args);
    }

    public static void setInHeadbar(boolean inHeadbar) {
        isInHeadbar = inHeadbar;
    }

    public static void changeGardenBox(int x, int y)
    {
        gardenBox = new GardenBox(x,y);
        ((BorderPane) root).setBottom(gardenBox);
    }
    @Override
    public void start(Stage primaryStage) throws IOException {

        root = FXMLLoader.load(getClass().getResource("fxml/main.fxml"));
        gardenBox = new GardenBox(8000,4000);

        ((BorderPane) root).setBottom(gardenBox);
        Scene scene = new Scene(root);

        makeBorderless(primaryStage,root);

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    //magia ktora sie dzieje po wcisnieciu przycisk start w UI
    public static void startCalculations() {
        //checkParameters(gardenBox, cycles, cycleTime)
        String filename = "sprinkler_stats.txt"; //maybe from GUI
        ParallelCalculation pc = new ParallelCalculation(gardenBox, cycles, cycleTime, filename);
        Thread t = new Thread(pc);

        //Blokada GUI i pokazanie krecacego koleczka czy cos
        //Blabla
        //buttonblock


        t.start(); //Odblokowanie GUI tutaj (w srodku), moze trzeba przekazac cos do Paralela jeszcze, nwm


    }


    //metoda odpowiedzialna za brak ramki
    private void makeBorderless(Stage stage,Parent root) {
        stage.initStyle(StageStyle.UNDECORATED);

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
                if(isInHeadbar) {
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
            }
        });
    }
}