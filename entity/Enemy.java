package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

import main.GamePanel;

public class Enemy extends Entity {

    // the superclass for all enemies
	protected boolean markedForDeletion = false;

    public Enemy(GamePanel gp) {
        super(gp);
        speed = 1;
        direction = "down";

		maxLife = 4;
		life = maxLife;

		// all enemies will have the same hitbox		
		solidArea = new Rectangle(3, 12, 39, 30); 
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
		
        getImage();
    }

    protected void getImage() { }
    protected void spawnProjectile() {}
	
	// we created this method in the subclass to override the movement of any entities 
    @Override
    public void setAction() {
		checkHealth(); 
		actionLockCounter++; 
		
		gp.getCollisionChecker().checkEntity(this, gp.getNPCs()); 
		if(actionLockCounter == 120) {
			Random random = new Random();
			int i = random.nextInt(100) + 1; // a random number from 1 to 100 (not inclusive so have to add 1) 
			
			// Simplistic AI (very basic) 
			// 25% of each time, it either goes up, down, left or right 
			if (i <= 25) {
				direction = "up";
			}
			if (i > 25 && i < 50) { 
				direction = "down";
			}
			if (i > 50 && i<75) {
				direction = "left";
				}
			if (i > 75 && i <= 100) { 
				direction = "right";
			}

            spawnProjectile();

			actionLockCounter = 0; // once a direction is picked up, it will nor change for next 2 seconds (120 frames)
		}
	}
	
	public void checkHealth() {
		markedForDeletion = life <= 0;
	}


	@Override
    // draw the hitbox for debug
    public void drawHitbox(Graphics2D g2) {
        g2.setColor(Color.green);
        g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
    }

	public boolean isMarkedForDeletion() {
		return markedForDeletion;
	}
}