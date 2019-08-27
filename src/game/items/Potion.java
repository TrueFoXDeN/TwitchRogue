package game.items;

import game.arena.Maze;
import geometry.Vector2f;
import io.ImageLoader;

import java.awt.*;
import java.awt.event.MouseAdapter;

public class Potion extends Item {

    public Potion(double x, double y) {
        pos = new Vector2f(x, y);
    }


    @Override
    public void use() {

    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(ImageLoader.sprites.get("potion"), (int) pos.x * Maze.CELL_SIZE, (int) pos.y * Maze.CELL_SIZE,
                Maze.CELL_SIZE, Maze.CELL_SIZE, null);
    }

    @Override
    public void update(double delta) {

    }
}
