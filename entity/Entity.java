package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import java.io.IOException;

import javax.imageio.ImageIO;

import static main.GamePanel.tileSize;

import main.GamePanel;
import main.UtilityTool;
import projectile.Projectile;

// this will be the super/parent class for all our entities
// stores variables that will be used in all of player, monster classes

public class Entity {
	// event triggers
	protected boolean attacking = false; 
	protected boolean collisionOn = false; 
	protected boolean isHurt = false;
	protected boolean markedForDeletion = false;

	// init hitbox for collision detection, can change later
	protected Rectangle solidArea = new Rectangle(0,0,48,48); 

	// world position
	protected int speed;
	protected int screenX, screenY;
	protected int solidAreaDefaultX, solidAreaDefaultY;
	protected int worldX, worldY;
	protected String direction;

	// animations
	protected int spriteCounter = 0, spriteNum = 1;
	protected int attackTimer;
	
	// entity health
	protected int maxLife; 
	protected int life;

	// sprites
	protected BufferedImage up1, up2, down1, down2, left1, left2, right1, right2, hurt;

	// main game class
	protected GamePanel gp;

	public Entity(GamePanel gp) {
		this.gp = gp;
	}

	public Entity(Projectile projectile) {
		// temporarily converts a projectile into an entity
		// this is for projectile collisions
		this.worldX = projectile.getWorldX();
		this.worldY = projectile.getWorldY();
		this.speed = projectile.getSpeed();
		this.direction = projectile.getDirection();
		this.solidArea = projectile.getSolidArea();
	}

	public void setHitbox(int x, int y, int width, int height) {
		// initialise hitbox boundaries
		solidArea.x = x;
		solidArea.y = y;
		solidArea.width = width;
		solidArea.height = height;
	}

	// override in subclasses
	public void drawHitbox(Graphics2D g2) {}
	public void setAction() {};
	protected void drawHealthBar(Graphics2D g2) {}; 
	protected void playHurtEffect() {};

	public void update() { 
		setAction(); // subclass method takes priority 
		
		collisionOn = false; 
		gp.getCollisionChecker().checkTile(this);
		gp.getCollisionChecker().checkObject(this, false);
		gp.getCollisionChecker().checkPlayer(this);
		
		// if no collisions, can move
		if (collisionOn == false) { 
            switch(direction) { 
                case "up": 
                    worldY -= speed; 
                    break; 
                case "down": 
                    worldY += speed;
                    break; 
                case "left":
                    worldX -= speed;
                    break; 
                case "right":
                    worldX += speed;
                    break;
            }
        }

        spriteCounter++; // everytime update is called, spriteCounter is incremented
		if (spriteCounter > 12) {
			if (spriteNum == 1) {// switch between two images for each direction to give a walking feel
				spriteNum = 2;
			}
			else if (spriteNum == 2) { 
				spriteNum = 1;
			}
			spriteCounter = 0; // reset spriteCounter
    	}
    }	
	
	public void draw(Graphics2D g2) {
		BufferedImage image = null;
		screenX = worldX - gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX(); 
		screenY = worldY - gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY();
		
		if (worldX + tileSize> gp.getPlayer().getWorldX() - gp.getPlayer().getScreenX() &&
			worldX -tileSize < gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX() && 
			worldY + tileSize> gp.getPlayer().getWorldY() - gp.getPlayer().getScreenY() && 
			worldY - tileSize < gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY()) { 
			// only draw the entity if it is in the game screen

			switch(direction) { 
			case "up": 
				image = (spriteNum == 1) ? up1 : up2;
				break; 
			case "down": 
				image = (spriteNum == 1) ? down1 : down2;
				break; 
			case "left": 
				image = (spriteNum == 1) ? left1 : left2;
				break;
			case "right": 
				image = (spriteNum == 1) ? right1 : right2;
				break;
			}

			if(isHurt && life > 0) { // if taken hit but not dead
				playHurtEffect();
				image = hurt;
				isHurt = false;
			}

			g2.drawImage(image, screenX, screenY, tileSize, tileSize, null);
			drawHealthBar(g2);
		}
	}

	protected BufferedImage setup(String imagePath) {
		// more general version of setup
		return setup(imagePath, tileSize, tileSize);
	}
		
	protected BufferedImage setup(String imagePath, int width, int height) { 
		UtilityTool uTool = new UtilityTool();
		BufferedImage image = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
		
		try { 
			image = ImageIO.read(getClass().getResourceAsStream(imagePath +".png"));
			image = uTool.scaleImage(image, width, height);
			
		}catch(IOException e) { 
			e.printStackTrace();
		}
		return image;
    }
	
	// getters and setters
	public void restoreFullHealth() {
		life = maxLife;
	}

    public void setDirection(String direction) {
        this.direction = direction;
    }

	public BufferedImage getPlayerMenuImg() {
		return down1;
	}

	public int getMaxLife() {
		return maxLife;
	}

	public int getCurrentLife() {
		return life;
	}

    public void takeDamage(int damage) {
        life -= damage;
		isHurt = true;
    }
    
    public String getDirection() {
        return direction;
    }

    public int getSolidAreaDefaultX() {
        return solidAreaDefaultX;
    }

    public int getSolidAreaDefaultY() {
        return solidAreaDefaultY;
    }

    public int getWorldX() {
        return worldX;
    }

	public void setWorldX(int worldX) {
		this.worldX = worldX;
	}

    public int getWorldY() {
        return worldY;
    }

	public void setWorldY(int worldY) {
		this.worldY = worldY;
	}

	public int getScreenX() {
		return screenX;
	}

	public int getScreenY() {
		return screenY;
	}

    public Rectangle getSolidArea() {
        return solidArea;
    }

    public int getSolidAreaX() {
        return solidArea.x;
    }

    public int getSolidAreaY() {
        return solidArea.y;
    }

	public void setSolidAreaX(int x) {
		solidArea.x = x;
	}

	public void setSolidAreaY(int y) {
		solidArea.y = y;
	}

	public void incrementSolidAreaX(int x) {
		solidArea.x += x;
	}

	public void incrementSolidAreaY(int y) {
		solidArea.y += y;
	}

    public int getSpeed() {
        return speed;
    }

	public boolean getCollisionOn() {
		return collisionOn;
	}

	public void setCollisionOn(boolean collisionOn) {
		this.collisionOn = collisionOn;
	}

	public boolean isMarkedForDeletion() {
		return markedForDeletion;
	}
}
