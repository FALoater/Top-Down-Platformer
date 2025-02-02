package main;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {

	private float volume;
	private int soundLevel;
	private Clip clip; // used to open sound files
	private URL music[] = new URL[30]; //URL used to store file path of sound files

	// constants to refer to specific sound files, similar to an enum
	public static final int MAIN_MENU = 0;
	public static final int LEVEL1 = 1;
	public static final int LEVEL2 = 2;
	public static final int LEVEL3 = 3;
	public static final int END_GAME = 4;

	public static final int PLAYER_HURT_1 = 5;
	public static final int PLAYER_HURT_2 = 6;
	public static final int PLAYER_DEATH = 7;
	public static final int PLAYER_ATTACK = 8;
	public static final int ENEMY_HURT = 9;
	public static final int ENEMY_DEATH = 10;

	public Sound(int soundLevel) { 
		// load sound files from assets 
		music[0] = getClass().getResource("/assets/sound/main_menu.wav");
		music[1] = getClass().getResource("/assets/sound/level_1.wav");
		music[2] = getClass().getResource("/assets/sound/level_2.wav");
		music[3] = getClass().getResource("/assets/sound/level_3.wav");
		music[4] = getClass().getResource("/assets/sound/end_game.wav");
		
		music[5] = getClass().getResource("/assets/sound/player_hurt_1.wav");
		music[6] = getClass().getResource("/assets/sound/player_hurt_2.wav");
		music[7] = getClass().getResource("/assets/sound/player_death.wav");
		music[8] = getClass().getResource("/assets/sound/player_attack.wav");
		music[9] = getClass().getResource("/assets/sound/enemy_hurt.wav");
		music[10] = getClass().getResource("/assets/sound/enemy_death.wav");

		this.soundLevel = soundLevel;
	}
	
	public void setFile(int i) { 
		try { 
			// format to open audio files in Java 
			AudioInputStream ais = AudioSystem.getAudioInputStream(music[i]);
			clip = AudioSystem.getClip();
			clip.open(ais);
		}catch (Exception e) { 
			System.out.println(e);	
		}
	}
	
	public void play() { 
		setSoundLevel(soundLevel);
		clip.start();
	}
	
	public void loop() { 
		setSoundLevel(soundLevel);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void stop() { 
		clip.stop();
	}

	private void updateSongVolume() {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN); // float control to change volume
        float range = gainControl.getMaximum() - gainControl.getMinimum(); // range of volume
        float gain = (range * volume) + gainControl.getMinimum(); // calculate current volume based on selected volume
        gainControl.setValue(gain); // apply changes
    }

	// getters and setters

	public void setVolume(float volume) {
		this.volume = volume;
		updateSongVolume();
	}

	public int getSoundLevel() {
		return soundLevel;
	}

	public void decreaseSound() {
		soundLevel--;
		if(soundLevel < 0) soundLevel = 0;
		setSoundLevel(soundLevel);
	}

	public void increaseSound() {
		soundLevel++;
		if(soundLevel > 10) soundLevel = 10;
		setSoundLevel(soundLevel);
	}

	public void setSoundLevel(int level) {
		// preset values as sound increase is exponential
		switch(level) {
		case 0:
			volume = 0;
			break;
		case 1:
			volume = 0.44f;
			break;
		case 2:	
			volume = 0.53f;
			break;
		case 3:
			volume = 0.58f;
			break;
		case 4:
			volume = 0.65f;
			break;
		case 5:
			volume = 0.68f;
			break;
		case 6:
			volume = 0.73f;	
			break;
		case 7:
			volume = 0.77f;
			break;
		case 8:
			volume = 0.80f;
			break;
		case 9:
			volume = 0.83f;
			break;
		case 10:
			volume = 0.86f;
			break;
		}
		updateSongVolume();
	}
}