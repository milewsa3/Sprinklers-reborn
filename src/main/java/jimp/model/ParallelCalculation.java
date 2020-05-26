package jimp.model;

import javafx.application.Platform;
import jimp.controllers.GardenBox;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

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
                    PrintWriter out = new PrintWriter(filename);
                    out.println(pop.printSprinklers());
                    out.close();
                }catch (FileNotFoundException e){
                    System.out.println(pop.printSprinklers());
                }
            }
        });
    }




}
