package game;

import drawing.Animator;
import game.arena.Maze;
import game.engine.Entity;
import game.engine.GamestateHandler;
import geometry.Vector2f;
import io.ImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Player extends Entity {

    public int level = 1, currentXP = 0, xpNeeded = 20, currentHP = 10, maxHP = 10;
    public int strength = 2, defense = 2;
    // position on the map
    private Vector2f nextPos = new Vector2f(0, 0);
    private Dir direction = Dir.SOUTH;

    private final double vel = 0.05;

    private final Animator spriteAnimator, shadowAnimator;

    public Player() {
        String spriteNames[] = {"player_walk_up_", "player_walk_right_", "player_walk_down_", "player_walk_left_"};
        List<BufferedImage> sprites = new ArrayList<>();
        for (String s : spriteNames) {
            for (int i = 0; i < 6; i++) {
                sprites.add(ImageLoader.sprites.get(s + i));
            }
        }

        List<BufferedImage> shadowSprites = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            shadowSprites.add(ImageLoader.sprites.get("player_shadow_" + i));
        }

        spriteAnimator = new Animator(0, 4, sprites, state -> {
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

        shadowAnimator = new Animator(0, 4, shadowSprites,
                state -> pos.equals(nextPos) ? 0 : ++state % 6);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(shadowAnimator.getSprite(), (int) ((pos.x + 0.2) * Maze.CELL_SIZE),
                (int) ((pos.y) * Maze.CELL_SIZE), (int) (Maze.CELL_SIZE / 1.5), (int) ((Maze.CELL_SIZE / 1.5) * 1.5), null);
        g.drawImage(spriteAnimator.getSprite(), (int) ((pos.x + 0.2) * Maze.CELL_SIZE),
                (int) ((pos.y) * Maze.CELL_SIZE), (int) (Maze.CELL_SIZE / 1.5), (int) ((Maze.CELL_SIZE / 1.5) * 1.5), null);
    }


    public void update(double delta) {

        spriteAnimator.update(delta);
        shadowAnimator.update(delta);

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
            spriteAnimator.flush();
            shadowAnimator.flush();
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

    public void addXP(int dXP) {
        currentXP += dXP;
        levelup();
    }

    private void levelup() {
        int possibleoverhead = currentXP - xpNeeded;
        if (currentXP >= xpNeeded) {
            level++;
            currentXP = Math.max(possibleoverhead, 0);

            strength++;
            defense++;
            maxHP += 5;
            currentHP = maxHP;

            xpNeeded = (int) (xpNeeded * 1.5);
        }
    }
}
