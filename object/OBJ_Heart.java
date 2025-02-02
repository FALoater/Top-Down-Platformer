package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import static main.GamePanel.tileSize;

public class OBJ_Heart extends SuperObject {
	
	public OBJ_Heart() { 
		name = "Heart"; 
		try { 
			// load imagees
			image = ImageIO.read(getClass().getResourceAsStream("/assets/objects/heart_full.png"));
			image2 = ImageIO.read(getClass().getResourceAsStream("/assets/objects/heart_half.png"));
			image3 = ImageIO.read(getClass().getResourceAsStream("/assets/objects/heart_blank.png"));
			image = uTool.scaleImage(image, tileSize, tileSize);
			image2 = uTool.scaleImage(image2, tileSize, tileSize);
			image3 = uTool.scaleImage(image3, tileSize, tileSize);
			
		}catch (IOException e) {
			// error if images do not exist
			e.printStackTrace();
		}
	}
}
