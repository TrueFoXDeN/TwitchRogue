package game.items;

import application.GameLoop;
import game.Player;
import game.arena.Maze;
import geometry.Vector2f;
import io.ImageLoader;

import java.awt.*;
import static application.GameLoop.gHandler;

public class Potion extends Item {

    private final int HEAL_AMOUNT = 5;

    public Potion(double x, double y) {
        pos = new Vector2f(x, y);
    }

    @Override
    public void use() {
        Player player = gHandler.getPlayer();
        player.currentHP = Math.max(player.currentHP + HEAL_AMOUNT, player.maxHP);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(ImageLoader.sprites.get("potion"), (int) ((pos.x + 0.35) * Maze.CELL_SIZE), (int) ((pos.y + 0.25) * Maze.CELL_SIZE),
                (int) ((Maze.CELL_SIZE / 1.28) * 0.5), (int) (((Maze.CELL_SIZE / 1.28) * 1.28) * 0.5), null);
    }

    @Override
    public void update(double delta) {

    }
}
