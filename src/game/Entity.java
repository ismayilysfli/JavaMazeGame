package game;

import java.awt.*;

public abstract class Entity {
    protected Point position;
    protected char symbol;

    public Entity(Point position, char symbol) {
        this.position = position;
        this.symbol = symbol;
    }

    public abstract void interact(Player player);
    public Point getPosition() { return position; }
    public char getSymbol() { return symbol; }
}