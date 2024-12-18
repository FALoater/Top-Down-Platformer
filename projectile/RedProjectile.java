package projectile;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class RedProjectile extends Projectile {

    public RedProjectile(int worldX, int worldY, int speed, String direction, GamePanel gp, int damage) {
        super(worldX, worldY, speed, direction, gp, damage);
        img = setup("/assets/projectiles/enemy_projectiles/red_projectile");

        // give projectile smaller hitbox to match the sprite
        solidArea = new Rectangle(12, 18,23,23);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    @Override
    protected void checkEntityCollisions() {
        markedForDeletion = gp.getCollisionChecker().checkProjectileCollision(gp.getPlayer(), this);
    }

    @Override
    protected boolean checkWallCollisions() {
        return gp.getCollisionChecker().checkTile(this);
    }

    protected BufferedImage getImg() {
        return img;
    }
    
}
