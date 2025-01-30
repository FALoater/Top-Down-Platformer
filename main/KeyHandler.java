package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
	// implements KeyListener is necessary 
	// KeyListener - the listener interface for receiving keyboard events (keystrokes) 
	private boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, attackPressed;

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
		
		//TITLE STATE
		if (gp.getGameState() == GameStates.TITLE) {
			if (gp.getUi().getTitleScreenState() == 0) {
				if (code == KeyEvent.VK_W) {
					gp.getUi().decrementCommandNum();
					if(gp.getUi().getCommandNum() < 0) {
						gp.getUi().setCommandNum(2); //prevents the arrow from moving into oblivion 
					}
				}
				if (code == KeyEvent.VK_S) {
					gp.getUi().incrementCommandNum();
					if(gp.getUi().getCommandNum() > 2) {
						gp.getUi().setCommandNum(0);
					}
				}
				
				if (code == KeyEvent.VK_ENTER) {
					if (gp.getUi().getCommandNum() == 0) {
						gp.setGameState(GameStates.PLAY);
						gp.playMusic(0);
					}
					if (gp.getUi().getCommandNum() == 1) {
						//add later
					}
					if(gp.getUi().getCommandNum() == 2) { 
						System.exit(0);
					}
				}
			}
		}
		
		//PLAY STATE 
		if(gp.getGameState() == GameStates.PLAY) {
			if (code == KeyEvent.VK_W) {
				upPressed = true; //update boolean value 
			}
			if (code == KeyEvent.VK_S) {
				downPressed = true; 		
			}
			if (code == KeyEvent.VK_A) {
				leftPressed = true;
			}
			if (code == KeyEvent.VK_D) {
				rightPressed = true;
			}
			if (code == KeyEvent.VK_P) {
				gp.setGameState(GameStates.PAUSE);
			}
			if (code == KeyEvent.VK_ENTER) {
				enterPressed = true;
			}
			if (code == KeyEvent.VK_E) {
				attackPressed = true;
				gp.getPlayer().attack();
			}
			if (code == KeyEvent.VK_Z) {
				gp.getLevelManager().loadNextLevel();
			}
			// DEBUG
			if (code == KeyEvent.VK_Q) { 
				gp.toggleDebug();
			}
		}
		
		// PAUSE STATE 
		else if (gp.getGameState() == GameStates.PAUSE) {
			if (code == KeyEvent.VK_P) {
				gp.setGameState(GameStates.PLAY);
			}
		}
		
		// DIALOGUE STATE
		if (gp.getGameState() == GameStates.DIALOGUE) {
			if (code == KeyEvent.VK_ENTER) {
				gp.setGameState(GameStates.PLAY);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();

		if (code == KeyEvent.VK_W) {
			upPressed = false; //update boolean value 
		}
		if (code == KeyEvent.VK_S) {
			downPressed = false; 		
		}
		if (code == KeyEvent.VK_A) {
			leftPressed = false;
		}
		if (code == KeyEvent.VK_D) {
			rightPressed = false;
		}
		if (code == KeyEvent.VK_E) {
			attackPressed = false;
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

	public boolean isEnterPressed() {
		return enterPressed;
	}

	public boolean isAttackPressed() {
		return attackPressed;
	}

	public void setEnterPressed(boolean enterPressed) {
		this.enterPressed = enterPressed;
	}
}