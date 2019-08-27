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
        g.drawImage(ImageLoader.sprites.get("potion"), (int) ((pos.x +0.35) * Maze.CELL_SIZE), (int) ((pos.y+0.25) * Maze.CELL_SIZE),
                (int) ((Maze.CELL_SIZE / 1.28)* 0.5) , (int) (((Maze.CELL_SIZE / 1.28) * 1.28) * 0.5), null);
    }

    @Override
    public void update(double delta) {

    }
}
