package main;

import tile.TileManager;

import static main.GamePanel.tileSize;

import java.awt.Graphics2D;

import gamestates.GameStateType;

public class LevelManager {
    private GamePanel gp;
    private TileManager tileManager;
    private int currentLevel = 1;
    private int numberOfEnemies;

    public LevelManager(GamePanel gp) {
        this.gp = gp;
        tileManager = new TileManager(gp);
    }

    public void init() {
        loadTiles(1);
        resetPlayer();
        loadEnemies();
    }

    public void loadNextLevel() {
        currentLevel++;
        loadTiles(currentLevel);
        resetAll();
        loadEnemies();
        gp.setGameState(GameStateType.LOADING);
        gp.stopMusic();
    }

    private void resetAll() {
        resetPlayer();
        resetProjectiles();
        resetEnemies();
    }

    private void resetProjectiles() {
        gp.resetProjectiles();
    }

    private void resetEnemies() {
        gp.resetEnemies();
    }

    public void draw(Graphics2D g2) {
        tileManager.draw(g2);
    }

    private void loadTiles(int level) {
        tileManager.getLevel(level);
    }

    private void resetPlayer() {
        switch(currentLevel) {
            case 1:
                gp.getPlayer().setWorldX(tileSize * 5);
                gp.getPlayer().setWorldY(tileSize * 3);
                break;
            case 2:
                gp.getPlayer().setWorldX(tileSize * 20);
                gp.getPlayer().setWorldY(tileSize * 10);
                break;
            // case 3:
            //     gp.getPlayer().setWorldX(100);
            //     gp.getPlayer().setWorldY(100);
            //     break;
        }
    }

    private void loadEnemies() {
        switch(currentLevel) {
            case 1:
            // case <level number>:
                gp.setEnemy(0, gp.getAssetSetter().spawnEnemy(17, 2, "fireThrower"));
                gp.setEnemy(1, gp.getAssetSetter().spawnEnemy(14, 14, "waterThrower"));
                gp.setEnemy(2, gp.getAssetSetter().spawnEnemy(4, 21, "slimeThrower"));
                // copy and paste the above line to add more enemies
                // enemyTypes: fireThrower, waterThrower, slimeThrower
                // worldX and worldY are tile coordinates
                numberOfEnemies = 3;
            case 2:
                numberOfEnemies = 3;
                return;
            case 3:
                return;
        }
    }

    // getters and settesr

    public TileManager getTileManager() {
        return tileManager;
    }

    public int getNumberOfEnemies() {
        return numberOfEnemies;
    }  

    public void decreaseNumberOfEnemies() {
        numberOfEnemies--;
    }   

    public int getCurrentLevel() {
        return currentLevel;
    }
}