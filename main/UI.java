package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

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
	private int optionSelection = 0;
	private boolean victory = false;

	//handles all the on-screen UI 
	private String[] storyStartLines = {"HELLO AND WELCOME TO THE GAME", "YOU ARE QUITE BAD"};// the current dialogue to be displayed on the screen
	private BufferedImage heart_full, heart_half, heart_blank, bullet;
	private Font arial_40;
	private GamePanel gp; 
	private Graphics2D g2;
	
	public UI (GamePanel gp) {
		this.gp = gp;
		arial_40 = new Font("Arial", Font.PLAIN, 40); // instantiate in the constructor once 
	
		// create hud during game state
		SuperObject heart = new OBJ_Heart();
		BufferedImage[] images = heart.getImages();
		heart_full = images[0];
		heart_half = images[1];
		heart_blank = images[2];
		bullet = setup("/assets/objects/bullet");
	}

	private BufferedImage setup(String imagePath) { 
		// format heart image
		UtilityTool uTool = new UtilityTool();
		BufferedImage image = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
		
		try { 
			image = ImageIO.read(getClass().getResourceAsStream(imagePath +".png"));
			image = uTool.scaleImage(image, tileSize, tileSize);
			
		}catch(IOException e) { 
			e.printStackTrace();
		}
		return image;
    }
	
	public void draw(Graphics2D g2) { 
		this.g2 = g2;
		
		g2.setFont(arial_40);
		g2.setColor(Color.white);

		GameStateType gameState = gp.getGameState();

		switch(gameState) {
			// draw the ui layout depending on the game state
			case TITLE:
				drawTitleScreen();
				break;
			case PLAY:
				drawPlayerLife();
				drawPlayerBullets();
				break;
			case PAUSE:
				drawPlayerLife();
				drawPlayerBullets();
				drawPauseScreen();
				drawVolumeSlider();
				break;
			case STORY:
				drawDialogueScreen(sentenceNo);
				break;
			case GAMEOVER:
				drawGameOverScreen();
			case NULL:
				break;
			case LOADING:
				drawLoadingScreen();	
				break;
			case SETTINGS:
				drawSettingsScreen();
				break;
			case RULES:
				drawRulesScreen();
				break;
		}
	}

	private void drawTitleScreen() {
		g2.setColor(new Color(0,0,0));
		g2.fillRect(0, 0, screenWidth, screenHeight);
		
		// game title
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,70F));
		String text = "Top Down Platformer"; // name of the game
		int x = getXforCentredText(text); 
		int y = tileSize * 3; 
		
		// shadow highlight
		drawTextWithShadow(text, x, y);
		
		// game logo which is main character
		x = screenWidth/2 - (tileSize*2)/2; 
		y += tileSize*2;
		g2.drawImage(gp.getPlayer().getPlayerMenuImg(), x, y, tileSize*2, tileSize*2, null);
		
		// menu underneath
		String[] options = {"NEW GAME", "SETTINGS", "RULES", "QUIT"};

		// draw the menu options, new game, load game and quit
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
		y += tileSize*2.5;

		for(int i=0;i<options.length;i++) {
			y += tileSize;
			drawMenuOption(options[i], getXforCentredText(options[i]), y, commandNum == i);
		}
	}

	private void drawDialogueScreen(int dialogueNum) {
	    // background window at top
	    int x = tileSize * 2; 
	    int y = tileSize / 2; 
	    int width = screenWidth - (tileSize * 4); 
	    int height = tileSize * 4;
	    
	    drawSubWindow(x, y, width, height); // pass everything to the method below
	    
	    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
	    x += tileSize;
	    y += tileSize;
	    
	    // split the text inside the dialogue at the backslash n keyword every 26 characters
		String[] currentDialogue = new String[(storyStartLines[dialogueNum].length() / 26) + 1];

		for(int i=0;i<currentDialogue.length;i++) {
			if(i == currentDialogue.length-1) {
				currentDialogue[i] = storyStartLines[dialogueNum].substring(i*26);
			} else {
				currentDialogue[i] = storyStartLines[dialogueNum].substring(i*26, (i+1)*26);
			}
		}

		for(String dialogue : currentDialogue) {
			// draw the current dialogue string
			g2.drawString(dialogue, x, y);
			y += 40;
		}
	}
	
	private void drawPauseScreen() {
		g2.setColor(new Color(0, 0, 0, 100)); // alpha transparency value of 100 enables game to be seen
		g2.fillRect(0, 0, screenWidth, screenHeight);

		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));

		String text = "PAUSED"; 
		drawTextWithShadow(text, getXforCentredText(text), tileSize * 2);
	}
	
	private void drawLoadingScreen() {
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
		String text = "LOADING LEVEL " + gp.getLevelManager().getCurrentLevel(); 
		int x = getXforCentredText(text), y = screenHeight/2; 
		g2.drawString(text, x, y);
	}

	private void drawGameOverScreen() {
		g2.setColor(new Color(0,0,0));
		g2.fillRect(0, 0, screenWidth, screenHeight);
		
		// TITLE NAME
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,70F));
		String text = "GAME OVER";
		if(victory) text = "YOU WON!";
		int x = getXforCentredText(text);
		int y = tileSize * 3; 
		
		//SHADOW
		drawTextWithShadow(text, x, y);

		// extra text underneath
		String subText = "You made it to level " + gp.getLevelManager().getCurrentLevel() + ".";
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
		g2.setColor(Color.white);
		g2.drawString(subText, getXforCentredText(subText), y + 50);

		// draw the menu options, main menu, play again and quit
		String[] options = {"MAIN MENU", "PLAY AGAIN", "QUIT"};

		// draw the menu options, new game, load game and quit
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
		y += tileSize * 5;

		for(int i=0;i<options.length;i++) {
			drawMenuOption(options[i], getXforCentredText(options[i]), y, commandNum == i);
			y += tileSize;
		}

	}

	private void drawSettingsScreen() {
		// fill background colour as black
		g2.setColor(new Color(0,0,0));
		g2.fillRect(0, 0, screenWidth, screenHeight);
		
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,70F));
		String text = "SETTINGS";
		int x = getXforCentredText(text);
		int y = tileSize * 2; 
		
		// shadow
		drawTextWithShadow(text, x, y);

		// draw options underneath
		drawVolumeSlider();
	}

	private void drawRulesScreen() {
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,70F));

		int y = tileSize * 2;
		String text = "Rules & Controls";
		g2.drawString(text, getXforCentredText(text), y);

		y += tileSize;
		String[] rules = {"W / Up", "A / Left", "S / Down", "D / Right", "Q", "E", "R", "Escape"};
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,30F));

		for(String line : rules) {
			g2.drawString(line, getXforCentredText(line), y);
			y += tileSize * 0.75;
		}

		g2.setColor(Color.red);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,50F));

		text = "Back to Menu";
		int x = getXforCentredText(text);
		y = (int) (tileSize * 10.5);

		g2.drawString(text, x, y);
	}

	private void drawVolumeSlider() {
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,50F));
		boolean shadow0 = true, shadow1 = true, shadow2 = true;

		switch(optionSelection) {
			case 0:
				shadow0 = false;
				break;
			case 1:
				shadow1 = false;
				break;
			case 2:
				shadow2 = false;
				break; 
		}
		// draw sound effect option
		String text = "SFX:";
		int y = tileSize * 4;
		drawTextWithShadow(text, getXforCentredText(text), y);

		y += tileSize;
		text = String.valueOf(gp.getSfx().getSfxToggle());
		drawText(text, getXforCentredText(text), y, shadow0); // draw toggle button

		// draw volume option
		text = "Volume:";
		y = tileSize * 7;
		int x = getXforCentredText(text);
		drawText(text, x, y, shadow1);

		// draw volume number
		text = String.valueOf(gp.getMusic().getSoundLevel());
		y += tileSize;
		x = getXforCentredText(text);
		drawTextWithShadow(text, x, y);

		if(commandNum == 2) g2.setColor(Color.red);
		g2.drawString("<", x - tileSize * 2, y); // draw left arrow 
		g2.setColor(Color.white); // reset colour

		// calculate position of right side
		if(commandNum == 3) g2.setColor(Color.red);
		x += 10;
		if(text.equals("10")) x += 30;
		
		// draw right arrow
		g2.drawString(">", x + tileSize * 2, y);

		// draw back to menu
		text = "Back to Menu";
		drawText(text, getXforCentredText(text), y + 3 * tileSize, shadow2);
	}

	private void drawText(String text, int x, int y, boolean shadow) {
		if(shadow) {
			drawTextWithShadow(text, x, y);
		} else {
			g2.setColor(Color.red);
			g2.drawString(text, x, y);
			g2.setColor(Color.white);
		}
		
	}

	private void drawTextWithShadow(String text, int x, int y) {
		g2.setColor(Color.gray);
		g2.drawString(text, x + 5, y + 5);
		g2.setColor(Color.white);
		g2.drawString(text, x, y);
	}

	private void drawPlayerLife() {
		int x = tileSize/2; 
		int y = tileSize/2; 
		int i = 0; 
		
		// draw max hearts
		while (i < gp.getPlayer().getMaxLife()/2 ) {
			g2.drawImage(heart_blank, x, y, null); 
			i++; 
			x += tileSize;
		}
		
		// reset x and y values
		x = tileSize/2; 
		y = tileSize/2; 
		i = 0;
		
		// draw current player health
		while (i < gp.getPlayer().getCurrentLife()) {
			g2.drawImage(heart_half, x, y, null);
			i++;

			if(i < gp.getPlayer().getCurrentLife()) {
				g2.drawImage(heart_full, x, y, null);
			}

			i++;
			x += tileSize;
		}
	}

	private void drawPlayerBullets() {
		int x = (int)(screenWidth - tileSize * 1.5); 
		int y = tileSize/2; 

		int ammo = gp.getPlayer().getAmmo();

		// draw current number of bullets
		if(ammo > 0) {
			for(int i = 0;i<ammo;i++) {
				g2.drawImage(bullet, x, y, null);
				x -= tileSize;
			}
		} else {
			// if no bullets show reloading animation
			g2.setColor(Color.white);
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 30F));
			g2.drawString("RELOADING", (int)(screenWidth - tileSize * (1.5 + 2) - 10), y + 30);
			g2.setColor(Color.red);
			g2.drawRect((int)(screenWidth - tileSize * (1.5 + 2)), y + 50, tileSize * 3, 10); // calculate percentage of bar filled using player attack cooldown timer
			g2.fillRect((int)(screenWidth - tileSize * (1.5 + 2)), y + 50, (int) ((float) (tileSize * 3 * gp.getPlayer().getAttackTimer() / 120)), 10);
		}
	}
	
	private void drawSubWindow(int x, int y, int width, int height) {
		Color c = new Color(0,0,0, 210); // RGB number for black, fourth number = alpha value (transparency level)
		g2.setColor(c);
		g2.fillRoundRect(x, y, width, height, 35, 35);
		
		c = new Color(255,255,255);// RGB number for white
		g2.setColor(c);
		g2.setStroke(new BasicStroke(5));
		// setStroke defines the width of outline graphics which are rendered with a Graphics2D
		
		g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
	}

	private void drawMenuOption(String text, int x, int y, boolean selected) {
		g2.setColor(Color.white);

		if(selected) {
			g2.setColor(Color.red);
			g2.drawString(">", x-tileSize, y); 	// points a little arrow to the left of each string
		}
	
		g2.drawString(text, x, y);
	}

	private int getXforCentredText(String text) { 
		//position x to the centre of the screen
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = screenWidth / 2 - length / 2;
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
		if(sentenceNo >= storyStartLines.length) {
			sentenceNo = 0;
			gp.setGameState(GameStateType.LOADING);
		}
	}

	public void setCommandNum(int commandNum) {
		this.commandNum = commandNum;
	}

	public int getCommandNum() {
		return commandNum;
	}

	public void incrementOptionSelection() {
		optionSelection++;
		if(optionSelection >= 3) optionSelection = 0;	
	}

	public void decrementOptionSelection() {
		optionSelection--;
		if(optionSelection < 0) optionSelection = 2;
	}

	public void setOptionSelection(int optionSelection) {
		this.optionSelection = optionSelection;
	}

	public int getOptionSelection() {
		return optionSelection;
	}

	public void setVictory(boolean victory) {
		this.victory = victory;
	}
}