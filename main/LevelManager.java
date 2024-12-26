package main;

import tile.TileManager;

import static main.GamePanel.tileSize;

import java.awt.Graphics2D;

public class LevelManager {
    private GamePanel gp;
    private TileManager tileManager;
    private int currentLevel = 1;

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
        resetPlayer();
        loadEnemies();
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
            case 2:
                return;
            case 3:
                return;
        }
    }

    public TileManager getTileManager() {
        return tileManager;
    }
}