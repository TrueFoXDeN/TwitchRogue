package game;

import drawing.Animator;
import drawing.Drawable;
import game.arena.Maze;
import game.engine.Entity;
import game.engine.GamestateHandler;
import geometry.Vector2f;
import io.ImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Player implements Entity, Drawable {

    // position on the map
    private Vector2f pos = new Vector2f(0, 0);
    private Vector2f nextPos = new Vector2f(0, 0);
    private Dir direction = Dir.SOUTH;

    private final double vel = 0.05;

    private final Animator animator;

    public Player() {
        String spriteNames[] = {"player_walk_up_", "player_walk_right_", "player_walk_down_", "player_walk_left_"};
        List<BufferedImage> sprites = new ArrayList<>();
        for (String s : spriteNames) {
            for (int i = 0; i < 6; i++) {
                sprites.add(ImageLoader.sprites.get(s + i));
            }
        }

        animator = new Animator(13, 4, sprites, state -> {
            if (pos.equals(nextPos)) {
                switch (direction) {
                    case NORTH: {
                        return 0;
                    }
                    case EAST: {
                        return 6;
                    }
                    case SOUTH: {
                        return 12;
                    }
                    case WEST: {
                        return 18;
                    }
                    default:
                        return 0;
                }
            } else {
                switch (direction) {
                    case NORTH: {
                        return ++state % 6;
                    }
                    case EAST: {
                        return ++state % 6 + 6;
                    }
                    case SOUTH: {
                        return ++state % 6 + 12;
                    }
                    case WEST: {
                        return ++state % 6 + 18;
                    }
                    default:
                        return 0;
                }
            }
        });
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(animator.getSprite(), (int) ((pos.x + 0.2) * Maze.CELL_SIZE),
                (int) ((pos.y) * Maze.CELL_SIZE), (int) (Maze.CELL_SIZE / 1.5), (int) ((Maze.CELL_SIZE / 1.5) * 1.5), null);
    }

    @Override
    public void update(double delta) {

        animator.update(delta);
        if (Math.abs(nextPos.x - pos.x) > 0.05) {
            pos.x += Math.signum(nextPos.x - pos.x) * vel * delta;
        }

        if (Math.abs(nextPos.y - pos.y) > 0.05) {
            pos.y += Math.signum(nextPos.y - pos.y) * vel * delta;
        }

        if (nextPos.dist(pos) < 0.05 && !nextPos.equals(pos)) {
            pos = (Vector2f) nextPos.clone();
            GamestateHandler.currentMaze.updateVision(pos);
        }
    }


    public void move(int dx, int dy) {
        if (pos.equals(nextPos)) {
            animator.flush();
            nextPos.add(new Vector2f(dx, dy));

            if (dx > 0) direction = Dir.EAST;
            else if (dx < 0) direction = Dir.WEST;

            if (dy > 0) direction = Dir.SOUTH;
            else if (dy < 0) direction = Dir.NORTH;
        }
    }

    public Vector2f getPos() {
        return pos;
    }
}
