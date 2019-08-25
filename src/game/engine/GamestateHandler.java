package game.engine;

import application.Main;
import game.Player;
import game.arena.Maze;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GamestateHandler {

    // current state of the game
    private GameState gameState = GameState.MENU;

    private Maze currentMaze;

    // entities that live in the current state
    private List<Entity> entities = new CopyOnWriteArrayList<>();

    // player entity is handled separately
    private final Player player = new Player();

    public GamestateHandler() {
        Main.display.getDrawables().add(player);
    }

    public void update() {

        // update all players and other entities
        player.update();

        for(Entity e: entities) {
            e.update();
        }

        // finite gamestate machine
        switch(gameState) {
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

    }

    private void updateBattle() {

    }

    private void updatePause() {

    }

    private void updateGameOver() {

    }

    public void addGameState(GameState g) {
        gameState = g;
    }

    public GameState getGameState() {
        return gameState;
    }
}
