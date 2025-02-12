package entity.enemy;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

import entity.Entity;
import main.GamePanel;

public abstract class Enemy extends Entity {
	private int directionLock = 60;
	private Random random = new Random();

    public Enemy(GamePanel gp) {
        super(gp);
        speed = 1;

		// choose random direction
		int i = random.nextInt(100) + 1;
		if (i <= 25) {
			direction = "up";
		} else if (i > 25 && i <= 50) {
			direction = "down";
		} else if (i > 50 && i <= 75) {
			direction = "left";
		} else {
			direction = "right";
		}

		// set max health
        maxLife = 4;
        life = maxLife;

		// set hitboxes
        solidArea = new Rectangle(3, 12, 39, 30);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

		// load images
        getImage();
    }

	// overriden in subclasses
    protected void getImage() {}
    protected void spawnProjectile() {}

    public void checkHealth() {
		markedForDeletion = life <= 0;
	}
    
	public void attack() {
        // return early if the player is already attacking
        if(attacking || attackTimer < 300) return;

        // else spawn projectile and update player sprite
        attacking = true;
        attackTimer = 0;
		spawnProjectile();
	}

    @Override
	public void setAction() { 
        checkHealth();
		attackTimer++;
		directionLock++;

		if(attackTimer > 300 && attacking) {
			attacking = false;
		}
		
		int playerX = gp.getPlayer().getWorldX();
		int playerY = gp.getPlayer().getWorldY();

		// only pathfind if player is within 10 tiles (480 pixels)
		// else randomly move
		// move left and right before up and down
		if( Math.sqrt(Math.pow(playerX - worldX, 2) + Math.pow(playerY - worldY, 2)) < 480) {
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
		} else {
			if(directionLock >= 60) {
				int i = random.nextInt(100) + 1;
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

				directionLock = 0;
			}

		}
		// after movement, attempts to shoot once
		attack();
	}	

	@Override
	protected void drawHealthBar(Graphics2D g2) {
		g2.setColor(Color.black);
		g2.drawRect(screenX, screenY - 10, 48, 10);
		g2.setColor(Color.red);
		g2.fillRect(screenX + 1, screenY - 9, (int)(47 * ((float)life / (float)maxLife)), 9);
		//                                      calculates percentage of health bar shaded
	}

	@Override
	public void drawHitbox(Graphics2D g2) {
        g2.setColor(Color.green);
        g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
    }
}