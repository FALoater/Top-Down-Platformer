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
	
	public void setObject() {
		//determine which objects you want and where you want to position them on the world map
		// can create a maximum of 10 objects (7 so far) 
		
//Treasure Hunting game 
//		gp.obj[0] = new OBJ_Key(gp);
//		gp.obj[0].worldX = 23 * tileSize; 
//		gp.obj[0].worldY = 7 * tileSize;
//		
//		gp.obj[1] = new OBJ_Key(gp);
//		gp.obj[1].worldX = 23 * tileSize;
//		gp.obj[1].worldY = 40*tileSize;
//		
//		gp.obj[2] = new OBJ_Key(gp); 
//		gp.obj[2].worldX = 38 * tileSize; 
//		gp.obj[2].worldY = 8 * tileSize;
//		
//		gp.obj[3] = new OBJ_Door(gp); 
//		gp.obj[3].worldX =  10* tileSize; 
//		gp.obj[3].worldY =  11* tileSize;
//		
//		gp.obj[4] = new OBJ_Door(gp); 
//		gp.obj[4].worldX =  8* tileSize; 
//		gp.obj[4].worldY =  28* tileSize;
//		
//		gp.obj[5] = new OBJ_Door(gp); 
//		gp.obj[5].worldX =  12* tileSize; 
//		gp.obj[5].worldY =  22* tileSize;
//		
//		gp.obj[6] = new OBJ_Chest(gp); 
//		gp.obj[6].worldX = 10 * tileSize; 
//		gp.obj[6].worldY = 7 * tileSize;
//		
//		gp.obj[7] = new OBJ_Boots(gp); 
//		gp.obj[7].worldX = 37 * tileSize; 
//		gp.obj[7].worldY =  42 * tileSize
	}

	public Enemy spawnEnemy(int worldX, int worldY, String type) {
		switch(type) {
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
