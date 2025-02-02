package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import gamestates.GameStateType;

public class KeyHandler implements KeyListener {
	// implements KeyListener is necessary 
	// KeyListener - the listener interface for receiving keyboard events (keystrokes) 
	private boolean upPressed, downPressed, leftPressed, rightPressed, attackPressed;

	private GamePanel gp;
	
	//Constructor
	public KeyHandler(GamePanel gp) {
		this.gp = gp;
	}
	
	// unimplemented methods (compulsory) 
	@Override
	public void keyTyped(KeyEvent e) {}
 
	@Override
	public void keyPressed(KeyEvent e) {	
		int code = e.getKeyCode(); // basically returns the number of the key that was pressed 
	
		switch(gp.getGameState()) {
			case TITLE:
				if(code == KeyEvent.VK_W) { // move arrow up
					gp.getUi().decrementCommandNum();
					if(gp.getUi().getCommandNum() < 0) {
						gp.getUi().setCommandNum(2); //prevents the arrow from moving into oblivion 
					}
				}
				if(code == KeyEvent.VK_S) { // move arrow down
					gp.getUi().incrementCommandNum();
					if(gp.getUi().getCommandNum() > 2) {
						gp.getUi().setCommandNum(0);
					}
				}
				
				if(code == KeyEvent.VK_ENTER) { // respond to relevant option selected
					if(gp.getUi().getCommandNum() == 0) {
						gp.stopMusic();
						gp.setGameState(GameStateType.STORY);
					}
					if(gp.getUi().getCommandNum() == 1) {
						gp.setGameState(GameStateType.SETTINGS);
					}
					if(gp.getUi().getCommandNum() == 2) { 
						System.exit(0);
					}
				}
				break;
			case PLAY:
				// update player movement 
				if(code == KeyEvent.VK_W) {
					upPressed = true;
				}
				if(code == KeyEvent.VK_S) {
					downPressed = true; 		
				}
				if(code == KeyEvent.VK_A) {
					leftPressed = true;
				}
				if(code == KeyEvent.VK_D) {
					rightPressed = true;
				}
				// pause menu
				if(code == KeyEvent.VK_ESCAPE) {
					gp.setGameState(GameStateType.PAUSE);
				// attacks
				}
				if(code == KeyEvent.VK_E) {
					attackPressed = true;
					gp.getPlayer().attack();
				}
				if(code == KeyEvent.VK_R) {
					gp.getPlayer().reloadAmmo();
				}
				// DEBUG
				if(code == KeyEvent.VK_Q) { 
					gp.toggleDebug();
				}
				break;
			case STORY:
				// go to next bit
				if(code == KeyEvent.VK_ENTER) {
					gp.getUi().incrementDialogue(); 
				}
				// skip story
				if(code == KeyEvent.VK_Q) {

					gp.setGameState(GameStateType.LOADING);
				}
				break;
			case GAMEOVER:
				// MAIN MENU, RESTART, QUIT
				if(code == KeyEvent.VK_W) {
					gp.getUi().decrementCommandNum();
					if(gp.getUi().getCommandNum() < 0) {
						gp.getUi().setCommandNum(2); //prevents the arrow from moving into oblivion 
					}
				}
				if(code == KeyEvent.VK_S) {
					gp.getUi().incrementCommandNum();
					if(gp.getUi().getCommandNum() > 2) {
						gp.getUi().setCommandNum(0);
					}
				}
				
				if(code == KeyEvent.VK_ENTER) {
					if(gp.getUi().getCommandNum() == 0) {
						gp.stopMusic();
						gp.setGameState(GameStateType.TITLE);
						gp.playMusic(Sound.MAIN_MENU);
					}
					if(gp.getUi().getCommandNum() == 1) {
						gp.stopMusic();
						gp.restartGame();
					}
					if(gp.getUi().getCommandNum() == 2) { 
						System.exit(0);
					}
				}
				break;
			case PAUSE:
				if(code == KeyEvent.VK_ESCAPE) {
					gp.setGameState(GameStateType.PLAY); // resume play
				}
				break;
			case SETTINGS:
				if(gp.getUi().getOptionSelection() == 0) { // selected on sound option
					// left arrow key
					if(code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) {
						gp.getUi().setCommandNum(2);
						gp.getMusic().decreaseSound();
					}
					// right arrow key
					if(code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) {
						gp.getUi().setCommandNum(3);
						gp.getMusic().increaseSound();
					}
				}
				// select bottom option
				if(code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
					gp.getUi().setOptionSelection(1);
				}
				// select top option
				if(code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
					gp.getUi().setOptionSelection(0);
				}
				// go back to main menu
				if(code == KeyEvent.VK_ENTER && gp.getUi().getOptionSelection() == 1) {
					gp.setGameState(GameStateType.TITLE);
				}
				break;
			case LOADING:
			case NULL:
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();

		switch(gp.getGameState()) {
			case PLAY:
				// update valuses
				if(code == KeyEvent.VK_W) {
					upPressed = false;
				}
				if(code == KeyEvent.VK_S) {
					downPressed = false; 		
				}
				if(code == KeyEvent.VK_A) {
					leftPressed = false;
				}
				if(code == KeyEvent.VK_D) {
					rightPressed = false;
				}
				if(code == KeyEvent.VK_E) {
					attackPressed = false;
				}
				break;
			case SETTINGS:
				// remove arrow highlight
				gp.getUi().setCommandNum(1);
				break;
			case GAMEOVER:
			case LOADING:
			case NULL:
			case PAUSE:
			case STORY:
			case TITLE:
				break;
		}
	}

	// getters and setters

	public boolean isUpPressed() {
		return upPressed;
	}

	public boolean isDownPressed() {
		return downPressed;
	}

	public boolean isLeftPressed() {
		return leftPressed;
	}

	public boolean isRightPressed() {
		return rightPressed;
	}

	public boolean isAttackPressed() {
		return attackPressed;
	}
}