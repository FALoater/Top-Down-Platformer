package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

import entity.Player;
import entity.enemy.Enemy;
import gamemanagers.LevelManager;
import gamestates.GameStateType;
import gamestates.Playing;
import object.SuperObject;
import projectile.Projectile;

public class GamePanel extends JPanel implements Runnable {
	
	// SCREEN SETTINGS
	private final static int ORIGINAL_TILE_SIZE = 16; //16x16 tile 
	private final static int SCALE = 3;
	
	// have to make these variables public and static to import into other classes
	public static final int tileSize = ORIGINAL_TILE_SIZE * SCALE; // 48x48 tile (public to give access to other packages)
	public static final int maxScreenCol = 16; 
	public static final int maxScreenRow = 12;
	public static final int screenWidth = tileSize * maxScreenCol; // 768 pixels wide
	public static final int screenHeight = tileSize * maxScreenRow; // 576 pixels tall 
	
	//WORLD SETTINGS 
	public static final int maxWorldCol = 50; 
	public static final int maxWorldRow = 50; 
	public static final int worldWidth = tileSize * maxWorldCol; 
	public static final int worldHeight = tileSize * maxWorldRow; 
	
	//FPS
	protected int FPS = 60;
	
	//INSTANTIATE SYSTEM CLASSES
	protected Sound music = new Sound(6), se = new Sound(9); // play music at lower volume so sound effects can be heard
	protected Thread gameThread; // very useful for game loops (need to implement runnable to use this thread) 
	protected KeyHandler keyH = new KeyHandler(this);

	//INSTANTIATE ENTITY AND OBJECT 
	protected SuperObject obj[] = new SuperObject[10]; // we have 10 slots to add objects (at the same time) (can update later on during the game development)
	protected Enemy enemies[] = new Enemy[10];
	protected Projectile proj[] = new Projectile[10];
	protected Player player = new Player(this, keyH); // instantiate in gamePanel class

	protected AssetSetter aSetter = new AssetSetter(this);
	protected CollisionChecker cChecker = new CollisionChecker(this);
	protected LevelManager levelManager = new LevelManager(this);
	protected UI ui = new UI(this);

	// loading screen
	private int loadTimer = 0;

	// GAME STATE
	// game state - various game situations (title screen, main game play screen, menu screen, pause screen etc.) 
	// depending on the situation, the program draws something different on the screen 
	private GameStateType gameState = GameStateType.TITLE;
	private Playing playing = new Playing(this); // instantiate in gamePanel class

	// debug
	protected boolean debug = false;

	// create gamePanel constructor 
	public GamePanel() { 
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true); // similar to BufferStrategy (improves game rendering process)
		this.addKeyListener(keyH); // gamePanel can recognise keyInput
		this.setFocusable(true); // needed for GamePanel to be 'focused' to receive key input 
	}
	
	// setupGame() method should be called before the thread starts running so position it correctly in the main class
	public void setupGame() { 
		levelManager.init();
		playMusic(Sound.MAIN_MENU);
		gameState = GameStateType.TITLE;
	}

	public void restartGame() {
		ui.setVictory(false);
		levelManager.init();
		gameState = GameStateType.STORY;
		player.restoreFullHealth();
	}

	public void startGameThread() { 
		gameThread = new Thread(this); // 'this' means we are passing this gamePanel class in as a parameter into our thread
		gameThread.start(); // automatically calls the 'run' method 
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
		switch(gameState) {
			case LOADING:
				loadTimer++;
				if(loadTimer >= 120) { // wait on loading screen for 2 seconds
					stopMusic();
					playMusic(levelManager.getCurrentLevel());
					gameState = GameStateType.PLAY;
					player.setMovement(true);
					loadTimer = 0;
				}
			case PLAY:
				playing.update(); // update playing gamestate
				break;
			case SETTINGS: // pass on other gamestates as nothing requires updating
			case TITLE:
			case NULL:
			case STORY:
			case PAUSE:
			case GAMEOVER:
			case RULES:
				break;
		}
	}
	
	public void paintComponent(Graphics g) { // standard built-in methods to draw things on JPanel in Java
		super.paintComponent(g); // just have to do it whenever you use paintComponent method 
		
		Graphics2D g2 = (Graphics2D)g;
		// Graphics2D class extends the Graphics class to provide greater control over geometry, coordinate transformations, colour management and text layout 
		// Graphics 2D has more functions than just Graphics on its own 
		
		// DEBUG
		long drawStart = 0; 
		if (debug) { 
			drawStart = System.nanoTime();
		}
		
		// DRAW GAME OBJECTS
		if(gameState == GameStateType.PLAY || gameState == GameStateType.PAUSE) {
			playing.draw(g2);
		} else {
			ui.draw(g2);
		}

		if (debug) { 
			long drawEnd = System.nanoTime();
			long passed = drawEnd - drawStart; 
			g2.setColor(Color.white);
			g2.drawString("Draw time: "  + passed, 10, 400);
			System.out.println("Draw time: " + passed);
		}			
		g2.dispose(); // once drawn, dispose of this graphics context and release any system resources it is using (good practice) 
	}
	
	public void playMusic(int i) { 
		music.setFile(i);
		music.play();
		music.loop();
	}
	
	public void stopMusic() { 
		try {
			music.stop();
		} catch (Exception e) {
			// fixes random bug when game is loaded too fast at the start
		}
	}

	public void playSoundEffect(int i) { 
		se.setFile(i);
		se.play(); // we dont call loop for sound effects because these are generally really short 
	}

	// Getters and setters
	public void setGameState(GameStateType gameState) {
		this.gameState = gameState;
	}

	public GameStateType getGameState() {
		return gameState;
	}

	public Projectile[] getProjectiles() {
		return proj;
	}

	public Projectile getProjectiles(int index) {
		return proj[index];
	}

	public Enemy[] getEnemy() {
		return enemies;
	}

	public Enemy getEnemy(int index) {
		return enemies[index];
	}

	public void setEnemy(int index, Enemy enemy) {
		enemies[index] = enemy;
	}

	public void setProjectile(int index, Projectile projectile) {
		proj[index] = projectile;
	}

	public void resetProjectiles() {
		for(int i = 0; i < proj.length; i++) {
			proj[i] = null;
		}
	}

	public void resetEnemies() {
		for(int i = 0; i < enemies.length; i++) {
			enemies[i] = null;
		}
	}

	public UI getUi() {
		return ui;
	}

	public Player getPlayer() {
		return player;
	}

	public CollisionChecker getCollisionChecker() {
		return cChecker;
	}

	public AssetSetter getAssetSetter() {
		return aSetter;
	}

	public KeyHandler getKeyHandler() {
		return keyH;
	}

	public LevelManager getLevelManager() {
		return levelManager;
	}

	public SuperObject[] getObjects() {
		return obj;
	}

	public SuperObject getObjects(int index) {
		return obj[index];
	}

	public Sound getMusic() {
		return music;
	}

	public void toggleDebug() {
		debug = !debug;
	}

	public boolean getDebug() {
		return debug;
	}

	public int getLoadTimer() {
		return loadTimer;
	}
}
/*
 * General notes - without an FPS counter, touching a key will update the position around a million times a second and move the rectangle off the screen 
 * Therefore FPS counter is necessary 
 * Generally delta FPS measure is better than the sleep FPS measure (can mention in NEA) 
 * 
*/
