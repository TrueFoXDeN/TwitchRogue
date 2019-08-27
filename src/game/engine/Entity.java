package game.engine;

import drawing.Drawable;
import geometry.Vector2f;

public abstract class Entity implements Drawable {

    protected Vector2f pos = new Vector2f(0, 0);

    public Vector2f getPos() {
        return pos;
    }

    public abstract void update(double delta);
}
