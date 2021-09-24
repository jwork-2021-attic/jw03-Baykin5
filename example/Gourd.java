package example;

import example.Line.Position;

public enum Gourd implements Linable {

    ONE(204, 0, 0),

    TWO(255, 165, 0),

    THREE(252, 233, 79),

    FOUR(78, 154, 6),

    FIVE(50, 175, 255),

    SIX(114, 159, 207),

    SEVEN(173, 127, 168);

    private final int r;
    private final int g;
    private final int b;

    private Position position;

    Gourd(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public static Gourd getGourdByRank(int rank) {

        for (Gourd gourd : Gourd.values()) {
            if (gourd.rank() == rank) {
                return gourd;
            }
        }
        return null;

    }

    public int rank() {
        return this.ordinal() + 1;
    }

    @Override
    public String toString() {
        return "\033[48;2;" + this.r + ";" + this.g + ";" + this.b + ";38;2;0;0;0m    " + this.rank() + "  \033[0m";
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public Position getPosition() {
        return this.position;
    }

    public void swapPosition(Gourd another) {
        Position p = another.position;
        this.position.setLinable(another);
        p.setLinable(this);
    }

    @Override
    public int getValue() {
        return this.rank();
    }

}