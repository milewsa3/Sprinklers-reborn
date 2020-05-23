package jimp.animations;

import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

public class ScalingAnimation {
    private ScaleTransition scaleTransition;

    public ScalingAnimation(Node node) {
        scaleTransition = new ScaleTransition(Duration.millis(1200),node);
        scaleTransition.setCycleCount(Timeline.INDEFINITE);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setByX(-0.05);
        scaleTransition.setByY(-0.05);
    }

    public void start() {
        scaleTransition.playFromStart();
    }
}
