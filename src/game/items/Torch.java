package game.items;

import game.arena.Maze;
import geometry.Vector2f;
import io.ImageLoader;

import java.awt.*;

public class Torch extends Item {

    public Torch(double x, double y) {
        pos = new Vector2f(x, y);
    }

    @Override
    public void use() {

    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(ImageLoader.sprites.get("fackel"), (int) pos.x * Maze.CELL_SIZE, (int) pos.y * Maze.CELL_SIZE,
                Maze.CELL_SIZE, Maze.CELL_SIZE, null);
    }

    @Override
    public void update(double delta) {

    }
}
