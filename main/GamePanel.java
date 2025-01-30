package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

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
	
	//FPS
	private int FPS = 60;
	private Thread gameThread; //very useful for game loops (need to implement runnable to use this thread) 


	// GAME STATEt
	//game state - various game situations (title screen, main game play screen, menu screen, pause screen etc.) 
	//depending on the situation, the program draws something different on the screen 
	private int gameState; 
	private final int titleState = 0; //for title screen 
	private final int playState = 1; 
	private final int pauseState = 2;
	private final int dialogueState = 3; // not sure if will be used

	// Create gamePanel constructor 
	public GamePanel() { 
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true); // similar to BufferStrategy (improves game rendering process)
		this.setFocusable(true); //needed for GamePanel to be 'focused' to receive key input 
	}
	
	//setupGame() method should be called before the thread starts running so position it correctly in the main class
	public void setupGame() { 
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
		
		//TITLE SCREEN
		g2.dispose(); // once drawn, dispose of this graphics context and release any system resources it is using (good practice) 
		
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

}