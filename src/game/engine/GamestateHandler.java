package game.engine;

import application.GameLoop;
import application.Main;
import drawing.BattleBackground;
import drawing.VoteHandler;
import game.Dir;
import game.Enemy;
import game.Player;
import game.arena.Maze;
import io.networking.TwitchConnection;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class GamestateHandler {

    // current state of the game
    // TODO: change back to start in menu state
    public static GameState gameState = GameState.EXPLORE;

    private Maze currentMaze;

    // entities that live in the current state
    private List<Entity> entities = new CopyOnWriteArrayList<>();

    // player entity is handled separately
    private final Player player = new Player();

    public static VoteHandler voteHandler = new VoteHandler();
    public static TwitchConnection twitchConnection;

    private int battleEnemyStrength = 0;
    private Enemy.EnemyTyp battleEnemyType;

    public GamestateHandler() {
        currentMaze = new Maze(15, 10, 5);
        entities.addAll(currentMaze.getEntities());

        Main.display.getDrawables().add(player);
        Main.display.getDrawables().add(voteHandler);
    }

    public void update(double delta) {

        /*if(twitchConnection != null)
            System.out.println(twitchConnection.getMessages().size());*/

        // update all players and other entities
        player.update(delta);

        for (Entity e : entities) {
            e.update(delta);
        }

        // finite gamestate machine
        switch (gameState) {
            case MENU: {
                updateMenu();
                break;
            }
            case EXPLORE: {
                updateExplore();
                break;
            }
            case BATTLE: {
                updateBattle();
                break;
            }
            case PAUSE: {
                updatePause();
                break;
            }
            case GAME_OVER: {
                updateGameOver();
                break;
            }
        }
    }

    private void updateMenu() {

    }

    private void updateExplore() {
        // TODO: remove this debugging statement
        if (Main.display.getBackground_() != currentMaze)
            Main.display.setBackground(currentMaze);
    }

    private void updateBattle() {

    }

    private void updatePause() {

    }

    private void updateGameOver() {

    }

    public void movePlayer(int dx, int dy) {

        Dir dir = Dir.WEST;
        if (dx == 1) dir = Dir.EAST;
        if (dx == -1) dir = Dir.WEST;
        if (dy == 1) dir = Dir.SOUTH;
        if (dy == -1) dir = Dir.NORTH;

        if (currentMaze.canMove(player.getPos(), dir))
            player.move(dx, dy);

        for (Entity e : entities) {
            if (e instanceof Enemy) ((Enemy) e).move();
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void setGameState(GameState g) {
        gameState = g;
        clearCurrentState();

        switch (gameState) {
            case EXPLORE:
                Main.display.setBackground(currentMaze);
                break;
            case BATTLE:
                Main.display.setBackground(new BattleBackground());
                battleEnemyStrength = 0;
                List<Enemy> attackers = currentMaze.getEntities().stream()
                        .filter(e -> e instanceof Enemy)
                        .map(e -> (Enemy) e)
                        .filter(e -> !e.getPos().equals(player.getPos()))
                        .collect(Collectors.toList());

                // get enemy strength
                attackers.forEach(e -> battleEnemyStrength += e.getType().strenghtID);
                // get the type of the strongest enemy for drawing
                attackers.stream()
                        .max(Comparator.comparingInt(e -> e.getType().strenghtID))
                        .ifPresentOrElse(e -> battleEnemyType = e.getType(),
                                () -> battleEnemyType = Enemy.EnemyTyp.SLIME);

                break;
        }
    }

    private void clearCurrentState() {
        Main.display.getDrawables().clear();
        GameLoop.gHandler.getEntities().clear();
        Main.display.getDrawables().add(voteHandler);
    }

    public GameState getGameState() {
        return gameState;
    }

    public Maze getCurrentMaze() {
        return currentMaze;
    }

    public List<Entity> getEntities() {
        return entities;
    }
}
