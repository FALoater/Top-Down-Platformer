package gamemanagers;

import static main.GamePanel.tileSize;

import java.awt.Graphics2D;

import gamestates.GameStateType;
import main.GamePanel;
import main.Sound;

public class LevelManager {
    private GamePanel gp;
    
    // access tilemanager to draw tiles
    private TileManager tileManager;

    // level statistics
    private int currentLevel;
    private int numberOfEnemies;
    private final int MAX_LEVEL = 3;

    public LevelManager(GamePanel gp) {
        this.gp = gp;
        tileManager = new TileManager(gp);
    }

    public void init() {
        // start game triggers
        currentLevel = 1;
        resetAll();
        loadTiles(1);
        loadEnemies();
    }

    public void loadNextLevel() {
        currentLevel++;

        if(currentLevel > MAX_LEVEL) { // if final level then go to end screen
            currentLevel--;
            gp.getUi().setVictory(true);
            gp.stopMusic();
            gp.playMusic(Sound.END_GAME);
            gp.setGameState(GameStateType.GAMEOVER);
            return;
        }

        // else load the next available level
        gp.getKeyHandler().resetMovement();
        loadTiles(currentLevel);
        resetAll();
        loadEnemies();
        gp.stopMusic();
        gp.setGameState(GameStateType.LOADING);
    }

    public void resetAll() {
        resetPlayer();
        resetProjectiles();
        resetEnemies();
        loadEnemies();
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
        // reset player animations
        gp.getPlayer().resetAttack();
        gp.getKeyHandler().resetMovement();
        
        // spawn player in correct position on map
        switch(currentLevel) {
            case 1:
                gp.getPlayer().setWorldX(tileSize * 5);
                gp.getPlayer().setWorldY(tileSize * 3);
                break;
            case 2:
                gp.getPlayer().setWorldX(tileSize * 5);
                gp.getPlayer().setWorldY(tileSize * 3);
                break;
            case 3:
                gp.getPlayer().setWorldX(tileSize * 47);
                gp.getPlayer().setWorldY(tileSize * 2);
                break;
        }
    }

    private void loadEnemies() {
        switch(currentLevel) {
            case 1:
            // case <level number>:
                gp.setEnemy(0, gp.getAssetSetter().spawnEnemy(17, 2, "fireThrower"));
                gp.setEnemy(1, gp.getAssetSetter().spawnEnemy(14, 14, "fireThrower"));
                gp.setEnemy(2, gp.getAssetSetter().spawnEnemy(4, 21, "fireThrower"));
                gp.setEnemy(3, gp.getAssetSetter().spawnEnemy(37, 17, "waterThrower"));
                gp.setEnemy(4, gp.getAssetSetter().spawnEnemy(24, 37, "waterThrower"));
                // copy and paste the above line to add more enemies
                // enemyTypes: fireThrower, waterThrower, slimeThrower
                // worldX and worldY are tile coordinates
                numberOfEnemies = 5;
                break;
            case 2:
                gp.setEnemy(0, gp.getAssetSetter().spawnEnemy(40, 7, "fireThrower"));
                gp.setEnemy(1, gp.getAssetSetter().spawnEnemy(9, 19, "waterThrower"));
                gp.setEnemy(2, gp.getAssetSetter().spawnEnemy(27, 33, "waterThrower"));
                gp.setEnemy(3, gp.getAssetSetter().spawnEnemy(44, 14, "waterThrower"));
                gp.setEnemy(4, gp.getAssetSetter().spawnEnemy(39, 25, "slimeThrower"));
                numberOfEnemies = 5;
                break;
            case 3:
                gp.setEnemy(0, gp.getAssetSetter().spawnEnemy(38, 11, "slimeThrower"));
                gp.setEnemy(1, gp.getAssetSetter().spawnEnemy(9, 9, "slimeThrower"));
                gp.setEnemy(2, gp.getAssetSetter().spawnEnemy(25, 21, "slimeThrower"));
                gp.setEnemy(3, gp.getAssetSetter().spawnEnemy(17, 28, "slimeThrower"));
                gp.setEnemy(4, gp.getAssetSetter().spawnEnemy(38, 29, "slimeThrower"));
                numberOfEnemies = 5;
                break;
        }
    }

    // getters and setters
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