package main;
import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		JFrame window = new JFrame(); //create an instance of a JFrame
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // able to close the window
		window.setResizable(false); //cannot resize window
		window.setTitle("2D Adventure"); //set title to window 
		
		GamePanel gamePanel = new GamePanel(); 
		window.add(gamePanel);
		
		window.pack(); // causes window to be sized to fit the preferred size and layouts of its subcomponents
		
		window.setLocationRelativeTo(null); //window will be displayed at the centre of screen
		window.setVisible(true); //needed to see the window
		
		//set up game before you run game thread so the objects are in the correct positions 
		gamePanel.setupGame();
		gamePanel.startGameThread();
	}

}
