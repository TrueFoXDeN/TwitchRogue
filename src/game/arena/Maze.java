package game.arena;

import drawing.Drawable;
import game.Dir;
import game.engine.Entity;
import geometry.Vector2f;
import io.ImageLoader;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Maze implements Drawable {
    private int size;
    private final int MAX_ENEMIES;

    // size in which the cells should be drawn
    public static int CELL_SIZE = 0;

    // items and enemies
    private final List<Entity> entities = new CopyOnWriteArrayList<>();
    private final Cell[] cells;

    public Maze(int size, int MAX_ENEMIES) {
        this.size = size;
        this.MAX_ENEMIES = MAX_ENEMIES;

        CELL_SIZE = 720 / size;

        cells = new Cell[size * size];

        for (int i = 0; i < cells.length; ++i) {
            cells[i] = new Cell(i % size, i / size);
        }

        for (int i = 0; i < cells.length; ++i) {
            Cell cell = cells[i];

            if (cell.x > 0) {
                cell.neighbors[Dir.WEST.id] = cells[cell.x - 1 + cell.y * size];
            }
            if (cell.x < size - 1) {
                cell.neighbors[Dir.EAST.id] = cells[cell.x + 1 + cell.y * size];
            }
            if (cell.y > 0) {
                cell.neighbors[Dir.NORTH.id] = cells[cell.x + (cell.y - 1) * size];
            }
            if (cell.y > size - 1) {
                cell.neighbors[Dir.SOUTH.id] = cells[cell.x + (cell.y + 1) * size];
            }
        }


        generateMaze();
        updateVision(new Vector2f(0, 0));
    }

    public boolean canMove(Vector2f playerPos, Dir dir) {
        return cells[playerPos.to1DIndex(size)].borders[dir.id];
    }

    public void updateVision(Vector2f playerPos) {
        int x = (int) playerPos.x;
        int y = (int) playerPos.y;

        var revealNeighbor = new Function<Vector2f, Void>() {
            @Override
            public Void apply(Vector2f playerPos1) {
                int x = (int) playerPos1.x;
                int y = (int) playerPos1.y;

                if (playerPos1.x > 0 && !cells[playerPos1.to1DIndex(size)].borders[Dir.WEST.id])
                    cells[playerPos1.add_(new Vector2f(-1, 0)).to1DIndex(size)].halfVisible = true;

                if (playerPos1.x < size - 1 && !cells[x + y * size].borders[Dir.EAST.id])
                    cells[playerPos1.add_(new Vector2f(1, 0)).to1DIndex(size)].halfVisible = true;

                if (playerPos1.y > 0 && !cells[x + y * size].borders[Dir.NORTH.id])
                    cells[playerPos1.add_(new Vector2f(0, -1)).to1DIndex(size)].halfVisible = true;

                if (playerPos1.y < size - 1 && !cells[x + y * size].borders[Dir.SOUTH.id])
                    cells[playerPos1.add_(new Vector2f(0, 1)).to1DIndex(size)].halfVisible = true;

                return null;
            }
        };

        cells[x + y * size].discovered = true;
        revealNeighbor.apply(playerPos);

        int tempX = x;
        int tempY = y;


        // right
        do {
            Cell nextCell = cells[tempX + tempY * size];
            nextCell.discovered = true;
            revealNeighbor.apply(new Vector2f(tempX, tempY));
            if (nextCell.borders[Dir.EAST.id]) break;
        } while (tempX++ < size - 1);

        tempX = x;
        tempY = y;

        // left
        do {
            Cell nextCell = cells[tempX + tempY * size];
            nextCell.discovered = true;
            revealNeighbor.apply(new Vector2f(tempX, tempY));
            if (nextCell.borders[Dir.WEST.id]) break;
        } while (tempX-- > 0);

        tempX = x;
        tempY = y;

        // up
        do {
            Cell nextCell = cells[tempX + tempY * size];
            nextCell.discovered = true;
            revealNeighbor.apply(new Vector2f(tempX, tempY));
            if (nextCell.borders[Dir.NORTH.id]) break;
        } while (tempY-- > 0);

        tempX = x;
        tempY = y;

        // right
        do {
            Cell nextCell = cells[tempX + tempY * size];
            nextCell.discovered = true;
            revealNeighbor.apply(new Vector2f(tempX, tempY));
            if (nextCell.borders[Dir.SOUTH.id]) break;
        } while (tempY++ < size - 1);
    }

    private void generateMaze() {
        // source of algorithm: https://en.wikipedia.org/wiki/Maze_generation_algorithm#Recursive_backtracker

        Deque<Cell> stack = new ArrayDeque<>();
        Random rand = new Random();

        Cell current;

        // top to bottom
        if (rand.nextBoolean()) {
            // to avoid that the corners are the initial cell
            int x1 = rand.nextInt(size - 1) + 1;
            current = cells[x1];
            // left to right
        } else {
            // to avoid that the corners are the initial cell
            int y1 = rand.nextInt(size - 1) + 1;
            current = cells[y1];
        }


        while (Arrays.stream(cells).anyMatch(c -> !c.visited)) {
            int x = current.x, y = current.y;

            cells[x + y * size].visited = true;

            // get all neighbors
            List<Cell> neighbors = new ArrayList<>(4);

            if (x != 0) neighbors.add(cells[x - 1 + y * size]);
            if (x != size - 1) neighbors.add(cells[x + 1 + y * size]);
            if (y != 0) neighbors.add(cells[x + (y - 1) * size]);
            if (y != size - 1) neighbors.add(cells[x + (y + 1) * size]);

            // get all unvisited neighbors
            neighbors = neighbors.stream().filter(c -> !c.visited).collect(Collectors.toList());


            // if the current cell has unvisited neighbors
            if (neighbors.size() > 0) {

                // choose one at random
                Cell nextCell = neighbors.get(rand.nextInt(neighbors.size()));

                stack.push(current);

                // remove the walls between the cells
                if (x == nextCell.x) {
                    if (y < nextCell.y) {
                        current.borders[Dir.SOUTH.id] = false;
                        nextCell.borders[Dir.NORTH.id] = false;
                    } else {
                        current.borders[Dir.NORTH.id] = false;
                        nextCell.borders[Dir.SOUTH.id] = false;
                    }
                } else {
                    if (x < nextCell.x) {
                        current.borders[Dir.EAST.id] = false;
                        nextCell.borders[Dir.WEST.id] = false;
                    } else {
                        current.borders[Dir.WEST.id] = false;
                        nextCell.borders[Dir.EAST.id] = false;
                    }
                }

                current = nextCell;

                // if the current cell has no unvisited neighbor
            } else {

                // pop a cell from the stack and make it the current cell
                current = stack.pollFirst();
            }
        }
    }

    @Override
    public void draw(Graphics g) {

        for (int i = 0; i < cells.length; i++) {
            Cell cell = cells[i];
            g.drawImage(ImageLoader.sprites.get("cobble"), cell.x * CELL_SIZE, cell.y * CELL_SIZE, CELL_SIZE, CELL_SIZE, null);
           /* g.setColor(Color.GREEN);
            ((Graphics2D) g).setStroke(new BasicStroke(1));
            g.drawRect(cell.x * CELL_SIZE, cell.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);*/
        }

        for (int i = 0; i < cells.length; i++) {
            Cell cell = cells[i];
            final int wallWidth = 5;

            ((Graphics2D) g).setStroke(new BasicStroke(wallWidth));
            if (!cell.discovered) {
                g.setColor(new Color(35, 35, 35));
                if (cell.halfVisible) {
                    g.setColor(new Color(0, 0, 0, 125));
                }
                g.fillRect(cell.x * CELL_SIZE, cell.y * CELL_SIZE,
                        CELL_SIZE, CELL_SIZE);
            }

            g.setColor(((cell.neighbors[Dir.NORTH.id] != null
                    && (cell.neighbors[Dir.NORTH.id].discovered
                    || cell.neighbors[Dir.NORTH.id].halfVisible))
                    || cell.discovered || cell.halfVisible) ? Color.BLACK : new Color(35, 35, 35));

            if (cell.borders[Dir.NORTH.id]) {
                g.drawLine(cell.x * CELL_SIZE, cell.y * CELL_SIZE,
                        (cell.x + 1) * CELL_SIZE, cell.y * CELL_SIZE);
            }

            g.setColor(((cell.neighbors[Dir.EAST.id] != null
                    && (cell.neighbors[Dir.EAST.id].discovered
                    || cell.neighbors[Dir.EAST.id].halfVisible))
                    || cell.discovered || cell.halfVisible) ? Color.BLACK : new Color(35, 35, 35));

            if (cell.borders[Dir.EAST.id]) {
                g.drawLine((cell.x + 1) * CELL_SIZE, cell.y * CELL_SIZE,
                        (cell.x + 1) * CELL_SIZE, (cell.y + 1) * CELL_SIZE);
            }

            g.setColor(((cell.neighbors[Dir.SOUTH.id] != null
                    && (cell.neighbors[Dir.SOUTH.id].discovered
                    || cell.neighbors[Dir.SOUTH.id].halfVisible))
                    || cell.discovered || cell.halfVisible) ? Color.BLACK : new Color(35, 35, 35));


            if (cell.borders[Dir.SOUTH.id]) {
                g.drawLine(cell.x * CELL_SIZE, (cell.y + 1) * CELL_SIZE,
                        (cell.x + 1) * CELL_SIZE, (cell.y + 1) * CELL_SIZE);
            }

            g.setColor(((cell.neighbors[Dir.WEST.id] != null
                    && (cell.neighbors[Dir.WEST.id].discovered
                    || cell.neighbors[Dir.WEST.id].halfVisible))
                    || cell.discovered || cell.halfVisible) ? Color.BLACK : new Color(35, 35, 35));

            if (cell.borders[Dir.WEST.id]) {
                g.drawLine(cell.x * CELL_SIZE, cell.y * CELL_SIZE,
                        cell.x * CELL_SIZE, (cell.y + 1) * CELL_SIZE);
            }
        }

    }

    private class Cell {
        int x, y;
        boolean[] borders = {true, true, true, true};
        boolean visited = false;
        Cell neighbors[] = new Cell[4];

        boolean discovered = false;
        boolean halfVisible = false;

        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
