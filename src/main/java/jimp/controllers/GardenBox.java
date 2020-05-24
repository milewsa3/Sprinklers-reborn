package jimp.controllers;

import javafx.animation.FillTransition;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.effect.Light;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import jimp.model.Angle;
import jimp.model.Direction;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GardenBox extends VBox{

    final int SKALA = 10;

    private Label reporter;
    private Region gardenRegion;
    private StackPane output;
    //Lista na rysowane zraszacze
    private List<Shape> sprinklers;
    //Lista na rysowane przeszkody
    private List<Rectangle> obstacles;

    private int x;
    private int y;

    String OUTSIDE_TEXT = "";

    public GardenBox(int x, int y) {
        this.x =x;
        this.y =y;
        reporter = new Label(OUTSIDE_TEXT);
        setPadding(new Insets(0, 0, 50, 0));
        sprinklers = new ArrayList<>();
        obstacles = new ArrayList<>();
        gardenRegion = createClipped(x/SKALA,y/SKALA);
        output = new StackPane(gardenRegion);
        createMonitoredLabel(gardenRegion,reporter);
        getChildren().setAll(output,reporter);
        final Rectangle selection = new Rectangle();
        final Light.Point anchor = new Light.Point();

        ((Pane)gardenRegion).setOnMousePressed(event -> {
            anchor.setX(event.getX());
            anchor.setY(event.getY());
            selection.setX(event.getX());
            selection.setY(event.getY());
            selection.setFill(null); // transparent
            selection.setStroke(Color.BLACK); // border
            selection.getStrokeDashArray().add(10.0);
        });

        ((Pane)gardenRegion).setOnMouseDragged(event -> {
            selection.setWidth(Math.abs(event.getX() - anchor.getX()));
            selection.setHeight(Math.abs(event.getY() - anchor.getY()));
            selection.setX(Math.min(anchor.getX(), event.getX()));
            selection.setY(Math.min(anchor.getY(), event.getY()));
        });

        ((Pane)gardenRegion).setOnMouseReleased(event -> {
            // Do what you want with selection's properties here
            Rectangle obstacleRect = new Rectangle(selection.getX(), selection.getY(), selection.getWidth(), selection.getHeight());
            obstacleRect.setFill(Color.rgb(0,155,255,1));
            ((Pane)gardenRegion).getChildren().add(obstacleRect);
            obstacles.add(obstacleRect);
            ((Pane)gardenRegion).getChildren().remove(selection);
            selection.setWidth(0);
            selection.setHeight(0);
        });
    }

    //czysci zraszacze z gardenBox
    public void usun(){
        for(Shape shape: sprinklers)
        ((Pane)gardenRegion).getChildren().remove(shape);
    }

    public void addSprinklerShape(Angle angle, Direction direction, int x, int y) {
        Arc shape = new Arc();
        shape.setFill(Color.rgb(0,255,0,1));
        shape.setType(ArcType.ROUND);
        double offset = 0f;
        switch (direction){
            case NORTH:
                offset=0f;
                break;
            case EAST:
                offset=90f;
                break;
            case SOUTH:
                offset=180f;
                break;
            case WEST:
                offset=270f;
                break;
        }
        switch (angle){
            case ANGLE_90:
                switch (direction){
                    case NORTH:
                        offset=0f;
                        break;
                    case EAST:
                        offset=270f;
                        break;
                    case SOUTH:
                        offset=180f;
                        break;
                    case WEST:
                        offset=90f;
                        break;
                }
                shape.setCenterY(y/SKALA);
                shape.setCenterX(x/SKALA);
                shape.setRadiusX(50.0f);
                shape.setRadiusY(50.0f);
                shape.setLength(90.0f);
                shape.setStartAngle(offset);
                break;
            case ANGLE_180:
                shape.setCenterY(y/10);
                shape.setCenterX(x/10);
                shape.setRadiusX(40.0f);
                shape.setRadiusY(40.0f);
                shape.setLength(180.0f);
                shape.setStartAngle(offset);
                break;
            case ANGLE_270:
                switch (direction){
                    case NORTH:
                        offset=270f;
                        break;
                    case EAST:
                        offset=180f;
                        break;
                    case SOUTH:
                        offset=90f;
                        break;
                    case WEST:
                        offset=0f;
                        break;
                }
                shape.setCenterY(y/SKALA);
                shape.setCenterX(x/SKALA);
                shape.setRadiusX(30.0f);
                shape.setRadiusY(30.0f);
                shape.setLength(270.0f);
                shape.setStartAngle(offset);
                break;
            case ANGLE_360:
                shape.setCenterY(y/SKALA);
                shape.setCenterX(x/SKALA);
                shape.setRadiusX(20.0f);
                shape.setRadiusY(20.0f);
                shape.setLength(360.0f);
                shape.setStartAngle(offset);
                break;
        }
        shape.toBack();
        ((Pane)gardenRegion).getChildren().add(shape);
        sprinklers.add(shape);
    }
    public void startAnimation(int cycles,int frameTime){
        for(Shape shape : sprinklers)
        {
            shape.toBack();
            FillTransition fill = new FillTransition();
            fill.setCycleCount(1);
            fill.setFromValue(Color.rgb(0,255,100,1));
            fill.setToValue(Color.rgb((int)cycles*255/100,255,(100-cycles),1));
            fill.setDuration(Duration.millis(frameTime*cycles));
            fill.setAutoReverse(false);
            System.out.println(shape);
            fill.setShape(shape);
            fill.play();
        }
    }

    private void clipChildren(Region region) {

        final Rectangle outputClip = new Rectangle();
        /*outputClip.setArcWidth(arc);
        outputClip.setArcHeight(arc);
        */
        region.setClip(outputClip);

        region.layoutBoundsProperty().addListener((ov, oldValue, newValue) -> {
            outputClip.setWidth(newValue.getWidth());
            outputClip.setHeight(newValue.getHeight());
        });
    }

    private void createMonitoredLabel(Region monitored, Label reporter) {


        monitored.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                String msg ="(x: "+ event.getX()+ ", y: " + event.getY() + ")" ;

                reporter.setText(msg);
            }
        });

        monitored.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                reporter.setText(OUTSIDE_TEXT);
            }
        });
    }

    private Region createClipped(int width, int height) {
        final Pane pane = new Pane();
        pane.setBackground(new Background(new BackgroundFill(Color.web("#FF0000"), CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setMaxSize(width,height);
        pane.setMinSize(width,height);
        //pane.setPrefSize(width,height);

        // clipped children still overwrite Border!
        clipChildren(pane);

        return pane;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public List<Rectangle> getObstacles() {
        return obstacles;
    }
}
