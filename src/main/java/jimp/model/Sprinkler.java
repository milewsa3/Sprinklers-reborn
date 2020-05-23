package jimp.model;

import java.util.Random;

public class Sprinkler implements Comparable<Sprinkler>{
    private int x;
    private int y;
    private double score;
    private Angle angle;
    private Direction direction;

    public Sprinkler(){}

    public Sprinkler(int x, int y, double score, Angle angle, Direction direction) {
        this.x = x;
        this.y = y;
        this.score = score;
        this.angle = angle;
        this.direction = direction;
    }
    public Sprinkler(Random r, int maxX, int maxY){
        this.x = r.nextInt(maxX);
        this.y = r.nextInt(maxY);

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

    public int compareTo(Sprinkler s2){
        return (int)(score-s2.score);
    }

    public double getScore(){
        return this.score;
    }

    public void setScore(double newScore){
        this.score = newScore;
    }

    public int getX(){
        return this.x;
    }

    public void setX(int x){
        this.x = x;
    }

    public int getY(){
        return this.y;
    }

    public void setY(int y){
        this.y = y;
    }

    public Angle getType(){
        return this.angle;
    }

    public void setType(Angle t){
        this.angle = t;
    }

    public Direction getDirection(){
        return this.direction;
    }

    public void setDirection(Direction d){
        this.direction = d;
    }
}
