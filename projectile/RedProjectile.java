package projectile;

import java.awt.image.BufferedImage;

import main.GamePanel;

public class RedProjectile extends Projectile {

    public RedProjectile(int worldX, int worldY, int speed, String direction, GamePanel gp) {
        super(worldX, worldY, speed, direction, gp);
        img = setup("/assets/projectiles/enemy_projectiles/red_projectile");

        damage = 1;
    }

    @Override
    protected boolean checkCollisions() {
        return super.checkCollisions() || gp.getCollisionChecker().checkProjectileCollision(gp.getPlayer(), this);
    }

    protected BufferedImage getImg() {
        return img;
    }
    
}
