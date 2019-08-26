package game;

public enum Dir {
    NORTH(0), EAST(1), SOUTH(2), WEST(3);

    public int id;

    private Dir(int id) {
        this.id = id;
    }

}
