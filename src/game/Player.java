package game;

import drawing.Drawable;
import game.arena.Maze;
import game.engine.Entity;
import io.ImageLoader;

import java.awt.*;
import java.awt.geom.Point2D;

public class Player implements Entity, Drawable {

    // position on the map
    private Point2D pos = new Point(0, 0);
    private Point2D nextPos;

    private final double vel = 0.25;

    @Override
    public void draw(Graphics g) {
        g.drawImage(ImageLoader.sprites.get("player_idle_down"), (int)((pos.getX() +  0.2)  * Maze.CELL_SIZE),
                (int)((pos.getY() )  * Maze.CELL_SIZE),(int)(Maze.CELL_SIZE / 1.5), (int)((Maze.CELL_SIZE/ 1.5) * 1.5),  null);
    }

    @Override
    public void update(double delta) {

    }

    public Point2D getPos() {
        return pos;
    }
}
