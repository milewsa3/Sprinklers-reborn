package jimp.model;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import jimp.controllers.GardenBox;

import java.util.ArrayList;
import java.util.function.Consumer;

/* Klasa będąca "wypaloną płytką" tego co narysował użytkownik prostokątami w GUI */
/* Klasa z macierza traw i sum potrzebnych do obliczenia wyniku spryskiwaczy*/
public class GardenGrass {
    private int xl;
    private int yl;
    private int grassBlocks;
    private int[][] sum;
    private boolean [][] grass;
    private final int SKALA = 10;


    public GardenGrass(int x, int y){
        this.xl=x;
        this.yl=y;
        this.grass = new boolean[x][y];
    }

    public GardenGrass (GardenBox gardenBox){
        this.xl=gardenBox.getX();
        this.yl=gardenBox.getY();
        this.grass = new boolean[this.xl][this.yl];
        this.sum = new int[this.xl][this.yl];
        int x1,x2,y1,y2;
        this.fillGrass();
        for(Rectangle rect : gardenBox.getObstacles()){
            System.out.println(rect.toString());

            //wartosci krawedzi prostokatow i sprawdzenie warunkow granicznych
            x1=(int)(rect.getX()*SKALA);
            y1=(int)(rect.getY()*SKALA);
            x2=(int)(rect.getX()*SKALA+rect.getWidth()*SKALA);
            y2=(int)(rect.getY()*SKALA+rect.getHeight()*SKALA);

            if(x1<0)
                x1=0;
            if(y1<0)
                y1=0;
            if(x2>gardenBox.getX())
                x2=gardenBox.getX();
            if(y2>gardenBox.getY())
                y2=gardenBox.getY();

            for(int i=y1;i<y2;i++){
                for(int j=x1;j<x2;j++) {
                    this.grass[j][i]=false;
                }
            }
        }
        for(int j=0;j<this.yl;j++) {
        for(int i=0;i<this.xl;i++){
                if (this.grass[i][j])
                    this.grassBlocks++;
            }
        }

    }

    public void setGrass(int x,int y,boolean val) {
        grass[x][y] = val;
    }

    public int getXl() {
        return xl;
    }

    public int getYl() {
        return yl;
    }

    public int getGrassBlocks() {
        return grassBlocks;
    }

    public boolean getGrass(int x, int y) {
        if(x>=0&&x<xl&&y>=0&&y<yl)
        return grass[x][y];
        else return false;
    }
    public void setSum(int x, int y, int val) {
        sum[x][y] = val;
    }

    public int getSum(int x, int y) {

        if(x>=0&&x<xl&&y>=0&&y<yl)
        return sum[x][y];
        else return 0;
    }

    public void addSum(int x,int y,int val) {
        if(x>=0&&x<xl&&y>=0&&y<yl)
        sum[x][y] += val;
    }

    void fillGrass() {
        for (int i = 0; i < xl; i++) {
            for (int j = 0; j < yl; j++) {
                grass[i][j] = true;
            }
        }
    }

    void cleanSums() {
        for (int i = 0; i < xl; i++) {
            for (int j = 0; j < yl; j++) {
                sum[i][j] = 0;
            }
        }
    }



}


