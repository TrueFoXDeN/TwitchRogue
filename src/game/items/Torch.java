package game.items;

import geometry.Vector2f;
import io.ImageLoader;

import java.awt.*;

public class Torch extends Item {

    public Torch(int x, int y) {
        pos = new Vector2f(x, y);
    }

    @Override
    public void use() {

    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(ImageLoader.sprites.get("fackel"), (int) pos.x, (int) pos.y, null);
    }

    @Override
    public void update(double delta) {

    }
}