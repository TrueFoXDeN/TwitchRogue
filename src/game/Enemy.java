package game;

import application.GameLoop;
import drawing.Animator;
import game.engine.Entity;
import geometry.Vector2f;
import io.ImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import static application.GameLoop.gHandler;

public class Enemy extends Entity {

    public EnemyTyp type;
    public Vector2f randomWalkDest;

    public Queue<Dir> path;
    private Animator animator;
    public Enemy(Vector2f pos) {
        this.pos = pos;

        List<BufferedImage> slimeSprites = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            slimeSprites.add(ImageLoader.sprites.get("slime_" + i));
        }

        animator = new Animator(0, 4, slimeSprites, state -> ++state % 4);

    }

    public void move() {
        if(playerVisible()) {
            randomWalkDest = gHandler.getPlayer().getPos();
            path = new ConcurrentLinkedQueue<>(gHandler.getCurrentMaze().getA_star().a_star(randomWalkDest, pos));
        } else if(pos.equals(randomWalkDest)) {
            Random r = new Random();
            int w = gHandler.getCurrentMaze().width;
            int h = gHandler.getCurrentMaze().height;

            randomWalkDest = new Vector2f(r.nextInt(w), r.nextInt(h));
            path = new ConcurrentLinkedQueue<>(gHandler.getCurrentMaze().getA_star().a_star(randomWalkDest, pos));
        }

        Dir nextStep = path.poll();
    }

    public boolean playerVisible() {
        return false;
    }

    @Override
    public void interact(Player player) {

    }

    @Override
    public void update(double delta) {
        super.update(delta);
    }

    @Override
    public void draw(Graphics g) {

    }

    public enum EnemyTyp {

    }

}
