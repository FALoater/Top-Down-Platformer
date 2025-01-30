package main;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
	
	private Clip clip; // used to open sound files
	private URL music[] = new URL[30]; //URL used to store file path of sound files

	public static final int LEVEL1 = 0;
	public static final int LEVEL2 = 1;

	public static final int PLAYER_HURT_1 = 2;
	public static final int PLAYER_HURT_2 = 3;
	public static final int PLAYER_DEATH = 4;
	public static final int ENEMY_HURT = 5;
	public static final int ENEMY_DEATH = 6;

	public Sound() { 
		music[0] = getClass().getResource("/assets/sound/level1.wav");
		music[1] = getClass().getResource("/assets/sound/level2.wav");
		
		music[2] = getClass().getResource("/assets/sound/player_hurt_1.wav");
		music[3] = getClass().getResource("/assets/sound/player_hurt_2.wav");
		music[4] = getClass().getResource("/assets/sound/player_death.wav");
		music[5] = getClass().getResource("/assets/sound/enemy_hurt.wav");
		music[6] = getClass().getResource("/assets/sound/enemy_death.wav");
	}
	
	public void setFile(int i) { 
		try { 
			//Format to open audio files in Java 
			AudioInputStream ais = AudioSystem.getAudioInputStream(music[i]);
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