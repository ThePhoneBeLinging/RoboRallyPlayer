package dk.dtu.compute.se.pisd.roborally.controller;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SoundController {
    private static SoundController instance;
    private Clip clip;
    private boolean isStopped = false; // new flag
    private Random random = new Random();
    private int currentSoundIndex= random.nextInt(5);
    private final BlockingQueue<URL> queue = new ArrayBlockingQueue<URL>(1);

    private SoundController() {
    }

    public static SoundController getInstance() {
        if (instance == null) {
            instance = new SoundController();
        }
        return instance;
    }

    public void playSound(String soundSrc) {
        try {
            // Open an audio input stream.
            URL url = getClass().getResource("/sounds/" + soundSrc + ".wav");
            if (url == null) {
                return;
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            // Get a sound clip resource.
            clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
            FloatControl fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            fc.setValue(-30.00f);
            clip.start();
            LineListener listener = new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if(event.getType() != LineEvent.Type.STOP) {
                        return;
                    }
                    try {
                        queue.take();
                    } catch (InterruptedException e) {

                    }
                }
            };
            clip.addLineListener(listener);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void stopSound() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            isStopped = true; // set the flag to true when stopping the sound
        }
    }

    public void loopSounds(String[] soundSrcs) {
        try {
            // Open an audio input stream.
            URL url = getClass().getResource("/sounds/" + soundSrcs[currentSoundIndex] + ".wav");
            if (url == null) {
                return;
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            // Get a sound clip resource.
            clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
            FloatControl fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            fc.setValue(-20.00f);
            clip.start();
            clip.addLineListener(e -> {
                if (!clip.isRunning() && !isStopped) { // check the flag before playing the next sound
                    int newIndex;
                    do {
                        newIndex = random.nextInt(soundSrcs.length);
                    }
                    while (newIndex == currentSoundIndex);
                    loopSounds(soundSrcs); // play the next sound
                }
            });

        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

}
