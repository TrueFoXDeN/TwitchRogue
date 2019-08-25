package game.arena;

import drawing.Drawable;
import game.Player;
import game.engine.Entity;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
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

        for(int i = 0; i < cells.length; ++i) {
            cells[i] = new Cell(i % size, i / size);
        }

        generateMaze();
    }

    public void updateVision(Player player) {
        int x = (int)player.getPos().getX();
        int y = (int)player.getPos().getY();

        int tempX = x;
        int tempY = y;


        // right
        while(tempX++ < size) {
            Cell nextCell = cells[tempX + tempY * size];
            nextCell.discovered = true;
            if(nextCell.borders[Cell.E]) break;
        }

        tempX = x;
        tempY = y;

        // left
        while(tempX-- >= 0) {
            Cell nextCell = cells[tempX + tempY * size];
            nextCell.discovered = true;
            if(nextCell.borders[Cell.W]) break;
        }

        tempX = x;
        tempY = y;

        // up
        while(tempY-- >= 0) {
            Cell nextCell = cells[tempX + tempY * size];
            nextCell.discovered = true;
            if(nextCell.borders[Cell.N]) break;
        }

        tempX = x;
        tempY = y;

        // right
        while(tempY++ < size) {
            Cell nextCell = cells[tempX + tempY * size];
            nextCell.discovered = true;
            if(nextCell.borders[Cell.S]) break;
        }
    }

    private void generateMaze() {
        // source of algorithm: https://en.wikipedia.org/wiki/Maze_generation_algorithm#Recursive_backtracker

        Deque<Cell> stack = new ArrayDeque<>();
        Random rand = new Random();

        Cell current;

        // top to bottom
        if(rand.nextBoolean()) {
            // to avoid that the corners are the initial cell
            int x1 = rand.nextInt(size - 1) + 1;
            current = cells[x1];
        // left to right
        } else {
            // to avoid that the corners are the initial cell
            int y1 = rand.nextInt(size - 1) + 1;
            current = cells[y1];
        }


        while(Arrays.stream(cells).anyMatch(c -> !c.visited)) {
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
            if(neighbors.size() > 0) {

                // choose one at random
                Cell nextCell = neighbors.get(rand.nextInt(neighbors.size()));

                stack.push(current);

                // remove the walls between the cells
                if (x == nextCell.x) {
                    if (y < nextCell.y) {
                        current.borders[Cell.S] = false;
                        nextCell.borders[Cell.N] = false;
                    } else {
                        current.borders[Cell.N] = false;
                        nextCell.borders[Cell.S] = false;
                    }
                } else {
                    if (x < nextCell.x) {
                        current.borders[Cell.E] = false;
                        nextCell.borders[Cell.W] = false;
                    } else {
                        current.borders[Cell.W] = false;
                        nextCell.borders[Cell.E] = false;
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
            g.setColor(Color.LIGHT_GRAY);
            ((Graphics2D) g).setStroke(new BasicStroke(1));
            g.drawRect(cell.x * CELL_SIZE, cell.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }

        for(int i = 0; i < cells.length; i++) {
            Cell cell = cells[i];

            g.setColor(Color.BLACK);
            ((Graphics2D) g).setStroke(new BasicStroke(5));
            if(!cell.discovered){
                g.fillRect(cell.x * CELL_SIZE, cell.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }

            if(cell.borders[Cell.N]) {
                g.drawLine(cell.x * CELL_SIZE, cell.y * CELL_SIZE,
                        (cell.x + 1) * CELL_SIZE, cell.y * CELL_SIZE);
            }
            if(cell.borders[Cell.E]) {
                g.drawLine((cell.x + 1) * CELL_SIZE, cell.y * CELL_SIZE,
                        (cell.x + 1) * CELL_SIZE, (cell.y + 1) * CELL_SIZE);
            }
            if(cell.borders[Cell.S]) {
                g.drawLine(cell.x * CELL_SIZE, (cell.y + 1) * CELL_SIZE,
                        (cell.x + 1) * CELL_SIZE, (cell.y + 1) * CELL_SIZE);
            }
            if(cell.borders[Cell.W]) {
                g.drawLine(cell.x * CELL_SIZE, cell.y * CELL_SIZE,
                        cell.x * CELL_SIZE, (cell.y + 1) * CELL_SIZE);
            }
        }
    }

    private class Cell {
        final static int N = 0, E = 1, S = 2, W = 3;

        int x, y;
        boolean[] borders = {true, true, true, true};
        boolean visited = false;

        boolean discovered = false;

        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
