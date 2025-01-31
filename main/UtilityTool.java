package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class UtilityTool {
	
	// put any optimised functions here that are convenient and then you can call it in another class
	public BufferedImage scaleImage(BufferedImage original, int width, int height) { 
		BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
		// parameters for BufferedImage are width, height and imageType
		Graphics2D g2 = scaledImage.createGraphics(); 
		g2.drawImage(original, 0, 0, width, height, null);
		g2.dispose();
		
		return scaledImage;
	}
}
