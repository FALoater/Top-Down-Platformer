package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

import entity.Player;
import entity.enemy.Enemy;

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
	public static final int worldWidth = tileSize * maxWorldCol; 
	public static final int worldHeight = tileSize * maxWorldRow; 
	
	private boolean debug; //debug mode to print out useful information

	//FPS
	private int FPS = 60;
	private Thread gameThread; //very useful for game loops (need to implement runnable to use this thread) 
	private KeyHandler keyH = new KeyHandler(this);

	// GAME OBJECTS
	private Enemy enemies[] = new Enemy[10];
	private Player player = new Player(this, keyH);

	// GAME MANAGERS
	private AssetSetter aSetter = new AssetSetter(this); // this will be used to set up the assets in the game
	private LevelManager levelManager = new LevelManager(this); // this will be used to manage the levels in the game
	private UI ui = new UI(this); // this will be used to manage the UI in the game

	// GAME STATE
	//game state - various game situations (title screen, main game play screen, menu screen, pause screen etc.) 
	//depending on the situation, the program draws something different on the screen 
	private int gameState; 
	private final int titleState = 0; //for title screen 
	private final int playState = 1; 
	private final int pauseState = 2;
	private final int dialogueState = 3;

	// Create gamePanel constructor 
	public GamePanel() { 
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true); // similar to BufferStrategy (improves game rendering process)
		this.addKeyListener(keyH); //add the keyHandler to the gamePanel
		this.setFocusable(true); //needed for GamePanel to be 'focused' to receive key input 
	}
	
	//setupGame() method should be called before the thread starts running so position it correctly in the main class
	public void setupGame() { 
		gameState = titleState;
		levelManager.init();
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
			player.update();

			for(int i = 0; i <enemies.length;i++) {
				if(enemies[i] != null) {
					if(enemies[i].isMarkedForDeletion()) {
						enemies[i] = null;
						//levelManager.decreaseNumberOfEnemies();
					} else {
						enemies[i].update();
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
		} else {
			levelManager.draw(g2);

			for (int i = 0; i < enemies.length; i++) {
				if (enemies[i] != null) {
					enemies[i].draw(g2);
				}
			}

			player.draw(g2);
			if(debug) player.drawHitbox(g2);

			ui.draw(g2);

			// DEBUG
			if (debug) {
				long drawEnd = System.nanoTime();
				long passed = drawEnd - drawStart;
				g2.drawString("Draw time: "  + passed, 10, 400);
				System.out.println("Draw time: " + passed);
			}
			g2.dispose(); 
		}
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

	public int getDialogueState() {
		return dialogueState;
	}

	public KeyHandler getKeyHandler() {
		return keyH;
	}

	public Player getPlayer() {
		return player;
	}

	public UI getUi() {
		return ui;
	}

	public AssetSetter getAssetSetter() {
		return aSetter;
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

	public void toggleDebug() {
		debug = !debug;
	}
}