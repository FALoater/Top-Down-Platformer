package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

import static main.GamePanel.tileSize;

public class OBJ_Key extends SuperObject {
	
	private GamePanel gp;

	public OBJ_Key(GamePanel gp) { 
		this.gp = gp;
		
		name = "Key"; 
		try { 
			image = ImageIO.read(getClass().getResourceAsStream("/objects/key.png"));
			uTool.scaleImage(image, tileSize, tileSize);
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

}
