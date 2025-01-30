package main;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
	
	private Clip clip; // used to open sound files
	private URL soundURL[] = new URL[30]; //URL used to store file path of sound files

	public Sound() { 
		soundURL[0] = getClass().getResource("/assets/sound/BlueBoyAdventure.wav");
		soundURL[1] = getClass().getResource("/assets/sound/fanfare.wav");
		soundURL[2] = getClass().getResource("/assets/sound/powerup.wav");
		soundURL[3] = getClass().getResource("/assets/sound/unlock.wav");
		soundURL[4] = getClass().getResource("/assets/sound/coin.wav");
	}
	
	public void setFile(int i) { 
		try { 
			//Format to open audio files in Java 
			AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
			clip = AudioSystem.getClip();
			clip.open(ais);
		}catch (Exception e) { 
			System.out.println(e);	
		}
	}
	
	public void play() { 
		clip.start();
	}
	
	public void loop() { 
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void stop() { 
		clip.stop();
	}
}