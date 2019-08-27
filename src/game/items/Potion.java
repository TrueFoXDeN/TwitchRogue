package game.items;

import io.ImageLoader;

import java.awt.*;

public class Potion extends  Item{

    @Override
    public void use() {

    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(ImageLoader.sprites.get("potion"), (int)pos.x, (int)pos.y, null);
    }

    @Override
    public void update(double delta) {

    }
}
