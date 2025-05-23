package game;
import java.awt.*;
import java.util.Random;

public class Boss extends Enemy {
    public Boss(Point position) {
        super(position);
        health = 100;
        damage = 15 + new Random().nextInt(16);
        symbol = 'B';
    }

    @Override
    public void execute(Player player) {
        System.out.println("Fighting BOSS!");
        super.execute(player);
        player.getGameManager().printVictoryBanner();
    }
}