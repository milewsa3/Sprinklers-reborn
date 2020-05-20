package jimp.model;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.effect.Light;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import jimp.model.Direction;
import jimp.model.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class GardenBoxOld extends VBox {

    final int SKALA = 10;

    private final String GRASS_COLOR = "#1bc904";
    private final String OBSTACLE_COLOR = "#424242";
    private final String SPRINKLER_COLOR = "#e3192d";


    private Label reporter;
    private Region gardenRegion;
    private StackPane output;
    private List<Shape> sprinklerList = new LinkedList<>();
    private List<Rectangle> obstacleList = new LinkedList<>();
    private List<Rectangle> removedObstacleList = new LinkedList<>();
    String OUTSIDE_TEXT = "";

    public GardenBoxOld(int x, int y) {
        reporter = new Label(OUTSIDE_TEXT);
        setPadding(new Insets(0, 0, 50, 0));
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
            ((Pane)gardenRegion).getChildren().add(selection);
        });

        ((Pane)gardenRegion).setOnMouseDragged(event -> {
            selection.setWidth(Math.abs(event.getX() - anchor.getX()));
            selection.setHeight(Math.abs(event.getY() - anchor.getY()));
            selection.setX(Math.min(anchor.getX(), event.getX()));
            selection.setY(Math.min(anchor.getY(), event.getY()));
        });

        ((Pane)gardenRegion).setOnMouseReleased(event -> {
            if(event.getButton() == MouseButton.PRIMARY) {
                drawObstacle(selection);
            }
            else if(event.getButton() == MouseButton.SECONDARY) {
                removeObstacle(selection);
            }
        });
    }

    private void drawObstacle(Rectangle selection) {
        Rectangle obstacleRect = new Rectangle(selection.getX(), selection.getY(), selection.getWidth(), selection.getHeight());
        obstacleRect.setFill(Color.web(OBSTACLE_COLOR));
        ((Pane)gardenRegion).getChildren().add(obstacleRect);
        obstacleList.add(obstacleRect);
        ((Pane)gardenRegion).getChildren().remove(selection);
        selection.setWidth(0);
        selection.setHeight(0);
    }

    private void removeObstacle(Rectangle selection) {
        Rectangle obstacleRect = new Rectangle(selection.getX(), selection.getY(), selection.getWidth(), selection.getHeight());
        obstacleRect.setFill(Color.web(GRASS_COLOR));
        ((Pane)gardenRegion).getChildren().add(obstacleRect);
        removedObstacleList.add(obstacleRect);
        ((Pane)gardenRegion).getChildren().remove(selection);
        selection.setWidth(0);
        selection.setHeight(0);
    }


    public void clearSprinklers(){
        ((Pane)gardenRegion).getChildren().clear();
        sprinklerList.clear();
    }
    public void addSprinklerShape(Type angle, Direction direction, int x, int y) {
        Arc shape = new Arc();
        shape.setFill(Color.web(SPRINKLER_COLOR));
        shape.setType(ArcType.ROUND);
        switch (direction){
            case NORTH:
                shape.setStartAngle(0f);
                break;
            case EAST:
                shape.setStartAngle(90f);
                break;
            case SOUTH:
                shape.setStartAngle(180f);
                break;
            case WEST:
                shape.setStartAngle(270f);
                break;
        }
        switch (angle){
            case ANGLE_90:
                shape.setCenterY(y/SKALA);
                shape.setCenterX(x/SKALA);
                shape.setRadiusX(50.0f);
                shape.setRadiusY(50.0f);
                shape.setLength(90.0f);
                break;
            case ANGLE_180:
                shape.setCenterY(y/10);
                shape.setCenterX(x/10);
                shape.setRadiusX(40.0f);
                shape.setRadiusY(40.0f);
                shape.setLength(180.0f);
                break;
            case ANGLE_270:
                shape.setCenterY(y/SKALA);
                shape.setCenterX(x/SKALA);
                shape.setRadiusX(30.0f);
                shape.setRadiusY(30.0f);
                shape.setLength(270.0f);
                break;
            case ANGLE_360:
                shape.setCenterY(y/SKALA);
                shape.setCenterX(x/SKALA);
                shape.setRadiusX(20.0f);
                shape.setRadiusY(20.0f);
                shape.setLength(360.0f);
                break;
        }
        ((Pane)gardenRegion).getChildren().add(shape);
        sprinklerList.add(shape);
    }

    public List<Shape> getSprinklerList() {
        return sprinklerList;
    }

    public List<Rectangle> getObstacleList() {
        return obstacleList;
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
                String msg =
                        "(x: "       + (int)event.getX()      + ", y: "       + (int)event.getY()       + ")" ;

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
        pane.setBackground(new Background(new BackgroundFill(Color.web(GRASS_COLOR), CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setMaxSize(width,height);
        pane.setMinSize(width,height);
        //pane.setPrefSize(width,height);

        // clipped children still overwrite Border!
        clipChildren(pane);

        return pane;
    }

    public Byte[][] getByteGarden() {
        Byte [][] garden = new Byte[(int)gardenRegion.getMaxHeight()][(int)gardenRegion.getMaxWidth()];
        for(int x=0;x<garden.length;x++) {
            Arrays.fill(garden[x], (byte)0);
        }


        obstacleList.forEach(new Consumer<Rectangle>() {
            @Override
            public void accept(Rectangle rectangle) {
                int xFrom = (int)rectangle.getX();
                int yFrom = (int)rectangle.getY();

                int xTo = xFrom + (int)rectangle.getWidth();
                int yTo = yFrom + (int)rectangle.getHeight();

                if((xFrom < 0 && yFrom < 0)) {
                    xFrom = 0;
                    yFrom = 0;
                }
                else if(xTo > (int)gardenRegion.getMaxWidth() && yFrom < 0) {
                    yFrom = 0;
                    xTo = (int)gardenRegion.getMaxWidth();
                }
                else if(xTo > (int)gardenRegion.getMaxWidth() &&
                        yTo > (int)gardenRegion.getMaxHeight()) {
                    xTo = (int)gardenRegion.getMaxWidth();
                    yTo = (int)gardenRegion.getMaxHeight();
                }
                else if(xFrom < 0 && yTo > (int)gardenRegion.getMaxHeight()) {
                    xFrom = 0;
                    yTo = (int)gardenRegion.getMaxWidth();
                }


//                System.out.println("ADDED");
//                System.out.println("X: ( " + xFrom + ", " + xTo + ")");
//                System.out.println("Y: ( " + yFrom + ", " + yTo + ")");

                for(int x=xFrom;x<xTo;x++) {
                    for(int y=yFrom;y<yTo;y++) {
                        garden[y][x] = 1;
                    }
                }
            }
        });

        removedObstacleList.forEach(new Consumer<Rectangle>() {
            @Override
            public void accept(Rectangle rectangle) {
                int xFrom = (int)rectangle.getX();
                int yFrom = (int)rectangle.getY();

                int xTo = xFrom + (int)rectangle.getWidth();
                int yTo = yFrom + (int)rectangle.getHeight();

                if((xFrom < 0 && yFrom < 0)) {
                    xFrom = 0;
                    yFrom = 0;
                }
                else if(xTo > (int)gardenRegion.getMaxWidth() && yFrom < 0) {
                    yFrom = 0;
                    xTo = (int)gardenRegion.getMaxWidth();
                }
                else if(xTo > (int)gardenRegion.getMaxWidth() &&
                        yTo > (int)gardenRegion.getMaxHeight()) {
                    xTo = (int)gardenRegion.getMaxWidth();
                    yTo = (int)gardenRegion.getMaxHeight();
                }
                else if(xFrom < 0 && yTo > (int)gardenRegion.getMaxHeight()) {
                    xFrom = 0;
                    yTo = (int)gardenRegion.getMaxWidth();
                }

//                System.out.println("REMOVED");
//                System.out.println("X: ( " + xFrom + ", " + xTo + ")");
//                System.out.println("Y: ( " + yFrom + ", " + yTo + ")");

                for(int x=xFrom;x<xTo;x++) {
                    for(int y=yFrom;y<yTo;y++) {
                        garden[y][x] = 0;
                    }
                }
            }
        });

//        for(int i=0;i<garden.length;i++) {
//            System.out.print("i: " + i);
//            System.out.print("[ ");
//            for(int j=0;j<garden[i].length;j++) {
//                System.out.print(garden[i][j] + ", ");
//            }
//            System.out.println(" ]");
//        }

        return garden;
    }

    public List<Rectangle> getObstacles() {
        ArrayList<Rectangle> obstacles = new ArrayList<>();
        Byte[][] garden = getByteGarden();


        boolean flag = false;
        int x = -1;
        int y = -1;
        int width = 0;
        int height = 0;

        int wTemp = -1;
        int kTemp = -1;

        int countWidth = 0;

        for(int w=0;w<garden.length;w++) {
            for(int k=0;k<garden[w].length;k++) {
                if(garden[w][k] == 1) {
                    if(!flag) {
                        y = w;
                        x = k;
                        width++;
                        flag = true;
                    }
                    else if(flag && height == 0) {
                        width++;
                    }
                    else if(flag && height > 0) {
                        countWidth++;
                    }
                }
                else if(garden[w][k] == 0) {
                    if(flag) {
                        if(height == 0) {
                            wTemp = w;
                            kTemp = k;

                            height++;
                            w++;
                            k = x-1;
                        }
                        else if(height > 0) {
                            if(countWidth == width) {
                                height++;
                                w++;
                                k = x-1;
                            }
                            else {
                                Rectangle rec = new Rectangle(x,y,width,height);
                                obstacles.add(rec);
                                for(int w2 = y;w2<y+height;w2++) {
                                    for(int k2= x;k2<x+width;k2++) {
                                        garden[w2][k2] = 0;
                                    }
                                }

                                //reset parameters
                                height = 0;
                                width = 0;
                                x = -1;
                                y = -1;
                                flag = false;

                                //return to previous position
                                w = wTemp;
                                k = kTemp-1;
                                wTemp = -1;
                                kTemp = -1;
                            }
                            countWidth = 0;
                        }
                    }
                }
            }
        }

        return obstacles;

    }
}
