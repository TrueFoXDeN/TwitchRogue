package game;

import application.GameLoop;
import drawing.Animator;
import game.arena.Maze;
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

        Random r = new Random();
        switch (r.nextInt(1)) {
            case 0: type = EnemyTyp.SLIME;
        }

        List<BufferedImage> slimeSprites = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            slimeSprites.add(ImageLoader.sprites.get("slime_" + i));
        }

        animator = new Animator(0, 4, slimeSprites, state -> ++state % 4);
    }

    public void move() {
        animator.flush();
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

        direction = path.poll();
        Vector2f dPos = Dir.dirToVec2f(direction);

        nextPos.add(dPos);
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

        animator.update(delta);

        if (Math.abs(nextPos.x - pos.x) > 0.05) {
            pos.x += Math.signum(nextPos.x - pos.x) * vel * delta;
        }

        if (Math.abs(nextPos.y - pos.y) > 0.05) {
            pos.y += Math.signum(nextPos.y - pos.y) * vel * delta;
        }

        if (nextPos.dist(pos) < 0.05 && !nextPos.equals(pos)) {
            pos = (Vector2f) nextPos.clone();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(animator.getSprite(), (int) ((pos.x + 0.2) * Maze.CELL_SIZE),
                (int) ((pos.y) * Maze.CELL_SIZE), (int) (Maze.CELL_SIZE / 1.5), (int) ((Maze.CELL_SIZE / 1.5) * 1.5), null);
    }

    public enum EnemyTyp {
        SLIME
    }

}
