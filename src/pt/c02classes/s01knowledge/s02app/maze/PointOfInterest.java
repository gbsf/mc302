package pt.c02classes.s01knowledge.s02app.maze;

import pt.c02classes.s01knowledge.s02app.util.Direction;

import java.util.EnumSet;

public class PointOfInterest {

    private final Direction from;
    private final Direction fromRev;
    private EnumSet<Direction> available;
    private int distance;

    public PointOfInterest(Direction from, int distance) {
        this.from = from;
        this.fromRev = from.reverse();
        this.available = null;
        this.distance = distance;
    }

    public Direction getFrom() {
        return from;
    }

    public Direction getFromRev() {
        return fromRev;
    }

    public EnumSet<Direction> getAvailable() {
        return available;
    }

    public void setAvailable(EnumSet<Direction> available) {
        this.available = available;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void incrDistance() {
        this.distance++;
    }

    @Override
    public String toString() {
        return "PointOfInterest{" +
                "from=" + from +
                ", available=" + available +
                ", distance=" + distance +
                '}';
    }
}
