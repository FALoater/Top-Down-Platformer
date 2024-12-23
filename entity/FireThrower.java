package entity;

import static main.GamePanel.tileSize;

import main.GamePanel;

public class FireThrower extends Enemy{

	public FireThrower(GamePanel gp) {
		super(gp);
		getImage();
	
	}

	@Override
	public void getImage() { 
		hurt = setup("/assets/entities/fire_thrower/fire_thrower_hurt", tileSize, tileSize);
		up1 = setup("/assets/entities/fire_thrower/fire_thrower_up_1", tileSize, tileSize); 
		left1 = setup("/assets/entities/fire_thrower/fire_thrower_left_1", tileSize, tileSize); 
		right1 = setup("/assets/entities/fire_thrower/fire_thrower_right_1", tileSize, tileSize); 
		down1 = setup("/assets/entities/fire_thrower/fire_thrower_down_1", tileSize, tileSize); 

		up2 = setup("/assets/entities/fire_thrower/fire_thrower_up_2", tileSize, tileSize); 
		left2 = setup("/assets/entities/fire_thrower/fire_thrower_left_2", tileSize, tileSize); 
		right2 = setup("/assets/entities/fire_thrower/fire_thrower_right_2", tileSize, tileSize); 
		down2 = setup("/assets/entities/fire_thrower/fire_thrower_down_2", tileSize, tileSize); 
	}
	
	@Override
	protected void spawnProjectile() {
		gp.getAssetSetter().spawnRedProjectile(worldX, worldY, 7, direction);
	}
}