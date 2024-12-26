package projectile;

import java.awt.image.BufferedImage;

import entity.enemy.Enemy;
import main.GamePanel;

public class FireProjectile extends Projectile{
    
    public FireProjectile(int worldX, int worldY, int speed, String direction, GamePanel gp) {
        super(worldX, worldY, speed, direction, gp);
        loadImgs();

        damage = 2;
    }

    private void loadImgs() {
        up1 = setup("/assets/projectiles/player_projectiles/fireball_up_1"); 
        down1 = setup("/assets/projectiles/player_projectiles/fireball_down_1"); 
        left1 = setup("/assets/projectiles/player_projectiles/fireball_left_1"); 
        right1 = setup("/assets/projectiles/player_projectiles/fireball_right_1"); 
    }

    @Override
    protected BufferedImage getImg() {
        switch(direction) {
            case "up":
                return up1;
            
            case "down":
                return down1;
            
            case "left":
                return left1;
            
            case "right":
                return right1;
            
            default:
                return null;
        }
    }

    // needs to be for every enemy

    @Override
    protected boolean checkCollisions() {
        if(gp.getCollisionChecker().checkTile(this)) return true;

        Enemy[] enemies = gp.getEnemy();

        for(Enemy enemy : enemies) {
            if(enemy != null) {
                if(gp.getCollisionChecker().checkProjectileCollision(enemy, this)) {
                    return true;
                }
            }
        }
        return false;
    }
}