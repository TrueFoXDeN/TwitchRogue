package game;

import application.GameLoop;
import drawing.Animator;
import game.arena.Maze;
import game.engine.Entity;
import geometry.Vector2f;
import io.ImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import static application.GameLoop.gHandler;

public class Enemy extends Entity {

    public EnemyTyp type;
    public Vector2f randomWalkDest;

    public Queue<Dir> path = new ConcurrentLinkedQueue<>();
    private Animator animator;

    public Enemy(Vector2f pos) {
        this.pos = pos;
        this.nextPos = (Vector2f) pos.clone();

        Random r = new Random();
        switch (r.nextInt(1)) {
            case 0:
                type = EnemyTyp.SLIME;
        }

        List<BufferedImage> slimeSprites = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            slimeSprites.add(ImageLoader.sprites.get("slime_" + i));
        }

        animator = new Animator(0, 8, slimeSprites, state -> ++state % 4);
    }

    public void move() {
        if (pos.equals(nextPos)) {
            animator.flush();

            if (playerVisible()) {
                randomWalkDest = gHandler.getPlayer().getPos();
                path = new ConcurrentLinkedQueue<>(gHandler.getCurrentMaze().getA_star().a_star(randomWalkDest, pos));
            } else if (pos.equals(randomWalkDest) || path.size() == 0) {
                Random r = new Random();
                do {
                    int w = gHandler.getCurrentMaze().width;
                    int h = gHandler.getCurrentMaze().height;

                    randomWalkDest = new Vector2f(r.nextInt(w), r.nextInt(h));
                } while (randomWalkDest.equals(pos));

                path = new ConcurrentLinkedQueue<>(gHandler.getCurrentMaze().getA_star().a_star(randomWalkDest, pos));
            }

            direction = path.poll();
            Vector2f dPos = Dir.dirToVec2f(direction);

            nextPos.add(dPos);
        }
    }

    public boolean playerVisible() {
        if(pos.equals(gHandler.getPlayer().getPos())) return true;

        boolean canSeePlayer = true;
        if (pos.x == gHandler.getPlayer().getPos().x) {
            if (pos.y < gHandler.getPlayer().getPos().y) {
                for (double y = pos.y + 1; y < gHandler.getPlayer().getPos().y; y++) {
                    Vector2f currentPos = new Vector2f(pos.x, y);
                    if (!(gHandler.getCurrentMaze().canMove(currentPos, Dir.NORTH)
                            && gHandler.getCurrentMaze().canMove(currentPos, Dir.SOUTH))) {
                        canSeePlayer = false;
                        break;
                    }
                }
            } else {
                for (double y = pos.y - 1; y > gHandler.getPlayer().getPos().y; y--) {
                    Vector2f currentPos = new Vector2f(pos.x, y);
                    if (!(gHandler.getCurrentMaze().canMove(currentPos, Dir.NORTH)
                            && gHandler.getCurrentMaze().canMove(currentPos, Dir.SOUTH))) {
                        canSeePlayer = false;
                        break;
                    }
                }
            }
            return canSeePlayer;
        } else if (pos.y == gHandler.getPlayer().getPos().y) {
            if (pos.x < gHandler.getPlayer().getPos().x) {
                for (double x = pos.x + 1; x < gHandler.getPlayer().getPos().x; x++) {
                    Vector2f currentPos = new Vector2f(x, pos.y);
                    if (!(gHandler.getCurrentMaze().canMove(currentPos, Dir.WEST)
                            && gHandler.getCurrentMaze().canMove(currentPos, Dir.EAST))) {
                        canSeePlayer = false;
                        break;
                    }
                }
            } else {
                for (double x = pos.x - 1; x > gHandler.getPlayer().getPos().x; x--) {
                    Vector2f currentPos = new Vector2f(x, pos.y);
                    if (!(gHandler.getCurrentMaze().canMove(currentPos, Dir.WEST)
                            && gHandler.getCurrentMaze().canMove(currentPos, Dir.EAST))) {
                        canSeePlayer = false;
                        break;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void interact(Player player) {

    }

    @Override
    public void update(double delta) {
        super.update(delta);

        animator.update(delta);

        if (Math.abs(nextPos.x - pos.x) > 0.05) {
            pos.x += Math.signum(nextPos.x - pos.x) * vel * delta;
        }

        if (Math.abs(nextPos.y - pos.y) > 0.05) {
            pos.y += Math.signum(nextPos.y - pos.y) * vel * delta;
        }

        if (nextPos.dist(pos) < 0.05 && !nextPos.equals(pos)) {
            pos = (Vector2f) nextPos.clone();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(animator.getSprite(), (int) ((pos.x + 0.2) * Maze.CELL_SIZE),
                (int) ((pos.y) * Maze.CELL_SIZE), (int) (Maze.CELL_SIZE / 1.5), (int) ((Maze.CELL_SIZE / 1.5) * 1.2), null);
    }

    public enum EnemyTyp {
        SLIME
    }
}
