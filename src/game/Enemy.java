package game;
import java.awt.*;
import java.util.Random;

public class Enemy extends Entity implements Executable {
    protected int health = 40;
    protected int damage = 8;

    public Enemy(Point position) { super(position, 'E'); }

    @Override
    public void interact(Player player) {
        execute(player);
    }

    @Override
    public void execute(Player player) {
        Random rand = new Random();
        System.out.println("Fighting enemy!");
        while (health > 0 && player.getHealth() > 0) {
            health -= player.getDamage();
            System.out.printf("You deal %d (Enemy HP:%d)\n",
                    player.getDamage(), Math.max(0, health));
            if (health > 0) {
                player.takeDamage(damage);
                System.out.printf("Enemy deals %d (Your HP:%d)\n",
                        damage, player.getHealth());
            }
        }
        if (player.getHealth() <= 0) System.exit(0);

        int boost = 1 + rand.nextInt(5);
        player.addDamage(boost);
        System.out.printf("Enemy defeated! +%d damage\n", boost);
        if (health <= 0) {
            player.getGameManager().getCurrentRoom().removeEntity(this);
        }
    }
}