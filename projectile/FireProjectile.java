package projectile;

import static main.GamePanel.tileSize;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import entity.Enemy;
import main.GamePanel;

public class FireProjectile extends Projectile{
    
    public FireProjectile(int worldX, int worldY, int speed, String direction, GamePanel gp, int damage) {
        super(worldX, worldY, speed, direction, gp, damage);
        loadImgs();

        solidArea = new Rectangle(12, 9,27,30);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    private void loadImgs() {
        up1 = setup("/assets/projectiles/player_projectiles/fireball_up_1", tileSize, tileSize); 
        down1 = setup("/assets/projectiles/player_projectiles/fireball_down_1", tileSize, tileSize); 
        left1 = setup("/assets/projectiles/player_projectiles/fireball_left_1", tileSize, tileSize); 
        right1 = setup("/assets/projectiles/player_projectiles/fireball_right_1", tileSize, tileSize); 
    }

    @Override
    protected void checkEntityCollisions() {
        for(Enemy enemy : gp.getEnemies()) {
            if(enemy != null) markedForDeletion = gp.getCollisionChecker().checkProjectileCollision(enemy, this);
        }
    }

    @Override
    protected boolean checkWallCollisions() {
        return gp.getCollisionChecker().checkTile(this);
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
}
