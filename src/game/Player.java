package game;

import drawing.Drawable;
import game.arena.Maze;
import game.engine.Entity;
import game.engine.GamestateHandler;
import geometry.Vector2f;
import io.ImageLoader;

import java.awt.*;
import java.awt.geom.Point2D;

public class Player implements Entity, Drawable {

    // position on the map
    private Vector2f pos = new Vector2f(0, 0);
    private Vector2f nextPos = new Vector2f(0,0);

    private final double vel = 0.05;

    @Override
    public void draw(Graphics g) {
        g.drawImage(ImageLoader.sprites.get("player_idle_down"), (int) ((pos.x + 0.2) * Maze.CELL_SIZE),
                (int) ((pos.y) * Maze.CELL_SIZE), (int) (Maze.CELL_SIZE / 1.5), (int) ((Maze.CELL_SIZE / 1.5) * 1.5), null);
    }

    @Override
    public void update(double delta) {

        if (Math.abs(nextPos.x - pos.x) > 0.05) {
            pos.x += Math.signum(nextPos.x - pos.x) * vel * delta;
        }

        if (Math.abs(nextPos.y - pos.y) > 0.05) {
            pos.y += Math.signum(nextPos.y - pos.y) * vel * delta;
        }

        if(nextPos.dist(pos) < 0.05 && !nextPos.equals(pos)) {
            pos = (Vector2f) nextPos.clone();
            GamestateHandler.currentMaze.updateVision(pos);
        }
    }


    public void move(int dx, int dy) {
        if(pos.equals(nextPos))
            nextPos.add(new Vector2f(dx ,dy));
    }

    public Vector2f getPos() {
        return pos;
    }
}
