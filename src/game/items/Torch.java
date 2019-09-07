package game.items;

import application.GameLoop;
import application.Main;
import game.Player;
import game.arena.Maze;
import geometry.Vector2f;
import io.ImageLoader;

import static application.GameLoop.gHandler;

import java.awt.*;

public class Torch extends Item {

    public Torch(double x, double y) {
        pos = new Vector2f(x, y);
    }

    @Override
    public void use() {
        gHandler.getPlayer().useTorch = true;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(ImageLoader.sprites.get("fackel"),  (int) ((pos.x +0.35) * Maze.CELL_SIZE), (int) ((pos.y+0.25) * Maze.CELL_SIZE),
                (int) ((Maze.CELL_SIZE / 1.8)* 0.5) , (int) ((Maze.CELL_SIZE )* 0.6), null);
    }

    @Override
    public void interact(Player player) {
        gHandler.getEntities().remove(this);
        gHandler.getCurrentMaze().getEntities().remove(this);

        Main.display.getDrawables().remove(this);
        use();
    }

    @Override
    public void update(double delta) {
        super.update(delta);
    }
}
