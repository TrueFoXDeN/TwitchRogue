package game.engine;

import drawing.Drawable;
import game.Player;
import geometry.Vector2f;

import static application.GameLoop.gHandler;

public abstract class Entity implements Drawable {

    protected Vector2f pos = new Vector2f(0, 0);

    public Vector2f getPos() {
        return pos;
    }

    public abstract void interact(Player player);

    public void update(double delta) {
        if (pos.equals(gHandler.getPlayer().getPos())) {
            interact(gHandler.getPlayer());
        }
    }
}
