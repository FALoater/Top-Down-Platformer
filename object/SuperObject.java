package object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.GamePanel;
import main.UtilityTool;

import static main.GamePanel.tileSize;

public class SuperObject {
	
	// this is the parent class of all the object classes that we will be creating later 
	protected boolean collision = false; 

	protected int solidAreaDefaultX = 0; 
	protected int solidAreaDefaultY = 0; 
	protected int worldX, worldY;
	//VARIABLES
	protected BufferedImage image, image2, image3; // heart object has 3 images
	protected Rectangle solidArea = new Rectangle(0,0,48,48); // we are goint o set the whole 48 by 48 square box as a hitbox 
	protected String name; 
	protected UtilityTool uTool = new UtilityTool();
	
	// use the same draw method used to dra thw tiles, the logic stays the same 
	public void draw(Graphics2D g2, GamePanel gp) {
		int screenX = worldX - gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX(); 
		int screenY = worldY - gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY();
		
		if (worldX + tileSize> gp.getPlayer().getWorldX() - gp.getPlayer().getScreenX() &&
			worldX -tileSize < gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX() && 
			worldY + tileSize> gp.getPlayer().getWorldY() - gp.getPlayer().getScreenY() && 
			worldY - tileSize < gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY()) { 
			g2.drawImage(image, screenX, screenY, tileSize, tileSize, null);
		}
	}

	// getters and setters

	public Rectangle getSolidArea() {
		return solidArea;
	}

	public int getWorldX() {
		return worldX;
	}

	public int getWorldY() {
		return worldY;
	}

	public boolean isCollision() {
		return collision;
	}

	public int getSolidAreaDefaultX() {
		return solidAreaDefaultX;
	}

	public int getSolidAreaDefaultY() {
		return solidAreaDefaultY;
	}

	public BufferedImage[] getImages() {
		return new BufferedImage[]{image, image2, image3};
	} 
}