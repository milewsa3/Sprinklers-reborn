package jimp.model;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogEvent;
import javafx.scene.image.Image;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import jimp.Main;
import jimp.controllers.GardenBox;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ParallelCalculation implements Runnable{
    private GardenBox gb;
    private int cycles;
    private int cycleTime;
    private String filename;


    public ParallelCalculation(GardenBox gb, int cycles, int cycleTime, String filename){
        this.gb = gb;
        this.cycles = cycles;
        this.cycleTime = cycleTime;
        this.filename = filename;
    }


    @Override
    public void run(){
        System.out.println("[ParallelCalculation] GardenBox.x = " + gb.getX() + " GardenBox.y = " + gb.getY() + " Cycles = " + this.cycles + " CycleTime = " + this.cycleTime);
        GardenGrass gg = new GardenGrass(this.gb);
        Population pop = new Population(gg);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //Tutaj odblokowanie GUI, bo juz po obliczeniu to teraz animacja juz na czysto
                //ublock lalala

                pop.drawPopulation(gb);
                gb.startAnimation(cycles, cycleTime);

                try{
                    PrintWriter out = new PrintWriter(filename + "_statystyki.txt");
                    out.println(pop.printSprinklers());
                    out.close();
                }catch (FileNotFoundException e){
                    System.out.println(pop.printSprinklers());
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Calculation finished!");
                alert.setHeaderText("Calculation finished!");
                alert.setTitle("Calculation finished!");
                alert.show();

                alert.setOnCloseRequest(new EventHandler<DialogEvent>() {
                    @Override
                    public void handle(DialogEvent event) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Image generated in resources folder");
                        alert.setHeaderText("Image handling");
                        alert.setTitle("Information");
                        alert.show();

                        saveImg();
                    }
                });
            }
        });

        Main.nThreads--;
        saveImg();
    }

    private void saveImg() {
        Platform.runLater(() -> {
            Canvas canvas = createCnvsBasedOnGb();

            try {
                Image snapshot = canvas.snapshot(null,null);

                ImageIO.write(SwingFXUtils.fromFXImage(snapshot,null),"png",new File("src/main/resources/jimp/" + filename +".png"));
            } catch(IOException exc) {
                System.out.println("Failed to save image: " + exc);
            }
        });
    }

    private Canvas createCnvsBasedOnGb() {
        Canvas canvas = new Canvas(gb.getX(),gb.getY()); //nieskalowane rozmiary
        GraphicsContext gc = canvas.getGraphicsContext2D();
        initGc(gc);
        int skala = gb.getSKALA();


        List<Shape> sprinklers = gb.getSprinklers();
        for(Shape s : sprinklers) {
            Arc arc = (Arc)s;
            gc.setFill(gb.getSprinklerColor());
            double radiusX = arc.getRadiusX() * skala * 2;
            double radiusY = arc.getRadiusY() * skala * 2;
            double centerX = arc.getCenterX() * skala - radiusX/2;
            double centerY = arc.getCenterY() * skala - radiusY/2;
            double startAngle = arc.getStartAngle();
            double length = arc.getLength();
            gc.fillArc((centerX)
                    ,centerY
                    ,radiusX
                    ,radiusY
                    ,startAngle
                    ,length
                    ,arc.getType());
        }

        List<Rectangle> obstacles = gb.getObstacles();
        for(Rectangle r : obstacles) {
            //System.out.println("X,Y: " + r.getX() + " " + r.getY());
            //System.out.println("WID, HEI: " + r.getWidth() + " " + r.getHeight());
            gc.setFill(gb.getObstacleColor());
            gc.fillRect(r.getX()*skala,r.getY()*skala
                    ,r.getWidth()*skala,r.getHeight()*skala);
        }



        return canvas;
    }

    private void initGc(GraphicsContext gc) {
        gc.setFill(gb.getBgColor());
        gc.fillRect(0,0,gb.getX(),gb.getY());
    }


}
