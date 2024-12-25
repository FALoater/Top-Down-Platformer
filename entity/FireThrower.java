package entity;

import main.GamePanel;

public class FireThrower extends Enemy{

    public FireThrower(GamePanel gp) {
        super(gp);
        direction = "down";
		speed = 1; 
        getImage();
    }

	@Override
    protected void getImage() { 
		hurt = setup("/assets/entities/fire_thrower/fire_thrower_hurt");

        up1 = setup("/assets/entities/fire_thrower/fire_thrower_up_1"); 
		left1 = setup("/assets/entities/fire_thrower/fire_thrower_left_1"); 
		right1 = setup("/assets/entities/fire_thrower/fire_thrower_right_1"); 
		down1 = setup("/assets/entities/fire_thrower/fire_thrower_down_1"); 

		up2 = setup("/assets/entities/fire_thrower/fire_thrower_up_2"); 
		left2 = setup("/assets/entities/fire_thrower/fire_thrower_left_2"); 
		right2 = setup("/assets/entities/fire_thrower/fire_thrower_right_2"); 
        down2 = setup("/assets/entities/fire_thrower/fire_thrower_down_2"); 
    }

	@Override
	protected void spawnProjectile() {
		gp.getAssetSetter().spawnRedProjectile(worldX, worldY, 7, direction);
	}
}