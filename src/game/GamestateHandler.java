package game;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GamestateHandler {
    private GameState gameState = GameState.MAIN;
    private List<Entity> entities = new CopyOnWriteArrayList<>();

    public void update() {
        for(Entity e: entities) {
            e.update();
        }
    }

    public void addGameState(GameState g) {
        gameState = g;
    }

    public GameState getGameState() {
        return gameState;
    }
}
