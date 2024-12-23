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
    private boolean attacking = false, canMove = true;
    private int attackTimer = 30;

    private BufferedImage shoot_left1, shoot_left2, shoot_right1, shoot_right2, shoot_up1, shoot_up2, shoot_down1, shoot_down2;

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
        worldX = tileSize * 23; 
        worldY = tileSize * 21; 
        speed = 4;
        direction = "down";
        
        // PLAYER STATUS 
        maxLife = 6; 
        life = maxLife; 
    }

    public void getPlayerImage() { 
        up1 = setup("/assets/entities/player/moving/boy_up_1", tileSize, tileSize); 
        up2 = setup("/assets/entities/player/moving/boy_up_2", tileSize, tileSize);
        down1 = setup("/assets/entities/player/moving/boy_down_1", tileSize, tileSize); 
        down2 = setup("/assets/entities/player/moving/boy_down_2", tileSize, tileSize); 
        left1 = setup("/assets/entities/player/moving/boy_left_1", tileSize, tileSize); 
        left2 = setup("/assets/entities/player/moving/boy_left_2", tileSize, tileSize); 
        right1 = setup("/assets/entities/player/moving/boy_right_1", tileSize, tileSize); 
        right2 = setup("/assets/entities/player/moving/boy_right_2", tileSize, tileSize); 

        shoot_down1 = setup("/assets/entities/player/attacking/boy_attack_down_1", tileSize, tileSize * 2);
        shoot_down2 = setup("/assets/entities/player/attacking/boy_attack_down_2", tileSize, tileSize * 2);
        shoot_up1 = setup("/assets/entities/player/attacking/boy_attack_up_1", tileSize, tileSize * 2);
        shoot_up2 = setup("/assets/entities/player/attacking/boy_attack_up_2", tileSize, tileSize * 2);
        shoot_left1 = setup("/assets/entities/player/attacking/boy_attack_left_1", tileSize * 2, tileSize);
        shoot_left2 = setup("/assets/entities/player/attacking/boy_attack_left_2", tileSize * 2, tileSize);
        shoot_right1 = setup("/assets/entities/player/attacking/boy_attack_right_1", tileSize * 2, tileSize);
        shoot_right2 = setup("/assets/entities/player/attacking/boy_attack_right_2", tileSize * 2, tileSize);

        hurt = setup("/assets/entities/player/moving/boy_hurt", tileSize, tileSize);
    }
    
    public void update() { 
        attackTimer++;
        if(attacking) canMove = false;
        else canMove = true;

        if(attackTimer > 30 && attacking) {
            attacking = false;
        } 

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

            // projectile collisions are managed in the projectile class

            //CHECK TILE COLLISION
            collisionOn = false; 
            gp.getCollisionChecker().checkTile(this);
            
            //CHECK OBJECT COLLISION
            int objIndex = gp.getCollisionChecker().checkObject(this, true);
            pickUpObject(objIndex);
            
            //CHECK NPC COLLISION
            int npcIndex = gp.getCollisionChecker().checkEntity(this, gp.getNPCs()); //pass npc array as 'target' parameter
            interactNPC(npcIndex);

            //CHECK ENEMY COLLISION
            gp.getCollisionChecker().checkEntity(this, gp.getEnemies()); // probably decrease health after this coliision
            
            // CHECK EVENT 
            gp.getEventHandler().checkEvent();
            
            gp.getKeyHandler().setEnterPressed(false);

            //IF COLLISION IS FALSE, PLAYER CAN MOVE
            if (collisionOn == false && canMove) { 
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

            if(canMove) spriteCounter++; // everytime update is called, spriteCounter is incremented
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

    //Player would like to interact with NPC
    private void interactNPC(int i) {
		if (i!= 999) {
		    if(gp.getKeyHandler().isEnterPressed()) {
		    	gp.setGameState(gp.getDialogueState());
				gp.getNPCs()[i].speak();
		    }
		}
	}

	public void pickUpObject(int i) { 
    	if (i!= 999) {
    	}
//         
//        	String objectName = gp.obj[i].name;
//        	
//        	//Handle object's reactions 
//        	switch (objectName) { 
//        	case "Key":
//        		gp.playSoundEffect(1); //coin
//        		hasKey++;
//        		gp.obj[i] = null;
//        		gp.ui.showMessage("You got a key!");
//        		//System.out.println("Key: " + hasKey); < -- Testing 
//        		break; 
//        	case "Door": 
//        		if (hasKey > 0) { 
//        			gp.playSoundEffect(3); //unlock
//        			gp.obj[i] = null;
//        			hasKey--;
//        			gp.ui.showMessage("You opened the door!");
//        		}
//        		else { 
//        			gp.ui.showMessage("You need a key!");
//        		}
//        		//System.out.println("Key: " + hasKey); < Testing
//        		break;
//        	case "Boots": 
//        		gp.playSoundEffect(2); //power-up sound
//        		speed += 1;
//        		gp.obj[i] = null; 
//        		gp.ui.showMessage("SPEED UP!");
//        		break;
//        	case "Chest": 
//        		gp.ui.gameFinished = true; 
//        		gp.stopMusic();
//        		gp.playSoundEffect(4);
//        		break;
//        		
//        	
//        	// add penalty item as well 
//        }
//        }
    }

    public void attack() {
        // return early if the player is already attacking
        if(attacking || attackTimer < 30) return;

        // else spawn projectile and update player sprite
        attacking = true;
        attackTimer = 0;

        switch(direction) {
            case "up":
                gp.getAssetSetter().spawnFireProjectile(worldX, worldY - tileSize, 7, "up");
                break;
            case "down":
                gp.getAssetSetter().spawnFireProjectile(worldX, worldY + tileSize, 7, "down");
                break;
            case "left":
                gp.getAssetSetter().spawnFireProjectile(worldX - tileSize, worldY, 7, "left");
                break;
            case "right":
                gp.getAssetSetter().spawnFireProjectile(worldX + tileSize, worldY, 7, "right");
                break;
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

        if(attacking) {
            switch(direction) {
                case "up":
                    image = (spriteNum == 1) ? shoot_up1 : shoot_up2;
                    break;
                case "down":
                    image = (spriteNum == 1) ? shoot_down1 : shoot_down2;
                    break;
                case "left":
                    image = (spriteNum == 1) ? shoot_left1 : shoot_left2;
                    break;
                case "right":
                    image = (spriteNum == 1) ? shoot_right1 : shoot_right2;
                    break;
            }
        }

        if(isHurt) {
            image = hurt;
            isHurt = false;
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