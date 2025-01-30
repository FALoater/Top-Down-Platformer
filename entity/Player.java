package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.GamePanel;
import main.KeyHandler;

import static main.GamePanel.screenWidth;
import static main.GamePanel.screenHeight;
import static main.GamePanel.tileSize;

//child class of entity super class
public class Player extends Entity {
    private KeyHandler keyH;

    public Player(GamePanel gp, KeyHandler keyH) {
    	super(gp);
        this.keyH = keyH;
        
        //Display character at the centre of the screen (have to subtract tileSize/2 because measurements are taken from top left corner of box) 
        screenX = screenWidth / 2 - (tileSize / 2); 
        screenY = screenHeight / 2 - (tileSize / 2);
        
        solidArea = new Rectangle(10, 12, 26, 26); 
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() { 
    	//Default characteristics for the player entity 
        worldX = tileSize * 5; 
        worldY = tileSize * 3; 
        speed = 4;
        direction = "down";
        
        // PLAYER STATUS 
        maxLife = 6; 
        life = maxLife; 
    }

    public void getPlayerImage() { 
        up1 = setup("/assets/entities/player/moving/boy_up_1"); 
        up2 = setup("/assets/entities/player/moving/boy_up_2"); 
        down1 = setup("/assets/entities/player/moving/boy_down_1"); 
        down2 = setup("/assets/entities/player/moving/boy_down_2"); 
        left1 = setup("/assets/entities/player/moving/boy_left_1"); 
        left2 = setup("/assets/entities/player/moving/boy_left_2"); 
        right1 = setup("/assets/entities/player/moving/boy_right_1"); 
        right2 = setup("/assets/entities/player/moving/boy_right_2"); 
    }
    
    public void update() { 
        //checks if key pressed (if not, no animation) 
        if (keyH.isUpPressed() || keyH.isDownPressed() || keyH.isLeftPressed() || keyH.isRightPressed()) { 
            if (keyH.isUpPressed()) {
                direction = "up"; 
            } else if (keyH.isDownPressed()) {
                direction = "down";
            } else if (keyH.isLeftPressed()) {
                direction = "left";
            } else if (keyH.isRightPressed()) {
                direction = "right";
            }

            //projectile collisions are managed in the projectile class

            //CHECK TILE COLLISION
            collisionOn = false; 
            gp.getCollisionChecker().checkTile(this);
            
            //CHECK OBJECT COLLISION
            // int objIndex = gp.getCollisionChecker().checkObject(this, true);
            // pickUpObject(objIndex);

            //CHECK ENEMY COLLISION
            gp.getCollisionChecker().checkEntity(this, gp.getEnemy()); // probably decrease health after this coliision
            
            // CHECK EVENT 
            //gp.getEventHandler().checkEvent();

            //IF COLLISION IS FALSE, PLAYER CAN MOVE
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
				if (spriteNum == 1) { // switch between two images for each direction to give a walking feel
					spriteNum = 2;
				}
				else if (spriteNum == 2) { 
					spriteNum = 1;
				}
				spriteCounter = 0; // reset spriteCounter
            }
        }	
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
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
        g2.drawImage(image, screenX, screenY, null);
    }

    @Override
    //Troubleshooting for collision detection
    public void drawHitbox(Graphics2D g2) {
        g2.setColor(Color.red);
        g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
    } 
}