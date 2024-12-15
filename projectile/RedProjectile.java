package projectile;

import java.awt.image.BufferedImage;

import main.GamePanel;

public class RedProjectile extends Projectile {

    public RedProjectile(int worldX, int worldY, int speed, String direction, GamePanel gp) {
        super(worldX, worldY, speed, direction, gp);
        img = setup("/assets/projectiles/enemy_projectiles/red_projectile");
    }

    @Override
    protected boolean checkCollisions() {
        return gp.getCollisionChecker().checkTile(this) || gp.getCollisionChecker().checkProjectileCollision(gp.getPlayer(), this);
    }

    protected BufferedImage getImg() {
        return img;
    }
    
}
