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
	//Here's a detailed explanation:

//Preprocessing the Scaling:
//When you scale an image, tile, or object outside of the game loop (essentially preprocessing it), you do the computationally expensive task once. This means you are not repeating the scaling operation every frame during the game loop. Instead, you store the scaled version of the image.
//
//Reduced Computation per Frame:
//The draw method within the game loop is executed once per frame, so any operation performed within this loop needs to be optimized for speed. Scaling images in each frame can be computationally expensive because it involves matrix transformations, which can significantly slow down the rendering process, especially if there are many objects to draw.
//
//Memory vs. Speed Trade-off:
//By scaling images outside the draw method, you trade off memory for speed. The scaled images take up more memory, but this allows the drawing operation to be a simple blit (copy) operation, which is much faster than scaling. This trade-off is usually beneficial for performance, as memory access and copying are generally much faster than performing scaling operations repeatedly.
}
