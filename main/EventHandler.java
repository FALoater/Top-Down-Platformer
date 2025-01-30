package main;

import static main.GamePanel.tileSize;
import static main.GamePanel.maxWorldCol;
import static main.GamePanel.maxWorldRow;

public class EventHandler {

    private GamePanel gp; 
    private EventRect eventRect[][];

    //VARIABLES
    private int previousEventX, previousEventY; 
    private boolean canTouchEvent = true;
    
    // Constructor 
    public EventHandler(GamePanel gp) {
        this.gp = gp;
        eventRect = new EventRect[maxWorldCol][maxWorldRow];
        
        int col = 0; 
        int row = 0; 
        while (col < maxWorldCol && row < maxWorldRow) {
        	// Initialize eventRect with the new position
            eventRect[col][row] = new EventRect();
            eventRect[col][row].x = 23;
            eventRect[col][row].y = 23;
            eventRect[col][row].width =2;
            eventRect[col][row].height = 2;
            // Calculate the eventRect's position based on the tile size and the new coordinates
            eventRect[col][row].setEventRectDefaultX(eventRect[col][row].x);
            eventRect[col][row].setEventRectDefaultY(eventRect[col][row].y);
            
            col++;
            if(col == maxWorldCol) {
            	col = 0; 
            	row++;
            }
        }
    }

    public void checkEvent() { 
    	//Check if player character is more than 1 tile away from the last event 
    	int xDistance = Math.abs(gp.getPlayer().getWorldX() - previousEventX); //returns positive even if answer is negative 
    	int yDistance = Math.abs(gp.getPlayer().getWorldY() - previousEventY);
    	int distance = Math.max(xDistance, yDistance); //picks the greater number of the two integeres within it 
    	if (distance > tileSize){ 
    		canTouchEvent = true;
    	}
    	
    	if (canTouchEvent == true) { 
    		if (hit(27, 17, "down") == true) { // Check for eventRect position
                // Event happens
                damagePit(27, 17, GameStates.DIALOGUE);
    		}
    		//TESTING PURPOSES ONLY 
    		if (hit(23, 19, "any") == true) { // Check for eventRect position
                // Event happens
                damagePit(23, 19, GameStates.DIALOGUE);
    		}
            if(hit(23,7, "up") == true) {
            	healingPool(23, 7, GameStates.DIALOGUE);
            }
    	}
    }

	private void damagePit(int col, int row, GameStates gameState) {
        gp.setGameState(gameState);
        gp.getUi().setCurrentDialogue("You fell into a pit!");
        gp.getPlayer().takeDamage(1);
        canTouchEvent = false; 
    }

    public boolean hit(int col, int row, String regDirection) {
        boolean hit = false;

        // Calculate the pixel position of the eventRect
        eventRect[col][row].x = col * tileSize + eventRect[col][row].x;;
        eventRect[col][row].y = row * tileSize + eventRect[col][row].y;;

        // Get player's current solidArea positions
        gp.getPlayer().incrementSolidAreaX(gp.getPlayer().getWorldX());
        gp.getPlayer().incrementSolidAreaY(gp.getPlayer().getWorldY());

        // Check if the player's solidArea is colliding with eventRect
        if (gp.getPlayer().getSolidArea().intersects(eventRect[col][row]) && eventRect[col][row].isEventDone() == false) {
            if(gp.getPlayer().getDirection().contentEquals(regDirection) || regDirection.contentEquals("any")) {
                hit = true;
                previousEventX = gp.getPlayer().getWorldX(); 
                previousEventY = gp.getPlayer().getWorldY();
        	}
        }

        // After checking the collision, reset the solidArea x and y
        gp.getPlayer().setSolidAreaX(gp.getPlayer().getSolidAreaDefaultX());
        gp.getPlayer().setSolidAreaY(gp.getPlayer().getSolidAreaDefaultY());

        // Reset eventRect to default position
        eventRect[col][row].x = eventRect[col][row].getEventRectDefaultX();
        eventRect[col][row].y = eventRect[col][row].getEventRectDefaultY();

        return hit;
    }
    
    public void healingPool(int col, int row, GameStates gameState) {
    	if(gp.getKeyHandler().isEnterPressed()) {
    		gp.setGameState(gameState);
    		gp.getUi().setCurrentDialogue("You drink the water.\nYour life has been recovered!");
    		gp.getPlayer().restoreFullHealth();
    	}
    	
    }
}