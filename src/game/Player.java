package game;

import java.awt.*;
import java.util.Map;
import java.util.Random;

public class Player {
    private Point position;
    private int health = 100;
    private int maxHealth = 100;
    private int damage = 15;
    private int keys = 1;
    private boolean hasCrowbar = false;
    private GameManager gameManager;
    private Map<Integer, Map<Point, String>> chestContents;
    private String currentWeapon = "Fists"; // Default weapon
    private int baseDamage = 10; // Base damage without weapons

    public Player(Point start) {
        position = start;
    }

    public void setGameManager(GameManager gm, Map<Integer, Map<Point, String>> chestContents) {
        this.gameManager = gm;
        this.chestContents = chestContents;
    }

    public void move(Point newPos) { position = newPos; }
    public void addKey() { keys++; }
    public void useKey() { keys--; }
    public void acquireCrowbar() { hasCrowbar = true; }
    public void takeDamage(int amount) { health = Math.max(0, health - amount); }
    public void addDamage(int amount) { damage += amount; }

    public void interactWithChest(Chest chest) {
        if (!hasCrowbar) {
            System.out.println("You can't open the chest because you have no crowbar.");
            return;
        }

        int roomIndex = gameManager.getWorld().getRoomIndex(gameManager.getCurrentRoom());
        Point p = chest.getPosition();

        if (chestContents.containsKey(roomIndex)) {
            Map<Point, String> roomChests = chestContents.get(roomIndex);
            if (roomChests.containsKey(p)) {
                String itemType = roomChests.remove(p);
                Random rand = new Random();

                if (itemType.equals("health")) {
                    int boost = 10 + rand.nextInt(11);
                    health += boost;
                    if (health > maxHealth) maxHealth = health;
                    System.out.printf("Health Potion! +%d HP (Now %d/%d)\n",
                            boost, health, maxHealth);
                }
                else {
                    Weapon newWeapon = new Weapon();
                    damage += newWeapon.getDamageBoost();
                    System.out.printf("You found %s! Damage +%d (Now: %d)\n",
                            newWeapon.getName(), newWeapon.getDamageBoost(), damage);
                }

                chest.setOpened(true);
                return; // Successfully opened
            }
        }

        // Fallback - shouldn't happen with the new world generation
        System.out.println("The chest was empty...");
        chest.setOpened(true);
    }

    public String getCurrentWeapon() { return currentWeapon; }
    public Point getPosition() { return position; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getDamage() { return damage; }
    public int getKeys() { return keys; }
    public boolean hasCrowbar() { return hasCrowbar; }
    public GameManager getGameManager() { return gameManager; }
}