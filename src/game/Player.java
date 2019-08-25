package game;

import drawing.Drawable;
import game.engine.Entity;

import java.awt.*;
import java.awt.geom.Point2D;

public class Player implements Entity, Drawable {

    // relative position on the map
    private Point2D pos;

    // absolute position used for drawing, anim, ...
    private double x, y;

    @Override
    public void draw(Graphics g) {

    }

    @Override
    public void update() {

    }
}
