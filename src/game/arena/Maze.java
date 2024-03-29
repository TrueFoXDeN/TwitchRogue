package game.arena;

import application.GameLoop;
import application.Main;
import drawing.Drawable;
import game.Dir;
import game.Enemy;
import game.algorithms.A_Star;
import game.engine.Entity;
import game.items.Item;
import game.items.Potion;
import game.items.Torch;
import geometry.Vector2f;
import io.ImageLoader;

import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Maze implements Drawable {
    public int width, height;
    private final int MAX_ENEMIES;
    private final int MAX_ITEMS = 5;

    // size in which the cells should be drawn
    public static int CELL_SIZE = 0;

    // items and enemies
    private final List<Entity> entities = new CopyOnWriteArrayList<>();
    private final Cell[] cells;

    private A_Star a_star;

    public Maze(int width, int height, int MAX_ENEMIES) {
        this.width = width;
        this.height = height;
        this.MAX_ENEMIES = MAX_ENEMIES;

        CELL_SIZE = 720 / height;

        cells = new Cell[width * height];

        for (int i = 0; i < cells.length; ++i) {
            cells[i] = new Cell(i % width, i / width);
        }

        for (int i = 0; i < cells.length; ++i) {
            Cell cell = cells[i];

            if (cell.x > 0) {
                cell.neighbors[Dir.WEST.id] = cells[cell.x - 1 + cell.y * width];
            }
            if (cell.x < width - 1) {
                cell.neighbors[Dir.EAST.id] = cells[cell.x + 1 + cell.y * width];
            }
            if (cell.y > 0) {
                cell.neighbors[Dir.NORTH.id] = cells[cell.x + (cell.y - 1) * width];
            }
            if (cell.y > height - 1) {
                cell.neighbors[Dir.SOUTH.id] = cells[cell.x + (cell.y + 1) * width];
            }
        }


        generateMaze();
        updateVision(new Vector2f(0, 0));
        a_star = new A_Star(this);
        addItems();
        addEnemies();
    }

    public boolean canMove(Vector2f pos, Dir dir) {
        return !cells[pos.to1DIndex(width)].borders[dir.id];
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public A_Star getA_star() {
        return a_star;
    }

    public void updateVision(Vector2f playerPos) {
        int x = (int) playerPos.x;
        int y = (int) playerPos.y;

        var revealNeighbor = new Function<Vector2f, Void>() {
            @Override
            public Void apply(Vector2f playerPos1) {
                int x = (int) playerPos1.x;
                int y = (int) playerPos1.y;

                if (playerPos1.x > 0 && !cells[playerPos1.to1DIndex(width)].borders[Dir.WEST.id])
                    cells[playerPos1.add_(new Vector2f(-1, 0)).to1DIndex(width)].halfVisible = true;

                if (playerPos1.x < width - 1 && !cells[x + y * width].borders[Dir.EAST.id])
                    cells[playerPos1.add_(new Vector2f(1, 0)).to1DIndex(width)].halfVisible = true;

                if (playerPos1.y > 0 && !cells[x + y * width].borders[Dir.NORTH.id])
                    cells[playerPos1.add_(new Vector2f(0, -1)).to1DIndex(width)].halfVisible = true;

                if (playerPos1.y < height - 1 && !cells[x + y * width].borders[Dir.SOUTH.id])
                    cells[playerPos1.add_(new Vector2f(0, 1)).to1DIndex(width)].halfVisible = true;

                return null;
            }
        };

        cells[x + y * width].discovered = true;
        revealNeighbor.apply(playerPos);

        int tempX = x;
        int tempY = y;


        // right
        do {
            Cell nextCell = cells[tempX + tempY * width];
            nextCell.discovered = true;
            revealNeighbor.apply(new Vector2f(tempX, tempY));
            if (nextCell.borders[Dir.EAST.id]) break;
        } while (tempX++ < width - 1);

        tempX = x;
        tempY = y;

        // left
        do {
            Cell nextCell = cells[tempX + tempY * width];
            nextCell.discovered = true;
            revealNeighbor.apply(new Vector2f(tempX, tempY));
            if (nextCell.borders[Dir.WEST.id]) break;
        } while (tempX-- > 0);

        tempX = x;
        tempY = y;

        // up
        do {
            Cell nextCell = cells[tempX + tempY * width];
            nextCell.discovered = true;
            revealNeighbor.apply(new Vector2f(tempX, tempY));
            if (nextCell.borders[Dir.NORTH.id]) break;
        } while (tempY-- > 0);

        tempX = x;
        tempY = y;

        // right
        do {
            Cell nextCell = cells[tempX + tempY * width];
            nextCell.discovered = true;
            revealNeighbor.apply(new Vector2f(tempX, tempY));
            if (nextCell.borders[Dir.SOUTH.id]) break;
        } while (tempY++ < height - 1);


        //TORCH
        if (GameLoop.gHandler != null && GameLoop.gHandler.getPlayer() != null) {
            if (GameLoop.gHandler.getPlayer().useTorch) {

                Vector2f aroundPos[] = {
                        new Vector2f(0, -1),
                        new Vector2f(1, -1),
                        new Vector2f(1, 0),
                        new Vector2f(1, 1),
                        new Vector2f(0, 1),
                        new Vector2f(-1, 1),
                        new Vector2f(-1, 0),
                        new Vector2f(-1, -1)
                };

                for (int i = 0; i < aroundPos.length; i++) {
                    Vector2f neighborPos = playerPos.add_(aroundPos[i]);
                    if (neighborPos.x >= 0 && neighborPos.x < width && neighborPos.y >= 0 && neighborPos.y < height) {
                        if (!cells[neighborPos.to1DIndex(width)].discovered) {
                            cells[neighborPos.to1DIndex(width)].halfVisible = true;
                        }
                    }


                }
            }

        }
    }

    private void generateMaze() {
        // source of algorithm: https://en.wikipedia.org/wiki/Maze_generation_algorithm#Recursive_backtracker

        Deque<Cell> stack = new ArrayDeque<>();
        Random rand = new Random();

        Cell current;

        // top to bottom
        if (rand.nextBoolean()) {
            // to avoid that the corners are the initial cell
            int x1 = rand.nextInt(width - 1) + 1;
            current = cells[x1];
            // left to right
        } else {
            // to avoid that the corners are the initial cell
            int y1 = rand.nextInt(height - 1) + 1;
            current = cells[y1];
        }


        while (Arrays.stream(cells).anyMatch(c -> !c.visited)) {
            int x = current.x, y = current.y;

            cells[x + y * width].visited = true;

            // get all neighbors
            List<Cell> neighbors = new ArrayList<>(4);

            if (x != 0) neighbors.add(cells[x - 1 + y * width]);
            if (x != width - 1) neighbors.add(cells[x + 1 + y * width]);
            if (y != 0) neighbors.add(cells[x + (y - 1) * width]);
            if (y != height - 1) neighbors.add(cells[x + (y + 1) * width]);

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

    private void addItems() {
        List<Item> items = new ArrayList<>(MAX_ITEMS);
        Random rand = new Random();

        double x = rand.nextInt(width - 2) + 1;
        double y = rand.nextInt(height - 2) + 1;

        items.add(new Torch(x, y));

        for (int i = 0; i < MAX_ITEMS - 1; ++i) {
            x = rand.nextInt(width - 2) + 1;
            y = rand.nextInt(height - 2) + 1;

            while (true) {
                boolean posValid = true;
                for (Item item : items) {
                    if (item.getPos().equals(new Vector2f(x, y))) {
                        posValid = false;
                        break;
                    }
                }

                if (posValid) break;

                x = rand.nextInt(width - 2) + 1;
                y = rand.nextInt(height - 2) + 1;
            }

            items.add(new Potion(x, y));
        }

        entities.addAll(items);
    }

    private void addEnemies() {
        List<Enemy> enemies = new ArrayList<>(MAX_ENEMIES);
        Random rand = new Random();

        double x = rand.nextInt(width - 2) + 1;
        double y = rand.nextInt(height - 2) + 1;


        for (int i = 0; i < MAX_ENEMIES; ++i) {
            while (true) {
                boolean posValid = true;
                for (Enemy enemy : enemies) {
                    if (enemy.getPos().equals(new Vector2f(x, y))) {
                        posValid = false;
                        break;
                    }
                }

                if (posValid) break;

                x = rand.nextInt(width - 2) + 1;
                y = rand.nextInt(height - 2) + 1;
            }

            enemies.add(new Enemy(new Vector2f(x, y)));
        }
        entities.addAll(enemies);
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

        // draw all non enemy entities
        entities.forEach(entity -> {
            if (!(entity instanceof Enemy) && cells[entity.getPos().to1DIndex(width)].discovered)
                entity.draw(g);
        });


        // draw the enemies
        // if more than one enemy has the same position
        // draw only the strongest enemy and the number of enemies o this cell
        Map<Vector2f, List<Enemy>> entityWithSamePos = new ConcurrentHashMap<>();
        entities.forEach(entity -> {
            if (!(entity instanceof Enemy)) return;

            boolean foundKey = false;
            for (Vector2f key : entityWithSamePos.keySet()) {
                if (entity.getPos().dist(key) <= 0.2) {
                    entityWithSamePos.get(key).add((Enemy) entity);
                    foundKey = true;
                    break;
                }
            }

            if (!foundKey) {
                List<Enemy> temp = new LinkedList<>();
                temp.add((Enemy) entity);
                entityWithSamePos.put(entity.getPos(), temp);
            }
        });

        g.setFont(new Font("Courier New", Font.BOLD, 24));
        FontMetrics fm = g.getFontMetrics();
        for (var p : entityWithSamePos.entrySet())
            p.getValue().stream().max(Comparator.comparingInt(e -> e.getType().strenghtID)
            ).ifPresent(enemy -> {
                if(cells[enemy.getNextPos().to1DIndex(width)].discovered) {
                    enemy.draw(g);
                    g.drawString(String.valueOf(p.getValue().size()),
                            (int) (enemy.getPos().x * CELL_SIZE) + fm.stringWidth(String.valueOf(p.getValue().size())),
                            (int) (enemy.getPos().y * CELL_SIZE) + fm.getHeight());
                }
            });

    }

    private class Cell {
        int x, y;
        boolean[] borders = {true, true, true, true};
        boolean visited = false;
        Cell neighbors[] = new Cell[4];

        boolean discovered = Main.DEBUGGING_VISION;
        boolean halfVisible = Main.DEBUGGING_VISION;

        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
