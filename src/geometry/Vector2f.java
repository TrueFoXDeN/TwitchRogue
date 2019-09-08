package geometry;

import java.util.Map;

public class Vector2f {

    public double x, y;

    public Vector2f(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void add(Vector2f o) {
        x += o.x;
        y += o.y;
    }

    public Vector2f add_(Vector2f o) {
        return new Vector2f(x + o.x, y + o.y);
    }

    public double dist(Vector2f o) {
        double distX = x - o.x;
        double distY = y - o.y;
        return Math.sqrt(distX * distX + distY * distY);
    }

    public int to1DIndex(int size) {
        return (int)(x) + (int)(y) * size;
    }

    @Override
    public Object clone() {
        return new Vector2f(x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Vector2f)) return false;
        Vector2f o = (Vector2f) obj;
        return o.x == x && o.y == y;
    }
}
