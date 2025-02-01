package main;

import entity.Entity;
import projectile.Projectile;

import static main.GamePanel.tileSize;

import java.awt.Rectangle;

public class CollisionChecker {
    private GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public boolean checkTile(Projectile projectile) {
        // a method for checkTile(entity) already exists
        // therefore convert projectile into entity
        Entity p = new Entity(projectile);
        try {
            checkTile(p);
        } catch (Exception e) {
            projectile.setMarkedForDeletion(true);
            return true;
        }
        return p.getCollisionOn();
    }
    
    public void checkTile(Entity entity) {
        Rectangle entitySolidArea = entity.getSolidArea();
        
        int entityLeftWorldX = entity.getWorldX() + entitySolidArea.x;
        int entityRightWorldX = entity.getWorldX() + entitySolidArea.x + entitySolidArea.width;
        int entityTopWorldY = entity.getWorldY() + entitySolidArea.y;
        int entityBottomWorldY = entity.getWorldY() + entitySolidArea.y + entitySolidArea.height;
        
        int entityLeftCol = entityLeftWorldX / tileSize; 
        int entityRightCol = entityRightWorldX / tileSize;
        int entityTopRow = entityTopWorldY / tileSize; 
        int entityBottomRow = entityBottomWorldY / tileSize;

        if(entityLeftCol > 50 || entityRightCol > 50 || entityTopRow > 50 || entityBottomRow > 50) {
            return;
        }
        
        int tileNum1, tileNum2;

        switch(entity.getDirection()) {
            case "up": 
                entityTopRow = (entityTopWorldY - entity.getSpeed()) / tileSize;
                tileNum1 = gp.getLevelManager().getTileManager().getMapTileNum(entityLeftCol, entityTopRow);
                tileNum2 = gp.getLevelManager().getTileManager().getMapTileNum(entityRightCol, entityTopRow);
                
                if (gp.getLevelManager().getTileManager().getTile(tileNum1).isCollision() || gp.getLevelManager().getTileManager().getTile(tileNum2).isCollision()) {
                    entity.setCollisionOn(true);
                }
                break; 

            case "down": 
                entityBottomRow = (entityBottomWorldY + entity.getSpeed()) / tileSize;
                tileNum1 = gp.getLevelManager().getTileManager().getMapTileNum(entityLeftCol, entityBottomRow);
                tileNum2 = gp.getLevelManager().getTileManager().getMapTileNum(entityRightCol, entityBottomRow);
                
                if (gp.getLevelManager().getTileManager().getTile(tileNum1).isCollision() || gp.getLevelManager().getTileManager().getTile(tileNum2).isCollision()) {
                    entity.setCollisionOn(true);
                }
                break; 

            case "left": 
                entityLeftCol = (entityLeftWorldX - entity.getSpeed()) / tileSize;
                tileNum1 = gp.getLevelManager().getTileManager().getMapTileNum(entityLeftCol, entityTopRow);
                tileNum2 = gp.getLevelManager().getTileManager().getMapTileNum(entityLeftCol, entityBottomRow);
                
                if (gp.getLevelManager().getTileManager().getTile(tileNum1).isCollision() || gp.getLevelManager().getTileManager().getTile(tileNum2).isCollision()) {
                    entity.setCollisionOn(true);
                }
                break;

            case "right":
                entityRightCol = (entityRightWorldX + entity.getSpeed()) / tileSize;
                tileNum1 = gp.getLevelManager().getTileManager().getMapTileNum(entityRightCol, entityTopRow);
                tileNum2 = gp.getLevelManager().getTileManager().getMapTileNum(entityRightCol, entityBottomRow);
                
                if (gp.getLevelManager().getTileManager().getTile(tileNum1).isCollision() || gp.getLevelManager().getTileManager().getTile(tileNum2).isCollision()) {
                    entity.setCollisionOn(true);
                }
                break;
        }
    }

    public int checkObject(Entity entity, boolean player) { // check if the entity is a player or not 
        int index = 999; 
        
        for (int i = 0; i < gp.getObjects().length; i++) { 
            if (gp.getObjects(i) != null) { 
                // Get entity's solid area position 
                entity.incrementSolidAreaX(entity.getWorldX());
                entity.incrementSolidAreaY(entity.getWorldY());
                
                // Get object's solid area position 
                gp.getObjects(i).getSolidArea().x = gp.getObjects(i).getWorldX() + gp.getObjects(i).getSolidArea().x; 
                // this is to make it not hard coded (we can change the solid area boundaries of our objects) 

                gp.getObjects(i).getSolidArea().y = gp.getObjects(i).getWorldY() + gp.getObjects(i).getSolidArea().y;
                
                switch (entity.getDirection()) { 
                    case "up": 
                        entity.incrementSolidAreaY(-entity.getSpeed());
                        if (entity.getSolidArea().intersects(gp.getObjects(i).getSolidArea())) { 
                        	// intersects (if solidArea of entity and object are touching, collision is detected) 

                            if (gp.getObjects(i).isCollision()) { 
                                entity.setCollisionOn(true);
                            }
                            if (player) { 
                                index = i; 
                            }
                        }
                        break; 

                    case "down":
                        entity.incrementSolidAreaY(entity.getSpeed());
                        if (entity.getSolidArea().intersects(gp.getObjects(i).getSolidArea())) { 
                            if (gp.getObjects(i).isCollision()) { 
                                entity.setCollisionOn(true);
                            }
                            if (player) { 
                                index = i; 
                            }
                        }
                        break; 

                    case "left": 
                        entity.incrementSolidAreaX(-entity.getSpeed());
                        if (entity.getSolidArea().intersects(gp.getObjects(i).getSolidArea())) { 
                            if (gp.getObjects(i).isCollision()) { 
                                entity.setCollisionOn(true);
                            }
                            if (player) { 
                                index = i; 
                            }
                        }
                        break; 

                    case "right": 
                        entity.incrementSolidAreaX(entity.getSpeed());
                        if (entity.getSolidArea().intersects(gp.getObjects(i).getSolidArea())) { 
                            if (gp.getObjects(i).isCollision()) { 
                                entity.setCollisionOn(true);
                            }
                            if (player) { 
                                index = i; 
                            }
                        }
                        break;
                }
                
               // reset the solidArea regions after a collision has been detected
               entity.setSolidAreaX(entity.getSolidAreaDefaultX());
               entity.setSolidAreaY(entity.getSolidAreaDefaultY()); 
               gp.getObjects(i).getSolidArea().x = gp.getObjects(i).getSolidAreaDefaultX();
               gp.getObjects(i).getSolidArea().y = gp.getObjects(i).getSolidAreaDefaultY();
            }
        }
        return index;
    }
    
    //Monster Collision 
    public int checkEntity(Entity entity, Entity[] target) {
    	int index = 999; 
        
        for (int i = 0; i < target.length; i++) { 
            if (target[i] != null) { 
                // Get entity's solid area position 
                entity.incrementSolidAreaX(entity.getWorldX());
                entity.incrementSolidAreaY(entity.getWorldY());
                
                // Get object's solid area position 
                target[i].incrementSolidAreaX(target[i].getWorldX()); 
                // this is to make it not hard coded (we can change the solid area boundaries of our objects) 

                target[i].incrementSolidAreaY(target[i].getWorldY());
                
                switch (entity.getDirection()) { 
                    case "up": 
                        entity.incrementSolidAreaY(-entity.getSpeed());
                        if (entity.getSolidArea().intersects(target[i].getSolidArea())) { 
                        	//intersects (if solidArea of entity and object are touching, collision is detected) 
                        	entity.setCollisionOn(true);
                        	index = i; 
                        }
                        break; 

                    case "down":
                        entity.incrementSolidAreaY(entity.getSpeed());
                        if (entity.getSolidArea().intersects(target[i].getSolidArea())) {
                            entity.setCollisionOn(true);
                            index = i; 
                        }
                       break; 

                    case "left": 
                        entity.incrementSolidAreaX(-entity.getSpeed());
                        if (entity.getSolidArea().intersects(target[i].getSolidArea())) { 
                            entity.setCollisionOn(true);
                            index = i; 
                        }
                        break; 

                    case "right": 
                        entity.incrementSolidAreaX(entity.getSpeed());
                        if (entity.getSolidArea().intersects(target[i].getSolidArea())) {
                            entity.setCollisionOn(true);
                            index = i; 
                        }
                        break;
                }
                
               // reset the solidArea regions after a collision has been detected
               entity.setSolidAreaX(entity.getSolidAreaDefaultX()); 
               entity.setSolidAreaY(entity.getSolidAreaDefaultY());
               target[i].setSolidAreaX(target[i].getSolidAreaDefaultX());
               target[i].setSolidAreaY(target[i].getSolidAreaDefaultY());
            }
        }
        return index;
    }
    
    public void checkPlayer(Entity entity) {
    	// Get entity's solid area position 
        entity.incrementSolidAreaX(entity.getWorldX());
        entity.incrementSolidAreaY(entity.getWorldY());
        
        // Get object's solid area position 
        gp.getPlayer().incrementSolidAreaX(gp.getPlayer().getWorldX());
        gp.getPlayer().incrementSolidAreaY(gp.getPlayer().getWorldY());
        
        switch (entity.getDirection()) {
            case "up": 
                entity.incrementSolidAreaY(-entity.getSpeed());
                if (entity.getSolidArea().intersects(gp.getPlayer().getSolidArea())) { 
                	    entity.setCollisionOn(true);
                    }
                break; 

            case "down":
                entity.incrementSolidAreaY(entity.getSpeed());
                if (entity.getSolidArea().intersects(gp.getPlayer().getSolidArea())) {
                        entity.setCollisionOn(true);
                }
               break; 

            case "left": 
                entity.incrementSolidAreaX(-entity.getSpeed());
                if (entity.getSolidArea().intersects(gp.getPlayer().getSolidArea())) { 
                      	entity.setCollisionOn(true);

                    }
                break; 

            case "right": 
                entity.incrementSolidAreaX(entity.getSpeed());
                if (entity.getSolidArea().intersects(gp.getPlayer().getSolidArea())) {
                        entity.setCollisionOn(true);

                    }
                break;
        }
        
       // reset the solidArea regions after a collision has been detected
       entity.setSolidAreaX(entity.getSolidAreaDefaultX()); 
       entity.setSolidAreaY(entity.getSolidAreaDefaultY());
       gp.getPlayer().setSolidAreaX(gp.getPlayer().getSolidAreaDefaultX());
       gp.getPlayer().setSolidAreaY(gp.getPlayer().getSolidAreaDefaultY());
    }

    public boolean checkProjectileCollision(Entity entity, Projectile projectile) {
        entity.incrementSolidAreaX(entity.getWorldX());
        entity.incrementSolidAreaY(entity.getWorldY());
        // --------------------------------------------------
        if(projectile != null) {
            projectile.incrementSolidAreaX(projectile.getWorldX());
            projectile.incrementSolidAreaY(projectile.getWorldY());

            if (entity.getSolidArea().intersects(projectile.getSolidArea())) { 
                entity.takeDamage(projectile.getDamage());
                return true;
            } else {
                // reset hitboxes and valuesx
                projectile.setSolidAreaX(projectile.getSolidAreaDefaultX());
                projectile.setSolidAreaY(projectile.getSolidAreaDefaultY());
                entity.setSolidAreaX(entity.getSolidAreaDefaultX()); 
                entity.setSolidAreaY(entity.getSolidAreaDefaultY());
            }
        }
        return false;
    }
}
