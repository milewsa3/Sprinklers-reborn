package jimp.model;

public enum Angle {
    ANGLE_90(500, 90f, 0), ANGLE_180(400, 180f,1),
    ANGLE_270(300, 270f,2), ANGLE_360(200, 360f,3);

    private final int radius;
    private final int type;
    private final double angle;

    Angle(int radius, double angle, int type) {
        this.radius = radius;
        this.angle = angle;
        this.type = type;
    }

    public int getRadius() {
        return radius;
    }

    public double getAngle() {
        return angle;
    }

    public int getType() {
        return type;
    }
}
