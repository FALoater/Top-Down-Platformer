package entity;

import java.util.Random;

import main.GamePanel;

public class NPC_OldMan extends Entity {
	
	private int actionLockCounter = 120;

	public NPC_OldMan(GamePanel gp) { 
		super(gp);
		direction = "down";
		speed = 1; 
		getImage(); // init images of all directions
		setDialogue();
	}
	
	public void getImage() { 
        up1 = setup("/assets/entities/npc/oldman_up_1"); 
        up2 = setup("/assets/entities/npc/oldman_up_2"); 
        down1 = setup("/assets/entities/npc/oldman_down_1"); 
        down2 = setup("/assets/entities/npc/oldman_down_2"); 
        left1 = setup("/assets/entities/npc/oldman_left_1"); 
        left2 = setup("/assets/entities/npc/oldman_left_2"); 
        right1 = setup("/assets/entities/npc/oldman_right_1"); 
        right2 = setup("/assets/entities/npc/oldman_right_2"); 
    }
	
	public void setDialogue() {
		dialogues[0] = "Hello, lad";
		dialogues[1] = "So you've come to this island to \nfind the treasure?";
		dialogues[2] = "I used to be a great wizard but now... \nI'm a bit too old for taking an adventure.";
		dialogues[3] = "Well, good luck on you.";
	}
	
	// we created this method in the subclass to override the movement of any entities 
	public void setAction() { 
		actionLockCounter++; 
		
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
			actionLockCounter = 0; // once a direction is picked up, it will nor change for next 2 seconds (120 frames)
		}
	}
		
	public void speak() {
		super.speak();
		// dialogues for specific NPCs
	}
}