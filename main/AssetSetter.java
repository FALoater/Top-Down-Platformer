package main;

import entity.enemy.Enemy;
import entity.enemy.FireThrower;
import projectile.*;
import entity.enemy.*;

import static main.GamePanel.tileSize;

public class AssetSetter {
	
	private GamePanel gp; 
	
	public AssetSetter(GamePanel gp) {
		this.gp = gp;
	}
	
	public Enemy spawnEnemy(int worldX, int worldY, String type) {
		switch(type) {
			// switch case is easier to use than if statements
			case "fireThrower":
				return new FireThrower(gp, tileSize * worldX, tileSize * worldY);
			case "waterThrower":
				return new WaterThrower(gp, tileSize * worldX, tileSize * worldY);
			case "slimeThrower":
				return new SlimeThrower(gp, tileSize * worldX, tileSize * worldY);
			default:
				return null;
		}
	}

	// linear search to find next position in the array that is free
	private int findNextFreePosition() {
		for(int i=0;i<gp.getProjectiles().length;i++) {
			if(gp.getProjectiles(i) == null) {
				return i;
			}
		}
		return -1;
	}

	public void spawnProjectile(int worldX, int worldY, int speed, String direction, String type) {
		switch(type) {
			case "fire":
				gp.setProjectile(findNextFreePosition(), new FireProjectile(worldX, worldY, speed, direction, gp));
				return;
			case "red":
				gp.setProjectile(findNextFreePosition(), new RedProjectile(worldX, worldY, speed, direction, gp));
				return;
			case "water":
				gp.setProjectile(findNextFreePosition(), new WaterProjectile(worldX, worldY, speed, direction, gp));
				return;
			case "slime":
				gp.setProjectile(findNextFreePosition(), new SlimeProjectile(worldX, worldY, speed, direction, gp));
				return;
		}
	}
}
