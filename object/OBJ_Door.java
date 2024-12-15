package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

import static main.GamePanel.tileSize;

public class OBJ_Door extends SuperObject {
	private GamePanel gp;
	
	public OBJ_Door(GamePanel gp) { 
		this.gp = gp;
		
		name = "Door"; 
		try { 
			image = ImageIO.read(getClass().getResourceAsStream("/objects/door.png"));
			uTool.scaleImage(image, tileSize, tileSize);
		}catch (IOException e) {
			e.printStackTrace();
		}	
		collision = true; // we want to make the door solid 
	}
}
