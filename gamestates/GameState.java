package gamestates;

import java.awt.Graphics2D;

public interface GameState {
    // premakes methods for game state
    public void update();
    public void draw(Graphics2D g);
}
