package projectile;

import java.awt.image.BufferedImage;

import main.GamePanel;

public class SlimeProjectile extends Projectile {

    public SlimeProjectile(int worldX, int worldY, int speed, String direction, GamePanel gp) {
        super(worldX, worldY, speed, direction, gp);
        img = setup("/assets/projectiles/enemy_projectiles/slime_projectile");
        damage = 3;
    }

    @Override
    protected boolean checkCollisions() {
        return super.checkCollisions() || gp.getCollisionChecker().checkProjectileCollision(gp.getPlayer(), this);
    }

    protected BufferedImage getImg() {
        return img;
    }
    
}
