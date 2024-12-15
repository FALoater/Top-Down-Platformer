package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

import static main.GamePanel.tileSize;

public class OBJ_Chest extends SuperObject {
	private GamePanel gp;
	
	public OBJ_Chest(GamePanel gp) { 
		this.gp = gp;
		
		name = "Chest"; 
		try { 
			image = ImageIO.read(getClass().getResourceAsStream("/objects/chest.png"));
			uTool.scaleImage(image, tileSize, tileSize);
		}catch (IOException e) {
			e.printStackTrace();
		}	
	}
}
