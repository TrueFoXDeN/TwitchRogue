package game;

import geometry.Vector2f;

public enum Dir {
    NORTH(0), EAST(1), SOUTH(2), WEST(3);

    public int id;

    private Dir(int id) {
        this.id = id;
    }
    public static Dir vec2fToDir(Vector2f v) {
        if(v.equals(new Vector2f(0, -1))) return NORTH;
        if(v.equals(new Vector2f(0, 1))) return SOUTH;
        if(v.equals(new Vector2f(1, 0))) return EAST;
        if(v.equals(new Vector2f(-1, 0))) return WEST;

        throw new IllegalArgumentException("Cant get direction from " + v.toString());
    }

}
