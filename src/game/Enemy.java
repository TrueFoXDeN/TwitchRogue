package game;

import application.GameLoop;
import game.engine.Entity;
import geometry.Vector2f;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import static application.GameLoop.gHandler;

public class Enemy extends Entity {

    public EnemyTyp type;
    public Vector2f randomWalkDest;

    public Queue<Dir> path;

    public Enemy(Vector2f pos) {
        this.pos = pos;
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
    }

    @Override
    public void draw(Graphics g) {

    }

    public enum EnemyTyp {

    }

}
