package jimp.model;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.effect.Light;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GardenBox {
    private final double SCALE = 0.1;
    private final int SKALA = 10;
    private final int INIT_WIDTH = 8000;
    private final int INIT_HEIGHT = 4000;
    private final String GRASS_COLOR = "#1bc904";
    private final String OBSTACLE_COLOR = "#424242";

    private final Canvas canvas;
    private GraphicsContext graphicsContext;
    private final Label mousePosLabel;
    private final Label alertLabel;
    private List<Rectangle> obstacleList;

    public GardenBox(Canvas canvas, Label mousePosLabel,Label alertLabel) {
        this.canvas = canvas;
        initCanvas(canvas);

        graphicsContext = canvas.getGraphicsContext2D();
        initGraphicalContext(graphicsContext);

        initObstacleDrawing();

        initMonitoredLabel(canvas,mousePosLabel);

        this.mousePosLabel = mousePosLabel;
        this.alertLabel = alertLabel;
        obstacleList = new ArrayList<>();
    }

    private void initObstacleDrawing() {
        final Light.Point anchor = new Light.Point();
        final Rectangle selection = new Rectangle();

        canvas.setOnMousePressed(event -> {
            anchor.setX(event.getX());
            anchor.setY(event.getY());
            selection.setX(event.getX());
            selection.setY(event.getY());
            selection.setFill(null); // transparent
            selection.setStroke(Color.BLACK); // border
            selection.getStrokeDashArray().add(10.0);
            //((Pane)gardenRegion).getChildren().add(selection);
        });

        canvas.setOnMouseDragged(event -> {
            selection.setWidth(Math.abs(event.getX() - anchor.getX()));
            selection.setHeight(Math.abs(event.getY() - anchor.getY()));
            selection.setX(Math.min(anchor.getX(), event.getX()));
            selection.setY(Math.min(anchor.getY(), event.getY()));
        });

        canvas.setOnMouseReleased(event -> {
            if(event.getButton() == MouseButton.PRIMARY) {
                //drawObstacle(selection);
                System.out.println("DRAWING OBSTACLE");
            }
            else if(event.getButton() == MouseButton.SECONDARY) {
                //removeObstacle(selection);
                System.out.println("REMOVING OBSTACLE");
            }
        });
    }

    private void initCanvas(Canvas canvas) {
        //do zrobienia
        canvas.setWidth(INIT_WIDTH*SCALE);
        canvas.setHeight(INIT_HEIGHT*SCALE);

        //applay clipping
//        final Rectangle outputClip = new Rectangle();
//
//        canvas.setClip(outputClip);
//
//        canvas.layoutBoundsProperty().addListener((ov, oldValue, newValue) -> {
//            outputClip.setWidth(newValue.getWidth());
//            outputClip.setHeight(newValue.getHeight());
//        });
    }

    private void initGraphicalContext(GraphicsContext graphicsContext) {
        //do zrobienia
        graphicsContext.setFill(Color.web(GRASS_COLOR));
        graphicsContext.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
    }

    private void initMonitoredLabel(Node monitored, Label reporter) {


        monitored.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                String msg =
                        "(x: "       + (int)event.getX()      + ", y: "       + (int)event.getY()       + ")" ;

                reporter.setText(msg);
            }
        });

        monitored.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                //reporter.setText(OUTSIDE_TEXT);
            }
        });
    }

    public void drawObstacle() {
    }

    public void removeObstacle() {
    }

    static  int ktory = 0;

    public void drawRectangle(){
        GraphicsContext gc = graphicsContext;
        if(ktory==0) {
            Rectangle rect = new Rectangle(100, 200, 200, 150);
            gc.setFill(Color.WHITESMOKE);
            gc.fillRect(rect.getX(),
                    rect.getY(),
                    rect.getWidth(),
                    rect.getHeight());
            gc.setFill(Color.GREEN);
            gc.setStroke(Color.BLUE);
            ktory++;
            System.out.println("siema");
        }
        else{
            for(int i=0;i<255;i+=10) {
                gc.setFill(Color.GREEN);
                gc.fillRect(0, 0, 800, 400);
                Rectangle rect = new Rectangle(333, 30, 200, 150);
                gc.setFill(Color.rgb(255,i,255));
                gc.fillRect(rect.getX(),
                        rect.getY(),
                        rect.getWidth(),
                        rect.getHeight());
                gc.setFill(Color.GREEN);
                gc.setStroke(Color.BLUE);
                System.out.println("siewsadama");
                try {
                    Thread.sleep(100);
                }
                catch (Exception e){

                }
            }
        }
    }

    public void onSave(String fileName) {
        try {
            Image snapshot = canvas.snapshot(null,null);

            ImageIO.write(SwingFXUtils.fromFXImage(snapshot,null),"png",new File("src/main/resources/jimp/" + fileName +".png"));
        } catch(IOException exc) {
            System.out.println("Failed to save image: " + exc);
        }
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public GraphicsContext getGraphicsContext() {
        return graphicsContext;
    }

    public List<Rectangle> getObstacleList() {
        return obstacleList;
    }
}
