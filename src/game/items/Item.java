package game.items;

import drawing.Drawable;
import game.engine.Entity;
import geometry.Vector2f;

public abstract class Item implements Entity, Drawable {

    protected Vector2f pos = new Vector2f(0, 0);

    public abstract void use();

    public Vector2f getPos() {
        return pos;
    }
}
