package game;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
public class Weapon {
    private static final List<String> WEAPON_NAMES = Arrays.asList(
            "Katana", "Longsword", "Sharp Knife", "Battle Axe", "Warhammer",
            "Rapier", "Dagger", "Scimitar", "Greatsword", "Mace",
            "Flail", "Halberd", "Spear", "Glaive", "Estoc"
    );

    private static final List<Integer> DAMAGE_VALUES = Arrays.asList(
            5, 8, 3, 10, 12,
            6, 4, 7, 15, 9,
            11, 13, 7, 14, 6
    );

    private static List<String> availableWeapons = new ArrayList<>(WEAPON_NAMES);
    private static Random random = new Random();

    private String name;
    private int damageBoost;

    public Weapon() {
        if (availableWeapons.isEmpty()) {
            // If all weapons found, reuse them but with half effect
            resetAvailableWeapons();
            this.damageBoost = random.nextInt(3) + 1; // Small boost
        } else {
            int index = random.nextInt(availableWeapons.size());
            this.name = availableWeapons.remove(index);
            this.damageBoost = DAMAGE_VALUES.get(WEAPON_NAMES.indexOf(name));
        }
    }

    public String getName() {
        return name;
    }

    public int getDamageBoost() {
        return damageBoost;
    }

    public static void resetAvailableWeapons() {
        availableWeapons = new ArrayList<>(WEAPON_NAMES);
    }
}