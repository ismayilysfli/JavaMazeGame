package game;
import java.awt.*;

public class Chest extends Entity implements Activatable {
    private boolean isOpened = false;
    private boolean attemptedWithoutCrowbar = false;

    public Chest(Point position) { super(position, 'C'); }

    @Override
    public void interact(Player player) {
        if (!isOpened) {
            activate(player);
        }
    }

    @Override
    public void activate(Player player) {
        if (player.hasCrowbar()) {
            player.interactWithChest(this);
            setOpened(true);
        } else if (!attemptedWithoutCrowbar) {
            System.out.println("You can't open the chest because you have no crowbar.");
            attemptedWithoutCrowbar = true;
        }
    }

    public boolean isOpened() { return isOpened; }
    public void setOpened(boolean opened) {
        isOpened = opened;
        if (opened) {
            this.symbol = ' '; // Change symbol when opened
        }
    }
}