package jimp.model;

import java.util.Random;

/*Klasa reprezentujaca modelowy zraszacz. Nie skorzystalem  z polimorfizmu bo latwiej mi korzystac z jednej klasy
z parametrami niz 16 plikow klas ktore troche sie roznia w obliczeniach.
 */

public class Sprinkler implements Comparable<Sprinkler> {
    private int x;
    private int y;
    private double score;
    private Angle angle;
    private Direction direction;

    public Sprinkler() {
    }

    public Sprinkler(int x, int y, double score, Angle angle, Direction direction) {
        this.x = x;
        this.y = y;
        this.score = score;
        this.angle = angle;
        this.direction = direction;
    }

    public Sprinkler(Random r, GardenGrass gg) {
        do{
            this.x = r.nextInt(gg.getXl());
            this.y = r.nextInt(gg.getYl());
        } while (!gg.getGrass(x,y));

        this.score = 0;

        int losowy = r.nextInt(4);
        switch (losowy) {
            case 0:
                this.angle = Angle.ANGLE_90;
                break;
            case 1:
                this.angle = Angle.ANGLE_180;
                break;
            case 2:
                this.angle = Angle.ANGLE_270;
                break;
            case 3:
                this.angle = Angle.ANGLE_360;
                break;
        }
        losowy = r.nextInt(4);
        switch (losowy) {
            case 0:
                this.direction = Direction.EAST;
                break;
            case 1:
                this.direction = Direction.NORTH;
                break;
            case 2:
                this.direction = Direction.WEST;
                break;
            case 3:
                this.direction = Direction.SOUTH;
                break;
        }
    }



    public void calcScore(GardenGrass gardenGrass) {
        this.score = 0;
        for (int i = (this.y - this.angle.getRadius()); i <= (this.y + this.angle.getRadius()); i++)
            for (int j = (this.x - this.angle.getRadius()); j <= (this.x + this.angle.getRadius()); j++) {
                if (((j - this.x) * (j - this.x) + (i - this.y) * (i - this.y)) < (this.angle.getRadius() * this.angle.getRadius())) {
                    switch (this.angle) {
                        case ANGLE_360:
                            if (check_obstacle(gardenGrass, j, i) == false) {
                                if(gardenGrass.getSum(j,i)==0)
                                this.score++;
                                gardenGrass.addSum(j, i, 1);
                            }
                            break;
                        case ANGLE_270:
                            if (this.direction == Direction.NORTH) {
                                if (i > this.y && j < this.x) {
                                    continue;
                                }
                            }
                            if (this.direction == Direction.EAST) {
                                if (i < this.y && j < this.x) {
                                    continue;
                                }
                            }
                            if (this.direction == Direction.SOUTH) {
                                if (i < this.y && j > this.x) {
                                    continue;
                                }
                            }
                            if (this.direction == Direction.WEST) {
                                if (i > this.y && j > this.x) {
                                    continue;
                                }
                            }
                            if (check_obstacle(gardenGrass, j, i) == false) {
                                if (gardenGrass.getSum(j, i) == 0)
                                    this.score++;
                                gardenGrass.addSum(j, i, 1);
                            }
                            break;
                        case ANGLE_180:
                            if (this.direction == Direction.NORTH) {
                                if (i > this.y) {
                                    continue;
                                }
                            }
                            if (this.direction == Direction.EAST) {
                                if (j < this.x) {
                                    continue;
                                }
                            }
                            if (this.direction == Direction.SOUTH) {
                                if (i < this.y) {
                                    continue;
                                }
                            }
                            if (this.direction == Direction.WEST) {
                                if (j > this.x) {
                                    continue;
                                }
                            }
                            if (check_obstacle(gardenGrass, j, i) == false) {
                                if (gardenGrass.getSum(j, i) == 0)
                                    this.score++;
                                gardenGrass.addSum(j, i, 1);
                            }

                            break;
                        case ANGLE_90:
                            if (this.direction == Direction.NORTH) {
                                if (i < this.y && j > this.x) {
                                    if (check_obstacle(gardenGrass, j, i) == false) {
                                        if (gardenGrass.getSum(j, i) == 0)
                                            this.score++;
                                        gardenGrass.addSum(j, i, 1);
                                    }
                                }
                            }
                            if (this.direction == Direction.EAST) {
                                if (i > this.y && j > this.x) {
                                    if (check_obstacle(gardenGrass, j, i) == false) {
                                        if (gardenGrass.getSum(j, i) == 0)
                                            this.score++;
                                        gardenGrass.addSum(j, i, 1);
                                    }
                                }
                            }
                            if (this.direction == Direction.SOUTH) {
                                if (i > this.y && j < this.x) {
                                    if (check_obstacle(gardenGrass, j, i) == false) {
                                        if (gardenGrass.getSum(j, i) == 0)
                                            this.score++;
                                        gardenGrass.addSum(j, i, 1);
                                    }
                                }
                            }
                            if (this.direction == Direction.WEST) {
                                if (i < this.y && j < this.x) {
                                    if (check_obstacle(gardenGrass, j, i) == false) {
                                        if (gardenGrass.getSum(j, i) == 0)
                                            this.score++;
                                        gardenGrass.addSum(j, i, 1);
                                    }
                                }
                            }
                            break;
                    }
                }
            }
    }


    // true jesli jest przeszkoda
    private boolean check_obstacle(GardenGrass gardenGrass, int x, int y) {

        int ty, tx, prec = 30;
        if(!gardenGrass.getGrass(x,y)){
            return true;
        }
        if(this.x <= x && this.y >= y)
        {
            tx = x;
            while (tx - this.x >= 0){
                ty = y;
                while(this.y - ty >= 0){
                    if (!gardenGrass.getGrass(tx,ty))
                        return true;
                    ty += prec;
                }
                tx -= prec;
            }
        }
        if(this.x > x && this.y > y)
        {
            tx = x;
            while (this.x - tx >= 0){
                ty = y;
                while(this.y - ty >= 0){
                    if (!gardenGrass.getGrass(tx,ty))
                        return true;
                    ty += prec;
                }
                tx += prec;
            }
        }
        if(this.x >= x && this.y <= y)
        {
            tx = x;
            while (this.x - tx >= 0){
                ty = y;
                while(ty - this.y >= 0){
                    if (!gardenGrass.getGrass(tx,ty))
                        return true;
                    ty -= prec;
                }
                tx += prec;
            }
        }
        if(this.x < x && this.y < y)
        {
            tx = x;
            while (tx - this.x >= 0){
                ty = y;
                while(ty - this.y >= 0){
                    if (!gardenGrass.getGrass(tx,ty))
                        return true;
                    ty -= prec;
                }
                tx -= prec;
            }
        }
        return false;
    }

    public int compareTo(Sprinkler s2) {
        return (int) (s2.score - score);
    }

    public double getScore() {
        return this.score;
    }

    public void setScore(double newScore) {
        this.score = newScore;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Angle getType() {
        return this.angle;
    }

    public void setType(Angle t) {
        this.angle = t;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public void setDirection(Direction d) {
        this.direction = d;
    }
}
