package jimp.model;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import jimp.controllers.GardenBox;

import java.util.ArrayList;
import java.util.function.Consumer;

/* Klasa będąca "wypaloną płytką" tego co narysował użytkownik prostokątami w GUI */
public class GardenGrass {
    private int xl;
    private int yl;
    private int grassBlocks;
    private short [][] grass;


    public GardenGrass(int x, int y){
        this.xl=x;
        this.yl=y;
        this.grass = new short[x][y];

        //TYMCZASOWE DO USUNIECIA!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        fillGrass();
    }
//TODO

    public static GardenGrass burnMap(GardenBox gardenBox){
        GardenGrass gg = new GardenGrass(gardenBox.getX(),gardenBox.getY());
        for(Rectangle rect : gardenBox.getObstacles()){
            rect.toString();
        }
        return gg;
    }

    public void setGrass(int x,int y,short val) {
        grass[x][y] = val;
    }

    public short getGrass(int x,int y) {
        return grass[x][y];
    }

    void fillGrass() {
        for (int i = 0; i < xl; i++) {
            for (int j = 0; j < yl; j++) {
                grass[i][j] = 1;
            }
        }
    }



}


