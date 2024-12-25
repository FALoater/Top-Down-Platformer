package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import main.GamePanel;

public class Enemy extends Entity {
    public Enemy(GamePanel gp) {
        super(gp);
        speed = 1;
        direction = "down";

        maxLife = 4;
        life = maxLife;

        solidArea = new Rectangle(3, 12, 39, 30);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    protected void getImage() {}
    protected void spawnProjectile() {}

    public void checkHealth() {
		markedForDeletion = life <= 0;
	}
    
	public void attack() {
        // return early if the player is already attacking
        if(attacking || attackTimer < 90) return;

        // else spawn projectile and update player sprite
        attacking = true;
        attackTimer = 0;
		spawnProjectile();
	}

    @Override
	public void setAction() { 
        checkHealth();
		attackTimer++;

		if(attackTimer > 90 && attacking) {
			attacking = false;
		}
		
		gp.getCollisionChecker().checkEntity(this, gp.getNPC()); 
		int playerX = gp.getPlayer().getWorldX();
		int playerY = gp.getPlayer().getWorldY();

		// compare player coordinates with enemy coordinates
		// move left and right before up and down
		if(playerX == worldX) {
			if(playerY > worldY) {
				direction = "down";
			} else {
				direction = "up";
			}

		} else {
			if(playerX > worldX) {
				direction = "right";
			} else {
				direction = "left";
			}
		}
		attack();
	}	

	@Override
	protected void drawHealthBar(Graphics2D g2) {
		g2.setColor(Color.black);
		g2.drawRect(screenX, screenY - 10, 48, 10);
		g2.setColor(Color.red);
		g2.fillRect(screenX + 1, screenY - 9, (int)(47 * ((float)life / (float)maxLife)), 9);
	}
}