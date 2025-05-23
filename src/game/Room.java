package game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

class Room {
    private char[][] grid = new char[6][6];
    private List<Entity> entities = new ArrayList<>();
    private int index;

    public Room() {
        initializeWalls();
    }

    private void initializeWalls() {
        for (int i = 0; i < 6; i++) {
            Arrays.fill(grid[i], '#');
            for (int j = 1; j < 5; j++) {
                if (i > 0 && i < 5) grid[i][j] = ' ';
            }
        }
    }

    public void addEntity(Entity entity) {
        Point pos = entity.getPosition();
        if (grid[pos.x][pos.y] == ' ') {
            entities.add(entity);
            grid[pos.x][pos.y] = entity.getSymbol();
        }
    }

    public void removeEntity(Entity entity) {
        if (entities.remove(entity)) {
            grid[entity.getPosition().x][entity.getPosition().y] = ' ';
        }
    }

    public void addDoor(Point pos) {
        grid[pos.x][pos.y] = 'D';
    }

    public char[][] getGrid() { return grid; }
    public char[][] getGridClone() { return Arrays.stream(grid).map(char[]::clone).toArray(char[][]::new); }
    public List<Entity> getEntities() { return entities; }
    public Point getRandomPosition() {
        Random rand = new Random();
        return new Point(rand.nextInt(4) + 1, rand.nextInt(4) + 1);
    }
    public void setIndex(int index) { this.index = index; }
}