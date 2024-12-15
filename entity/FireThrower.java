package entity;

import java.util.Random;

import main.GamePanel;

public class FireThrower extends Entity{

    public FireThrower(GamePanel gp) {
        super(gp);
        direction = "down";
		speed = 1; 
        getImage();
    }

    public void getImage() { 
        up1 = setup("/assets/entities/fire_thrower/fire_thrower_up_1"); 
		left1 = setup("/assets/entities/fire_thrower/fire_thrower_left_1"); 
		right1 = setup("/assets/entities/fire_thrower/fire_thrower_right_1"); 
		down1 = setup("/assets/entities/fire_thrower/fire_thrower_down_1"); 

		up2 = setup("/assets/entities/fire_thrower/fire_thrower_up_2"); 
		left2 = setup("/assets/entities/fire_thrower/fire_thrower_left_2"); 
		right2 = setup("/assets/entities/fire_thrower/fire_thrower_right_2"); 
        down2 = setup("/assets/entities/fire_thrower/fire_thrower_down_2"); 
    }
	
	// we created this method in the subclass to override the movement of any entities 
	public void setAction() { 
		actionLockCounter++; 
		
		gp.getCollisionChecker().checkEntity(this, gp.npc); 
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

			gp.getAssetSetter().spawnProjectile(worldX, worldY, 7, direction);
			
			actionLockCounter = 0; // once a direction is picked up, it will nor change for next 2 seconds (120 frames)
		}
	}	
}