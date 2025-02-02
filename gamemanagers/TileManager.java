package gamemanagers;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;
import tile.Tile;

import static main.GamePanel.tileSize;
import static main.GamePanel.maxWorldCol;
import static main.GamePanel.maxWorldRow;

public class TileManager {
	
	private GamePanel gp; 
	private Tile[] tile;
	private int mapTileNum[][] = new int[maxWorldCol][maxWorldRow];
	
	public TileManager(GamePanel gp) { 
		this.gp = gp;
		this.tile = new Tile[6]; // only need 6 types of tiles
		getTileImage();
	}
	
	public static BufferedImage readImage(String imageName){
		// load image from asset files
		String fileName = imageName + ".png";
		InputStream is = TileManager.class.getResourceAsStream("/assets/tiles/" + fileName);
		BufferedImage img = null;

		try { 
			img = ImageIO.read(is);
		}catch(IOException e) {
			System.out.println(e);
		}

		return img;
	}

    public void loadLevel(int level) { 
	    try { 
	        InputStream is = getClass().getResourceAsStream("/assets/maps/world0" + String.valueOf(level) + ".txt"); // importStream used to import text file 
	        BufferedReader br = new BufferedReader(new InputStreamReader(is));  // bufferedReader reads the contents of the text file (just a format) 
	        
	        int col = 0;
	        int row = 0; 
	        
	        while(row < maxWorldRow && col < maxWorldCol) {
	            String line = br.readLine(); // reads a line of the text file (a single line at a time) 
	            if (line == null) {
	                break; // EOF reached
	            }
	            String numbers[] = line.split(" "); // creates an array and splits the components every time there is a space
	            
	            while (col < maxWorldCol) {
	                int num = Integer.parseInt(numbers[col]); // changing to integer
	                mapTileNum[col][row] = num;
	                col++;
	            }
	            
	            col = 0; 
	            row++;
	        }
	        br.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
    }

	private void initaliseTile(int index, String imgName, boolean collision) {
		UtilityTool uTool = new UtilityTool();
		this.tile[index] = new Tile();
		this.tile[index].setImage(readImage(imgName));
		this.tile[index].setImage(uTool.scaleImage(tile[index].getImage(), tileSize, tileSize));
		this.tile[index].setCollision(collision);
	}
	
	public void draw(Graphics2D g2) {
		
		// we need to automate the tile drawing process to avoid a lot of typing 
		
		int worldCol = 0;
		int worldRow = 0; 
		
		while(worldCol< maxWorldCol && worldRow < maxWorldRow) { 
			
			int tileNum = mapTileNum[worldCol][worldRow];
			int worldX = worldCol * tileSize; 
			int worldY = worldRow * tileSize; 
			int screenX = worldX - gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX(); 
			int screenY = worldY - gp.getPlayer().getWorldY()  + gp.getPlayer().getScreenY();
			
			if (worldX + tileSize> gp.getPlayer().getWorldX() - gp.getPlayer().getScreenX() &&
				worldX -tileSize < gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX() && 
				worldY + tileSize> gp.getPlayer().getWorldY()  - gp.getPlayer().getScreenY() && 
				worldY - tileSize < gp.getPlayer().getWorldY()  + gp.getPlayer().getScreenY()) { 
				
				// previously, image had to be scaled during the game loop (not anymore, we have already pre-scaled it)
				g2.drawImage(tile[tileNum].getImage(), screenX, screenY, null);
			}
			worldCol++; 
		
			if (worldCol == maxWorldCol) {
				worldCol = 0; 
				worldRow++;
			}
		}
	}

	// getters and setters

	public void getLevel(int level) {
		loadLevel(level);
	}
	
	public void getTileImage() { 
		//load Tile images in this method 
			initaliseTile(0, "grass", false);
			initaliseTile(1, "wall", true);
			initaliseTile(2, "water", true);
			initaliseTile(3, "earth", false);
			initaliseTile(4, "tree", true);
			initaliseTile(5, "sand", false);
	}

	public int[][] getMapTileNum() {
		return mapTileNum;
	}

	public int getMapTileNum(int col, int row) {
		return mapTileNum[col][row];
	}

	public Tile[] getTile() {
		return tile;
	}

	public Tile getTile(int index) {
		return tile[index];
	}
}
