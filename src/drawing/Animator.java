package drawing;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Animator {

    public Animator(double switchTime, List<BufferedImage> sprites, Rule rule) {
        this.switchTime = switchTime;
        this.sprites = sprites;
        this.rule = rule;
    }

    private double currTime = 0;
    private int state = 0;
    private double switchTime;

    private List<BufferedImage> sprites = new ArrayList<>();
    private Rule rule;

    public void update(double delta) {
        currTime -= delta;
        rule.fire(state);

        if(currTime <= 0) {
            currTime = switchTime;
        }
    }

    public BufferedImage getSprite() {
        return sprites.get(state);
    }

    @FunctionalInterface
    public interface Rule {
        void fire(int state);
    }
}
