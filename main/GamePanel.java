package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Arrays;

import javax.swing.JPanel;

import entity.Enemy;
import entity.Entity;
import entity.Player;
import object.SuperObject;
import projectile.Projectile;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {
	
	// SCREEN SETTINGS
	private final static int ORIGINAL_TILE_SIZE = 16; //16x16 tile 
	private final static int SCALE = 3;
	
	//have to make these variables public and static to import into other classes
	public static final int tileSize = ORIGINAL_TILE_SIZE * SCALE; // 48x48 tile (public to give access to other packages)
	public static final int maxScreenCol = 16; 
	public static final int maxScreenRow = 12;
	public static final int screenWidth = tileSize * maxScreenCol; // 768 pixels wide
	public static final int screenHeight = tileSize * maxScreenRow; // 576 pixels tall 
	
	//WORLD SETTINGS 
	public static final int maxWorldCol = 50; 
	public static final int maxWorldRow = 50; 
	public static  final int worldWidth = tileSize * maxWorldCol; 
	public static  final int worldHeight = tileSize * maxWorldRow; 
	
	//FPS
	private int FPS = 60;
	
	//INSTANTIATE SYSTEM CLASSES
	private Sound music = new Sound(), se = new Sound();
	private Thread gameThread; //very useful for game loops (need to implement runnable to use this thread) 

	private AssetSetter aSetter = new AssetSetter(this);
	private CollisionChecker cChecker = new CollisionChecker(this);
	private EventHandler eHandler = new EventHandler(this); //instantiate in gamePanel class
	private KeyHandler keyH = new KeyHandler(this);
	private TileManager tileM = new TileManager(this);
	private UI ui = new UI(this);

	//INSTANTIATE ENTITY AND OBJECT 
	private Player player = new Player(this, keyH);
	private SuperObject obj[] = new SuperObject[10]; // we have 10 slots to add objects (at the same time) (can update later on during the game development) 
	private Entity npc[] = new Entity[10];
	private Enemy enemies[] = new Enemy[10];
	private Projectile proj[] = new Projectile[10];
	
	// GAME STATE
	//game state - various game situations (title screen, main game play screen, menu screen, pause screen etc.) 
	//depending on the situation, the program draws something different on the screen 
	private int gameState; 
	private final int titleState = 0; //for title screen 
	private final int playState = 1; 
	private final int pauseState = 2;
	private final int dialogueState = 3; // not sure if will be used

	// debug
	private boolean debug = false;

	// Create gamePanel constructor 
	public GamePanel() { 
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true); // similar to BufferStrategy (improves game rendering process)
		this.addKeyListener(keyH); //gamePanel can recognise keyInput
		this.setFocusable(true); //needed for GamePanel to be 'focused' to receive key input 
	}
	
	//setupGame() method should be called before the thread starts running so position it correctly in the main class
	public void setupGame() { 
		aSetter.setObject();
		aSetter.setNPC();
		aSetter.spawnFlameThrower();
		//playMusic(0); // this is the first song that will run continuously in the background
		// we do not want music running on the title screen so we turn it off in this function 
		gameState = titleState;
	}
	
	public void startGameThread() { 
		gameThread = new Thread(this); // 'this' means we are passing this gamePanel class in as a parameter into our thread
		gameThread.start(); //automatically calls the 'run' method 
	}
	
	public void run() {
		double drawInterval = 1000000000/FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;
		
		while (gameThread != null) {
			
			currentTime = System.nanoTime();
			
			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime-lastTime);
			
			lastTime = currentTime; 
			
			if (delta >= 1) {
				update(); 
				repaint();
				delta--; 
				drawCount++;
			}
			
			if (timer >= 1000000000) {
				System.out.println("FPS: " + drawCount);
				drawCount = 0; 
				timer = 0;
			}
		}
	}
	
	// the two methods (update and paintComponent) will be called in the gameLoop 
	public void update() {

		if (gameState == playState) { 
			// PLAYER
			player.update();

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
			//NPC
			for (int i = 0; i < npc.length; i++) {
				if(npc[i] != null) {
					npc[i].update();
				}
			}
			//Enemies
			for (int i = 0; i < enemies.length; i++) {
				Enemy enemy = enemies[i];
				if(enemy != null) {
					if(enemy.isMarkedForDeletion()) {
						enemies[i] = null;
					} else {
						enemy.update();
					}
				}
			}
		}
		if (gameState == pauseState) {
			// pause menu
		}
	}
	
	public void paintComponent(Graphics g) { //standard built-in methods to draw things on JPanel in Java
		super.paintComponent(g); // just have to do it whenever you use paintComponent method 
		
		Graphics2D g2 = (Graphics2D)g;
		// Graphics2D class extends the Graphics class to provide greater control over geometry, coordinate transformations, colour management and text layout 
		// Graphics 2D has more functions than just Graphics on its own 
		
		// DEBUG
		long drawStart = 0; 
		if (debug) { 
			drawStart = System.nanoTime();
		}
		
		//TITLE SCREEN
		if (gameState == titleState) {
			ui.draw(g2);
		}
		
		//OTHERS (add a condition for the title xfeen) 
		else {
			//TILE
			tileM.draw(g2); // this has to come before the player line (draw tiles first, then player) 
			
			//OBJECT
			for (int i = 0; i < obj.length; i++) { // have to go through the entire objects array and check to see if any object is null 
				if (obj[i] != null) { // avoids NullPointer error 
					obj[i].draw(g2, this);
				}
			}

			//Enemy
			for (int i = 0; i < enemies.length; i++) {
				if (enemies[i] != null) {
					enemies[i].draw(g2);
				}
			}
			
			//NPC
			for (int i = 0; i < npc.length; i++) {
				if (npc[i] != null) {
					npc[i].draw(g2);
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
			
			//DEBUG
			if (debug) { 
				long drawEnd = System.nanoTime();
				long passed = drawEnd - drawStart; 
				g2.setColor(Color.white);
				g2.drawString("Draw time: "  + passed, 10, 400);
				System.out.println("Draw time: " + passed);
			}			
			g2.dispose(); // once drawn, dispose of this graphics context and release any system resources it is using (good practice) 
		}
	}
	
	public void playMusic(int i) { 
		music.setFile(i);
		music.play();
		music.loop();
	}
	
	public void stopMusic() { 
		music.stop();
	}

	public void playSoundEffect(int i) { 
		se.setFile(i);
		se.play(); // we dont call loop for sound effects because these are generally really stop 
	}

	// Getters and setters
	public void setGameState(int gameState) {
		this.gameState = gameState;
	}

	public int getGameState() {
		return gameState;
	}

	public int getTitleState() {
		return titleState;
	}

	public int getPlayState() {
		return playState;
	}
	
	public int getPauseState() {
		return pauseState;
	}

	public Projectile[] getProjectiles() {
		return proj;
	}

	public Projectile getProjectiles(int index) {
		return proj[index];
	}

	public void setProjectile(int index, Projectile projectile) {
		proj[index] = projectile;
	}

	public int getDialogueState() {
		return dialogueState;
	}

	public UI getUi() {
		return ui;
	}

	public Player getPlayer() {
		return player;
	}

	public Entity[] getNPCs() {
		return npc;
	}

	public Entity getNPCs(int index) {
		return npc[index];
	}

	public void createNPC(int index, Entity npc) {
		this.npc[index] = npc;	
	}

	public Enemy[] getEnemies() {
		return enemies;
	}

	public Enemy getEnemies(int index) {
		return enemies[index];
	}

	public void createEnemy(int index, Enemy enemy) {
		this.enemies[index] = enemy;
	}

	public CollisionChecker getCollisionChecker() {
		return cChecker;
	}

	public AssetSetter getAssetSetter() {
		return aSetter;
	}

	public EventHandler getEventHandler() {
		return eHandler;
	}

	public KeyHandler getKeyHandler() {
		return keyH;
	}

	public TileManager getTileManager() {
		return tileM;
	}

	public SuperObject[] getObjects() {
		return obj;
	}

	public SuperObject getObjects(int index) {
		return obj[index];
	}

	public void toggleDebug() {
		debug = !debug;
	}
}
/*
 * General notes - without an FPS counter, touching a key will update the position around a million times a second and move the rectangle off the screen 
 * Therefore FPS counter is necessary 
 * Generally delta FPS measure is better than the sleep FPS measure (can mention in NEA) 
 * 
*/
