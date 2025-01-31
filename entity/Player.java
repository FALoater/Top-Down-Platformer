package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import gamestates.GameStateType;
import main.GamePanel;
import main.KeyHandler;
import main.Sound;

import static main.GamePanel.screenWidth;
import static main.GamePanel.screenHeight;
import static main.GamePanel.tileSize;

//child class of entity super class
public class Player extends Entity {
    private KeyHandler keyH;
    private boolean canMove;
    private int ammo = 3;

    private BufferedImage attackDown1, attackDown2, attackUp1, attackUp2, attackLeft1, attackLeft2, attackRight1, attackRight2;

    public Player(GamePanel gp, KeyHandler keyH) {
    	super(gp);
        this.keyH = keyH;
        
        //Display character at the centre of the screen (have to subtract tileSize/2 because measurements are taken from top left corner of box) 
        screenX = screenWidth / 2 - (tileSize / 2); 
        screenY = screenHeight / 2 - (tileSize / 2);
        
        solidArea = new Rectangle(10, 12, 26, 26); 
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        attackTimer = 120;
        
        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() { 
    	//Default characteristics for the player entity 
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

        // load shooting sprites
        attackDown1 = setup("/assets/entities/player/attacking/boy_attack_down_1", tileSize, tileSize * 2);
        attackDown2 = setup("/assets/entities/player/attacking/boy_attack_down_2", tileSize, tileSize * 2);
        attackUp1 = setup("/assets/entities/player/attacking/boy_attack_up_1", tileSize, tileSize * 2);
        attackUp2 = setup("/assets/entities/player/attacking/boy_attack_up_2", tileSize, tileSize * 2);
        attackLeft1 = setup("/assets/entities/player/attacking/boy_attack_left_1", tileSize * 2, tileSize);
        attackLeft2 = setup("/assets/entities/player/attacking/boy_attack_left_2", tileSize * 2, tileSize);
        attackRight1 = setup("/assets/entities/player/attacking/boy_attack_right_1", tileSize * 2, tileSize);
        attackRight2 = setup("/assets/entities/player/attacking/boy_attack_right_2", tileSize * 2, tileSize);

        hurt = setup("/assets/entities/player/moving/boy_hurt");
    }
    
    public void update() { 
        if(life <= 0) {
            gp.playSoundEffect(Sound.PLAYER_DEATH);
            gp.setGameState(GameStateType.GAMEOVER);
        }

        attackTimer++;

        if(attacking) {
            canMove = false;
        } else {
            canMove = true;
        }

        if(ammo > 0 && !keyH.isAttackPressed()) {
            attacking = false;
        }

        if(ammo <= 0) {
            if(attackTimer > 120) {
                ammo = 3;
            }
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
            
            //CHECK ENEMY COLLISION
            gp.getCollisionChecker().checkEntity(this, gp.getEnemy()); // probably decrease health after this coliision
            
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

    @Override
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
                    image = (spriteNum == 1) ? attackUp1 : attackUp2;
                    break;
                case "down":
                    image = (spriteNum == 1) ? attackDown1 : attackDown2;
                    break;
                case "left":
                    image = (spriteNum == 1) ? attackLeft1 : attackLeft2;
                    break;
                case "right":
                    image = (spriteNum == 1) ? attackRight1 : attackRight2;
                    break;
                }
        }
        
        if(isHurt && life > 0) {
            image = hurt;
            // pick random hurt sfx
            int random = (int) (Math.random() * 2);
            gp.playSoundEffect(2 + random); // player hurt is either 2 or 3
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
    
    public void attack() {
        if(ammo <= 0 || !keyH.isAttackPressed()) return;

        gp.playSoundEffect(Sound.PLAYER_ATTACK);
        attacking = true;
        attackTimer = 0;
        ammo--;

        switch(direction) {
            case "up":
                gp.getAssetSetter().spawnProjectile(worldX, worldY - tileSize, 7, direction, "fire");
                break;
            case "down":
                gp.getAssetSetter().spawnProjectile(worldX, worldY + tileSize, 7, direction, "fire");
                break;
            case "left":
                gp.getAssetSetter().spawnProjectile(worldX - tileSize, worldY, 7, direction, "fire");
                break;
            case "right":
                gp.getAssetSetter().spawnProjectile(worldX + tileSize, worldY, 7, direction, "fire");
                break;
        }
    }

    // getters and setters
    public int getAmmo() {
        return ammo;
    }

    public int getAttackTimer() {
        if(attackTimer > 120) {
            return 120;
        } else {
            return attackTimer;
        }
    }
}