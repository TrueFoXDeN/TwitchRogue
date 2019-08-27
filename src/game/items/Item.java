package game.items;

import drawing.Drawable;
import game.engine.Entity;
import geometry.Vector2f;

public abstract class Item extends Entity {
    public abstract void use();
}
