package game.items;

import io.ImageLoader;

import java.awt.*;

public class Torch extends Item {
    @Override
    public void use() {

    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(ImageLoader.sprites.get("fackel"), (int)pos.x, (int)pos.y, null);
    }

    @Override
    public void update(double delta) {

    }
}
