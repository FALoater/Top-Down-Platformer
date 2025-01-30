package gamestates;

import projectile.Projectile;
import entity.enemy.Enemy;
import entity.Player;
import main.GamePanel;
import main.LevelManager;
import main.Sound;
import main.UI;
import object.SuperObject;

import static main.GamePanel.worldHeight;
import static main.GamePanel.worldWidth;

import java.awt.Graphics2D;

public class Playing implements GameState{
    private boolean debug;

    private GamePanel gp;
    private Player player;
    private LevelManager levelManager;
    private Projectile proj[];
    private Enemy enemies[];
    private SuperObject obj[];
    private UI ui;

    public Playing(GamePanel gp) {
        this.gp = gp;

        player = gp.getPlayer();
        levelManager = gp.getLevelManager();
        proj = gp.getProjectiles();
        enemies = gp.getEnemy();
        obj = gp.getObjects();
        ui = gp.getUi();
    }

    @Override
    public void update() {
        player.update();
        if(levelManager.getNumberOfEnemies() == 0) {
            levelManager.loadNextLevel();
        }

        // Projectiles
        for(int i = 0;i<proj.length;i++) {
            Projectile projectile = proj[i];

            if(projectile != null) {
                // check first if the projectile is meant to be deleted first
                if(projectile.isMarkedForDeletion() || Math.abs(projectile.getWorldX()) > worldWidth || Math.abs(projectile.getWorldY()) > worldHeight) {
                    proj[i] = null;
                } else {
                    projectile.update();
                }
            }
        }

        for(int i = 0; i <enemies.length;i++) {
            if(enemies[i] != null) {
                if(enemies[i].isMarkedForDeletion()) {
                    gp.playSoundEffect(Sound.ENEMY_DEATH);
                    enemies[i] = null;
                    levelManager.decreaseNumberOfEnemies();
                } else {
                    enemies[i].update();
                }
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        debug = gp.getDebug();

        levelManager.draw(g2); // this has to come before the player line (draw tiles first, then player) 
        
        //OBJECT
        for (int i = 0; i < obj.length; i++) { // have to go through the entire objects array and check to see if any object is null 
            if (obj[i] != null) { // avoids NullPointer error 
                obj[i].draw(g2, gp);
            }
        }

        //Enemy
        for (int i = 0; i < enemies.length; i++) {
            if (enemies[i] != null) {
                enemies[i].draw(g2);
            }
        }

        //PLAYER
        player.draw(g2);
        if(debug) player.drawHitbox(g2);

        //Projectile
        //Using for x in list structure similar to python
        for(Projectile projectile : proj) {
            if(projectile != null) {
                projectile.draw(g2);
                if(debug) projectile.drawHitbox(g2);
            }
        }
        
        //UI called at the end because it should not be covered by tile or player 
        ui.draw(g2);
    }
}
