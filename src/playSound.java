import java.io.*;
import javax.sound.sampled.*;
   
//note: code adapted from http://www.ntu.edu.sg/home/ehchua/programming/java/J8c_PlayingSound.html

public enum playSound {
   WALL("wallhit.wav"),
   BRICK("brickhit.wav"),
   DEATH("deathhit.wav");
   
   private Clip clip;
   
   playSound(String soundFileName) {
      try {
    	  File soundFile = new File(soundFileName);
    	  AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
         clip = AudioSystem.getClip();
         clip.open(audioIn);
      } catch (UnsupportedAudioFileException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } catch (LineUnavailableException e) {
         e.printStackTrace();
      }
   }
   
   public void play() {
         if (clip.isRunning())
            clip.stop();   // Stop the player if it is still running
         clip.setFramePosition(0); // rewind to the beginning
         clip.start();     // Start playing
   }
  
}