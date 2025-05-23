package game;

import java.awt.*;
import java.util.*;
import java.util.List;

public class GameManager {
    private static final int GRID_SIZE = 6;
    private World world;
    private Player player;
    private Room currentRoom;
    private List<DoorConnection> doors;
    private Set<DoorConnection> openedDoors = new HashSet<>();
    private Random random = new Random();

    public GameManager() {
        do {
            world = new World();
        } while (!world.checkBalance());
        player = new Player(new Point(3, 3));
        player.setGameManager(this, world.getChestContents());
        currentRoom = world.getStartingRoom();
        doors = world.getDoorConnections();
    }

    public void startGame() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            display();
            System.out.print("Move (WASD/Q): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("q")) {
                System.out.println("Thanks for playing!");
                break;
            }
            if (!input.isEmpty()) {
                handleInput(input.charAt(0));
            }
            clearConsole();
        }
        scanner.close();
    }

    private void handleInput(char direction) {
        Point originalPos = new Point(player.getPosition());
        Point newPos = calculateNewPosition(direction);

        if (isValidMove(newPos)) {
            boolean movedThroughDoor = handleEntityInteraction(newPos);
            boolean blockMovement = shouldBlockMovement(newPos);

            if (!movedThroughDoor) {
                if (blockMovement) {
                    player.move(originalPos); // Revert position
                } else {
                    player.move(newPos);
                }
            }
        }
    }
    private boolean shouldBlockMovement(Point position) {
        for (Entity entity : currentRoom.getEntities()) {
            if (entity.getPosition().equals(position)) {
                if (entity instanceof Chest) {
                    return !((Chest) entity).isOpened();
                }
                if (entity instanceof Enemy) {
                    return ((Enemy) entity).health > 0;
                }
            }
        }
        return false;
    }

    private Point calculateNewPosition(char dir) {
        Point pos = player.getPosition();
        return switch (dir) {
            case 'w' -> new Point(pos.x - 1, pos.y);
            case 's' -> new Point(pos.x + 1, pos.y);
            case 'a' -> new Point(pos.x, pos.y - 1);
            case 'd' -> new Point(pos.x, pos.y + 1);
            default -> pos;
        };
    }

    private boolean isValidMove(Point pos) {
        return pos.x >= 0 && pos.x < GRID_SIZE &&
                pos.y >= 0 && pos.y < GRID_SIZE &&
                currentRoom.getGrid()[pos.x][pos.y] != '#';
    }

    private boolean handleEntityInteraction(Point position) {
        boolean movedThroughDoor = false;
        boolean chestInteractionFailed = false;

        for (Entity entity : new ArrayList<>(currentRoom.getEntities())) {
            if (entity.getPosition().equals(position)) {
                entity.interact(player);

                if (entity instanceof Chest) {
                    chestInteractionFailed = !((Chest) entity).isOpened();
                }

                // Existing removal logic
                if ((entity instanceof Consumable && !(entity instanceof Chest)) ||
                        (entity instanceof Chest && ((Chest) entity).isOpened()) ||
                        (entity instanceof Enemy && ((Enemy) entity).health <= 0)) {
                    currentRoom.removeEntity(entity);
                }
            }
        }

        // Door handling remains unchanged
        for (DoorConnection door : doors) {
            if (door.isConnection(currentRoom, position)) {
                handleDoorInteraction(door);
                movedThroughDoor = true;
                break;
            }
        }

        return movedThroughDoor || chestInteractionFailed;
    }

    private void handleDoorInteraction(DoorConnection door) {
        if (openedDoors.contains(door)) {
            Point newPosition = door.getOtherPosition(currentRoom);
            currentRoom = door.getOtherRoom(currentRoom);
            player.move(newPosition);
            System.out.println("Entered Room " + (world.getRoomIndex(currentRoom) + 1));
        } else if (player.getKeys() > 0) {
            player.useKey();
            openedDoors.add(door);
            Point newPosition = door.getOtherPosition(currentRoom);
            currentRoom = door.getOtherRoom(currentRoom);
            player.move(newPosition);
            System.out.println("Used a key! Entered Room " + (world.getRoomIndex(currentRoom) + 1));
        } else {
            System.out.println("Need a key!");
        }
    }

    private void display() {
        clearConsole();
        System.out.println("Room " + (world.getRoomIndex(currentRoom) + 1));
        char[][] grid = currentRoom.getGridClone();
        Point pos = player.getPosition();
        grid[pos.x][pos.y] = '@';

        for (char[] row : grid) {
            for (char c : row) {
                System.out.print(c + " ");
            }
            System.out.println();
        }

        System.out.printf("Health: %d/%d | Weapon: %s (%d damage) | Keys: %d | Crowbar: %s%n",
                player.getHealth(), player.getMaxHealth(),
                player.getCurrentWeapon(), player.getDamage(),
                player.getKeys(), player.hasCrowbar() ? "Yes" : "No");
    }

    private void clearConsole() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception e) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }

    public void printVictoryBanner() {
        System.out.println("\n" +
                "**            **  **  ********* *********  ***********  ********    ***         ***\n" +
                " **          **   **  **           ***     **       **  **    **     ***       ***\n" +
                "  **        **    **  **           ***     **       **  ********      ***     ***\n" +
                "   **      **     **  **           ***     **       **  ***            ***   ***\n" +
                "    **    **      **  **           ***     **       **  **  ***           ***\n" +
                "     **  **       **  **           ***     **       **  **    ***         ***\n" +
                "       **         **  *********    ***     ***********  **      ***       ***\n");
        System.out.println("CONGRATULATIONS! You have conquered the labyrinth and emerged victorious!");
        System.out.print("Play again? (Y/N): ");
        String resp = new Scanner(System.in).nextLine().trim().toUpperCase();
        if (resp.equals("Y")) {
            new GameManager().startGame();
        } else {
            System.out.println("Thanks for playing!");
            System.exit(0);
        }
    }

    public World getWorld() {
        return world;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }
}