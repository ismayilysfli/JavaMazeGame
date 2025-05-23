package game;

import java.awt.*;
import java.util.Random;

public class DoorConnection {
    private Room roomA, roomB;
    private Point doorA, doorB;

    public DoorConnection(Room a, Room b, char sideA, char sideB) {
        this.roomA = a;
        this.roomB = b;
        this.doorA = calculateDoorPosition(sideA);
        this.doorB = calculateDoorPosition(sideB);
        placeDoors();
    }

    private Point calculateDoorPosition(char side) {
        Random rand = new Random();
        return switch (side) {
            case 'N' -> new Point(0, rand.nextInt(4) + 1);
            case 'S' -> new Point(5, rand.nextInt(4) + 1);
            case 'W' -> new Point(rand.nextInt(4) + 1, 0);
            case 'E' -> new Point(rand.nextInt(4) + 1, 5);
            default -> throw new IllegalArgumentException();
        };
    }

    private void placeDoors() {
        roomA.addDoor(doorA);
        roomB.addDoor(doorB);
    }

    public boolean isConnection(Room room, Point position) {
        return (room == roomA && position.equals(doorA)) || (room == roomB && position.equals(doorB));
    }

    public Room getOtherRoom(Room current) {
        return (current == roomA) ? roomB : roomA;
    }

    public Point getOtherPosition(Room current) {
        return (current == roomA) ? doorB : doorA;
    }
}