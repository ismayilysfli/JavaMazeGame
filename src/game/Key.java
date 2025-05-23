package game;
import java.awt.*;

public class Key extends Entity implements Consumable {
    public Key(Point position) { super(position, 'K'); }

    @Override
    public void interact(Player player) {
        consume(player);
    }

    @Override
    public void consume(Player player) {
        player.addKey();
        System.out.println("Picked up a key!");
        player.getGameManager().getCurrentRoom().removeEntity(this);
    }
}