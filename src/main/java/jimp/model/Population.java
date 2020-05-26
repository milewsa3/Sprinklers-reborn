package jimp.model;

import jimp.controllers.GardenBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Population {

    final int dead_score[] = { 30000, 100000, 80000, 50000 };

    private List<Sprinkler> sprinklerList;
    int speed;

    //konstruktor jednoczesnie tworzy końcową populację
    public Population(GardenGrass gardenGrass){
        sprinklerList = new ArrayList<>();
        speed = calcDeathSpeed(gardenGrass);
        int watchdog=0, prevPopSize=0;
        //System.out.println(gardenGrass.getGrassBlocks() + "  " + gardenGrass.getXl()*gardenGrass.getYl());
        if(gardenGrass.getGrassBlocks()>0) {
            Random r = new Random();
            for (int i = 0; i < 20; i++) {
                sprinklerList.add(new Sprinkler(r, gardenGrass));
            }
            for (int i = 0; i < 400; i++) {
                    for (int j = 0; j < 20; j++) {
                        sprinklerList.add(new Sprinkler(r, gardenGrass));
                    }
                    for(int k=0;k<10;k++) {
                        calcPopScore(gardenGrass);
                        Collections.sort(this.sprinklerList);
                        terminator();
                        if(sprinklerList.size()>0) {
                            if (sprinklerList.get(sprinklerList.size() - 1).getScore() > dead_score[0])
                                break;
                        }
                    }

                if(prevPopSize==sprinklerList.size())
                    watchdog++;
                else
                    watchdog=0;

                if(watchdog>10)
                    break;
                prevPopSize = sprinklerList.size();

                //System.out.println("Iteracja nr: " + i + "rozmiar pop: " + sprinklerList.size());
                //System.out.println(this.printSprinklers());
            }

            //System.out.println(this.printSprinklers());
        }
    }

    private void terminator(){
        for(int i =0; i< sprinklerList.size(); i++){
            if(sprinklerList.get(i).getScore() < dead_score[sprinklerList.get(i).getType().getType()]){
                sprinklerList.remove(sprinklerList.get(i));
            }
        }
        //System.out.println ("wynik ostatniego : " + sprinklerList.get(sprinklerList.size()-1).getScore() );

    }
    private void calcPopScore(GardenGrass gg){
        gg.cleanSums();
        for(Sprinkler spr:sprinklerList){
            spr.calcScore(gg);
        }
    }
    //przekaz abstrakcyjne zraszacze do gardenboxa jako animowane kółka
    public void drawPopulation(GardenBox gardenBox){
        gardenBox.clean();
        for(Sprinkler spr:sprinklerList) {
            gardenBox.addSprinklerShape(spr.getType(),spr.getDirection(), spr.getX(), spr.getY());
        }
    }

    private int calcDeathSpeed(GardenGrass gg){
        int halfMap = gg.getXl()*gg.getYl()/2;
        if(gg.getGrassBlocks()<halfMap)
            return 0;
        else
            return (12- (6 *(gg.getXl()*gg.getYl()-gg.getGrassBlocks())/halfMap));
    }
    public String printSprinklers(){
        String wy = new String();
        char dir = '-';
        int angle;
        wy += "| SPR |   X   |   Y   | DIR | TYPE |\n";
        wy += "------------------------------------\n";
        for(int i=0;i<sprinklerList.size();i++){
            switch (sprinklerList.get(i).getDirection()) {
                case NORTH:
                    dir = 'N';
                    break;

                case EAST:
                    dir = 'E';
                    break;

                case SOUTH:
                    dir = 'S';
                    break;

                case WEST:
                    dir = 'W';
                    break;
            }
            wy += String.format("| %3d | %5d | %5d | %3c | %4d |\n",i,sprinklerList.get(i).getX()+1,sprinklerList.get(i).getY(),dir,(int)sprinklerList.get(i).getType().getAngle()); //%5d (int)sprinklerList.get(i).getScore()
            }
        wy += "------------------------------------\n";
        return wy;
    }
}
