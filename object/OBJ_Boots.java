package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

import static main.GamePanel.tileSize;

public class OBJ_Boots extends SuperObject {
	private GamePanel gp;

	public OBJ_Boots(GamePanel gp) { 
		this.gp = gp;

		name = "Boots"; 
		try { 
			image = ImageIO.read(getClass().getResourceAsStream("/objects/boots.png"));
			uTool.scaleImage(image, tileSize, tileSize);
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
