package pt.c02classes.s01knowledge.s02app.util;

public enum Direction {
    WEST("oeste"),
    NORTH("norte"),
    EAST("leste"),
    SOUTH("sul");

    private final String dir;

    Direction(String dir) {
        this.dir = dir;
    }

    public String getDir() {
        return dir;
    }

    Direction turn(int steps) {
        int index = (ordinal() + steps) % values().length;
        if (index < 0)
            index += values().length;
        return values()[index];
    }

    public Direction next() {
        return turn(1);
    }

    public Direction previous() {
        return turn(-1);
    }

    public Direction reverse() {
        return turn(2);
    }
}
