package game;

import java.awt.*;
import java.util.*;
import java.util.List;

public class World {
    private Room[] rooms = new Room[7];
    private List<DoorConnection> doorConnections = new ArrayList<>();
    private Map<Integer, Map<Point, String>> chestContents = new HashMap<>();
    private Random random = new Random();

    public World() {
        Weapon.resetAvailableWeapons(); // Reset weapons when generating new world
        for (int i = 0; i < 7; i++) {
            rooms[i] = new Room();
            rooms[i].setIndex(i);
        }
        createConnections();
        placeEntities();
        assignChests();
    }

    private void createConnections() {
        int[][] adjacencies = {{0, 1, 'E', 'W'}, {1, 2, 'E', 'W'}, {2, 3, 'S', 'N'},
                {3, 4, 'E', 'W'}, {4, 5, 'S', 'N'}, {3, 6, 'S', 'N'}};
        for (int[] adj : adjacencies) {
            doorConnections.add(new DoorConnection(rooms[adj[0]], rooms[adj[1]], (char) adj[2], (char) adj[3]));
        }
    }

    private void placeEntities() {
        rooms[0].getGrid()[3][3] = ' ';
        Point crowbarPos;
        do {
            crowbarPos = rooms[0].getRandomPosition();
        } while (crowbarPos.equals(new Point(3, 3)));
        rooms[0].addEntity(new Crowbar(crowbarPos));
        rooms[5].addEntity(new Boss(new Point(3, 3)));
        for (Room room : rooms) {
            int items = random.nextInt(4) + 2;
            for (int i = 0; i < items; i++) {
                Point pos = room.getRandomPosition();
                if (room.getGrid()[pos.x][pos.y] == ' ') {
                    switch (random.nextInt(3)) {
                        case 0: room.addEntity(new Enemy(pos)); break;
                        case 1: room.addEntity(new Key(pos)); break;
                        case 2: room.addEntity(new Chest(pos)); break;
                    }
                }
            }
        }
    }

    public boolean verifyChestContents() {
        int weaponChests = 0;
        int healthChests = 0;

        for (Map<Point, String> room : chestContents.values()) {
            for (String type : room.values()) {
                if (type.equals("damage")) weaponChests++;
                else if (type.equals("health")) healthChests++;
            }
        }

        System.out.println("Weapon chests: " + weaponChests);
        System.out.println("Health chests: " + healthChests);
        return weaponChests >= 1 && healthChests >= 1;
    }
    private void assignChests() {
        // First ensure at least one weapon and one health chest per world
        boolean hasWeapon = false;
        boolean hasHealth = false;

        for (int r = 0; r < 7; r++) {
            chestContents.putIfAbsent(r, new HashMap<>());
            for (Entity entity : rooms[r].getEntities()) {
                if (entity instanceof Chest) {
                    Point p = entity.getPosition();

                    // First 2 chests guarantee one weapon and one health
                    if (!hasWeapon) {
                        chestContents.get(r).put(p, "damage");
                        hasWeapon = true;
                    }
                    else if (!hasHealth) {
                        chestContents.get(r).put(p, "health");
                        hasHealth = true;
                    }
                    else {
                        // Random 70/30 split for remaining chests
                        chestContents.get(r).put(p,
                                random.nextFloat() < 0.7 ? "damage" : "health");
                    }
                }
            }
        }
    }

    public boolean checkBalance() {
        int doors = 0, keys = 0;
        for (Room room : rooms) {
            for (char[] row : room.getGrid()) {
                for (char c : row) {
                    if (c == 'D') doors++;
                    if (c == 'K') keys++;
                }
            }
        }
        return keys >= doors - 1;
    }

    public Room getStartingRoom() { return rooms[0]; }
    public List<DoorConnection> getDoorConnections() { return doorConnections; }
    public int getRoomIndex(Room room) {
        for (int i = 0; i < rooms.length; i++) {
            if (rooms[i] == room) return i;
        }
        return -1;
    }
    public Map<Integer, Map<Point, String>> getChestContents() { return chestContents; }
}