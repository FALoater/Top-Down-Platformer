package main;

import entity.NPC_OldMan;
import projectile.FireProjectile;
import projectile.RedProjectile;
import entity.FireThrower;

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
	
	public void setNPC() {
		gp.createNPC(0, new NPC_OldMan(gp));
		gp.getNPCs(0).setWorldX(tileSize*21);
		gp.getNPCs(0).setWorldY(tileSize*21);
	}

	public void spawnFlameThrower() {
		gp.createEnemy(0, new FireThrower(gp));
		gp.getEnemies(0).setWorldX(tileSize*19);
		gp.getEnemies(0).setWorldY(tileSize*19);
	}

	public void spawnRedProjectile(int xPos, int yPos, int speed, String direction) {
		for(int i=0;i<gp.getProjectiles().length;i++) {
			if(gp.getProjectiles(i) == null) {
				gp.setProjectile(i, new RedProjectile(xPos, yPos, speed, direction, gp, 1));
				return;
			}
		}
	}

	public void spawnFireProjectile(int xPos, int yPos, int speed, String direction) {
		for(int i=0;i<gp.getProjectiles().length;i++) {
			if(gp.getProjectiles(i) == null) {
				gp.setProjectile(i, new FireProjectile(xPos, yPos, speed, direction, gp, 2));
				return;
			}
		}
	}
}