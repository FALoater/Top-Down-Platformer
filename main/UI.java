package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import gamestates.GameStateType;
import object.OBJ_Heart;
import object.SuperObject;

import static main.GamePanel.tileSize;
import static main.GamePanel.screenHeight;
import static main.GamePanel.screenWidth;

public class UI {
	// store for game over
	private int commandNum = 0;
	private int sentenceNo = 0;

	//handles all the on-screen UI 
	private String[] storyStartLines = {"HELLO AND WELCOME TO THE GAME", "YOU ARE QUITE BAD"};// the current dialogue to be displayed on the screen
	private BufferedImage heart_full, heart_half, heart_blank;
	private Font arial_40;
	private GamePanel gp; 
	private Graphics2D g2;
	
	public UI (GamePanel gp) {
		this.gp = gp;
		arial_40 = new Font("Arial", Font.PLAIN, 40); //instantiate in the constructor once 
	
		// CREATE HUD OBJECT 
		SuperObject heart = new OBJ_Heart();
		BufferedImage[] images = heart.getImages();
		heart_full = images[0];
		heart_half = images[1];
		heart_blank = images[2];
	}
	
	public void draw(Graphics2D g2) { 
		this.g2 = g2;
		
		g2.setFont(arial_40);
		g2.setColor(Color.white);

		GameStateType gameState = gp.getGameState();

		switch(gameState) {
			case TITLE:
				drawTitleScreen();
				break;
			case PLAY:
				drawPlayerLife();
				break;
			case PAUSE:
				drawPlayerLife();
				drawPauseScreen();
				break;
			case STORY:
				drawDialogueScreen(sentenceNo);
				break;
			case GAMEOVER:
				break;
			case NULL:
				break;
			case LOADING:
				drawLoadingScreen();	
				break;
			case SETTINGS:
				break;
			default:
				break;
		}
	}
	
	private void drawPlayerLife() {
		int x = tileSize/2; 
		int y = tileSize/2; 
		int i = 0; 
		
		//DRAW MAX LIFE
		while (i < gp.getPlayer().getMaxLife()/2 ) {
			g2.drawImage(heart_blank, x, y, null); 
			i++; 
			x+= tileSize;
		}
		
		//RESET VALUES
		x = tileSize/2; 
		y = tileSize/2; 
		i = 0;
		
		//DRAW CURRENT LIFE
		while (i < gp.getPlayer().getCurrentLife()) {
			g2.drawImage(heart_half, x, y, null);
			i++;
			if(i < gp.getPlayer().getCurrentLife()) {
				g2.drawImage(heart_full, x, y, null);
			}
			i++;
			x+= tileSize;
		}
	}

	private void drawTitleScreen() {
		g2.setColor(new Color(0,0,0));
		g2.fillRect(0, 0, screenWidth, screenHeight);
		
		// TITLE NAME
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,70F));
		String text = "Top Down Platformer"; // change this to the name of your game
		int x = getXforCentredText(text); 
		int y = tileSize * 3; 
		
		//SHADOW
		g2.setColor(Color.gray);
		g2.drawString(text, x+5, y+5);
		
		//MAIN COLOUR
		g2.setColor(Color.white);
		g2.drawString(text, x, y);
		
		// BLUE BOY IMAGE
		x = screenWidth/2 - (tileSize*2)/2; 
		y += tileSize*2;
		g2.drawImage(gp.getPlayer().getPlayerMenuImg(), x, y, tileSize*2, tileSize*2, null);
		
		//MENU 
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
		
		text = "NEW GAME";
		x = getXforCentredText(text); 
		y += tileSize*3.5; //add a further four tiles down from the image 
		g2.drawString(text, x, y);
		if (commandNum == 0) { 
			g2.drawString(">", x-tileSize, y); // points a little arrow to the left of each string
		}
		
		text = "LOAD GAME";
		x = getXforCentredText(text); 
		y += tileSize; //add a further four tiles down from the image 
		g2.drawString(text, x, y);
		if (commandNum == 1) { 
			g2.drawString(">", x-tileSize, y); // points a little arrow to the left of each string
		}
		
		text = "QUIT";
		x = getXforCentredText(text); 
		y += tileSize; //add a further four tiles down from the image 
		g2.drawString(text, x, y);
		if (commandNum == 2) { 
			g2.drawString(">", x-tileSize, y); // points a little arrow to the left of each string
		}
	}

	private void drawDialogueScreen(int dialogueNum) {
	    // WINDOW 
	    int x = tileSize * 2; 
	    int y = tileSize / 2; 
	    int width = screenWidth - (tileSize * 4); 
	    int height = tileSize * 4;
	    
	    drawSubWindow(x, y, width, height); // Pass everything to the method below
	    
	    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
	    x += tileSize;
	    y += tileSize;
	    
	    // Split the text inside the dialogue at the backslash n keyword every 26 characters
		String[] currentDialogue = new String[(storyStartLines[dialogueNum].length() / 26) + 1];

		for(int i=0;i<currentDialogue.length;i++) {
			if(i == currentDialogue.length-1) {
				currentDialogue[i] = storyStartLines[dialogueNum].substring(i*26);
			} else {
				currentDialogue[i] = storyStartLines[dialogueNum].substring(i*26, (i+1)*26);
			}
		}

		for(String dialogue : currentDialogue) {
			g2.drawString(dialogue, x, y);
			y += 40;
		}
	}
	
	private void drawPauseScreen() {
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
		String text = "PAUSED"; 
		int x = getXforCentredText(text), y = screenHeight/2; 
		g2.drawString(text, x, y);
	}
	
	private void drawLoadingScreen() {
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
		String text = "LOADING LEVEL " + gp.getLevelManager().getCurrentLevel(); 
		int x = getXforCentredText(text), y = screenHeight/2; 
		g2.drawString(text, x, y);
	}

	private void drawSubWindow(int x, int y, int width, int height) {
		Color c = new Color(0,0,0, 210); // RGB number for black, fourth number = alpha value (transparency level)
		g2.setColor(c);
		g2.fillRoundRect(x, y, width, height, 35, 35);
		
		c = new Color(255,255,255);//RGB number for white
		g2.setColor(c);
		g2.setStroke(new BasicStroke(5));
		//setStroke defines the width of outline graphics which are rendered with a Graphics2D
		
		g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
	}

	private int getXforCentredText(String text) { 
		//position x to the centre of the screen
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = screenWidth/2 - length/2;
		return x;
	}
	// getters and setters

	public void decrementCommandNum() {
		this.commandNum--;
	}

	public void incrementCommandNum() {
		this.commandNum++;
	}

	public void incrementDialogue() {
		this.sentenceNo++;
		if(sentenceNo >= storyStartLines.length) gp.setGameState(GameStateType.LOADING);
	}

	public void setCommandNum(int commandNum) {
		this.commandNum = commandNum;
	}

	public int getCommandNum() {
		return commandNum;
	}
}