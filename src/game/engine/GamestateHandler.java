package game.engine;

import application.Main;
import drawing.VoteHandler;
import game.Dir;
import game.Player;
import game.arena.Maze;
import io.networking.TwitchConnection;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GamestateHandler {

    // current state of the game
    // TODO: change back to start in menu state
    public static GameState gameState = GameState.EXPLORE;

    public static Maze currentMaze = new Maze(15, 10, 0);

    // entities that live in the current state
    private List<Entity> entities = new CopyOnWriteArrayList<>();

    // player entity is handled separately
    private final Player player = new Player();

    public static VoteHandler voteHandler = new VoteHandler();

    public static TwitchConnection twitchConnection;

    public GamestateHandler() {
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
        if (Main.display.getBackground_() != currentMaze) {
            Main.display.setBackground(currentMaze);
        }
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
    }

    public Player getPlayer() {
        return player;
    }

    public void addGameState(GameState g) {
        gameState = g;
    }

    public GameState getGameState() {
        return gameState;
    }
}
