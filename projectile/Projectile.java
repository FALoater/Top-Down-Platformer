package projectile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import entity.Entity;
import main.GamePanel;

import static main.GamePanel.tileSize;

public class Projectile extends Entity {
    //Every projectile needs the position, direction, image and access to the main game class
    protected BufferedImage img;
    protected boolean markedForDeletion;
    protected int damage;

    private boolean init = true;

    public Projectile(int worldX, int worldY, int speed, String direction, GamePanel gp, int damage) {
        super(gp);
        this.worldX = worldX;
        this.worldY = worldY;
        this.speed = speed;
        this.direction = direction;
        this.gp = gp;
        this.damage = damage;
    }

    protected BufferedImage getImg() {
        return null;
    }

    protected void checkEntityCollisions() {}

    protected boolean checkWallCollisions() {
        return false;
    }

    @Override
    public void update() {
        // first, check whether the projectile hits the wall
        // if not, then check whether the projectile hits the player
        // in both cases, delete the projectile if true
        init = false;
        markedForDeletion = checkWallCollisions();
        checkEntityCollisions();

        // calculate where projectile will display on the screen
        screenX = worldX - gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX(); 
        screenY = worldY - gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY();;

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

    @Override
    public void draw(Graphics2D g2) {
        // Calculate the area where the player is currently in
		if (worldX + tileSize> gp.getPlayer().getWorldX() - gp.getPlayer().getScreenX() &&
            worldX -tileSize < gp.getPlayer().getWorldX() + gp.getPlayer().getScreenY() && 
            worldY + tileSize> gp.getPlayer().getWorldY() - gp.getPlayer().getScreenY() && 
            worldY - tileSize < gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY()) { 
            
            img = getImg();
                
            // Draw the projectile if inside the four corners
            if(!init) g2.drawImage(img, screenX, screenY, tileSize, tileSize, null);
        }
    }

    @Override
    // draw the hitbox for debug
    public void drawHitbox(Graphics2D g2) {
        g2.setColor(Color.cyan);
        g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
    }

    // Getters and setters

    public boolean isMarkedForDeletion() {
        return markedForDeletion;
    }

    public void setMarkedForDeletion(boolean markedForDeletion) {
        this.markedForDeletion = markedForDeletion;
    }
    
    public int getScreenX() {
        return screenX;
    }

    public int getScreenY() {
        return screenY;
    }

    public int getDamage() {
        return damage;
    }
}