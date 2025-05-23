package game;
import java.awt.*;

public class Crowbar extends Entity implements Consumable {
    public Crowbar(Point position) { super(position, 'P'); }

    @Override
    public void interact(Player player) {
        consume(player);
    }

    @Override
    public void consume(Player player) {
        player.acquireCrowbar();
        System.out.println("Acquired crowbar!");
        player.getGameManager().getCurrentRoom().removeEntity(this);
    }
}