package tile;

import java.awt.image.BufferedImage;

public class Tile {

	//this is a class that represents a single tile 
	private boolean collision = false; 
	private BufferedImage image; 

	public boolean isCollision() {
		return collision;
	}

	public void setCollision(boolean collision) {
		this.collision = collision;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

}
