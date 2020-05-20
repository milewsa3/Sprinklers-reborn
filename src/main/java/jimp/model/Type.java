package jimp.model;

public enum Type {
    ANGLE_90(500,90f),ANGLE_180(400,180f),
    ANGLE_270(300,270f),ANGLE_360(200,360f);

    private final int radius;
    private final double angle;

    Type(int radius,double angle) {
        this.radius = radius;
        this.angle = angle;
    }

    public int getRadius() {
        return radius;
    }
    public double getAngle() {return angle;}
}
