package entity.enemy;

import main.GamePanel;

public class SlimeThrower extends Enemy{

    public SlimeThrower(GamePanel gp, int worldX, int worldY) {
        super(gp);
		this.worldX = worldX;
		this.worldY = worldY;
        direction = "down";
		speed = 1; 
        getImage();
    }

    @Override
    protected void getImage() { 
		hurt = setup("/assets/entities/enemy/slime_thrower/slime_thrower_hurt");

		up1 = setup("/assets/entities/enemy/slime_thrower/slime_thrower_up_1");
		left1 = setup("/assets/entities/enemy/slime_thrower/slime_thrower_left_1");
		right1 = setup("/assets/entities/enemy/slime_thrower/slime_thrower_right_1");
		down1 = setup("/assets/entities/enemy/slime_thrower/slime_thrower_down_1");

		up2 = setup("/assets/entities/enemy/slime_thrower/slime_thrower_up_2");
		left2 = setup("/assets/entities/enemy/slime_thrower/slime_thrower_left_2");
		right2 = setup("/assets/entities/enemy/slime_thrower/slime_thrower_right_2");
		down2 = setup("/assets/entities/enemy/slime_thrower/slime_thrower_down_2");
    }

	// @Override
	// protected void spawnProjectile() {
	// 	gp.getAssetSetter().spawnProjectile(worldX, worldY, 7, direction, "slime");
	// }
}
