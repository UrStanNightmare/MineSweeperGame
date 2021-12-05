package ru.ateam.minesweeper.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ateam.minesweeper.listeners.ModelAudioListener;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SimpleMusicPlayer implements AutoCloseable, ModelAudioListener {
    private final static Logger log = LoggerFactory.getLogger(SimpleMusicPlayer.class.getName());

    private boolean released;
    private AudioInputStream stream = null;
    private Clip clip = null;
    private FloatControl volumeControl = null;
    private boolean playing = false;
    private boolean autoplay;
    private volatile boolean forceStopped;
    private final String name;

    public SimpleMusicPlayer(String name, Boolean autoplay) {
        this.name = name;
        File file = new File(ClassLoader.getSystemResource(name).getPath());
        this.autoplay = autoplay;
        try {
            stream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(stream);
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    playing = false;
                    synchronized (clip) {
                        clip.notify();
                    }
                    if (autoplay && !forceStopped) {
                        play(true);
                    }
                }
            });
            released = true;
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            log.error("Couldn't load a sound file. {}", e.getMessage(), e);
            released = false;
            close();
        }
    }

    //Если успешно загрузился
    public boolean isReleased() {
        return released;
    }

    public boolean isPlaying() {
        return playing;
    }

    @Override
    public void play(boolean breakOld) {
        if (released) {
            forceStopped = false;
            if (breakOld) {
                clip.stop();
                clip.setFramePosition(0);
                clip.start();
                playing = true;
            } else if (!isPlaying()) {
                clip.setFramePosition(0);
                clip.start();
                playing = true;
            }
        }
    }

    public void stop() {
        if (playing) {
            clip.stop();
            forceStopped = true;
            playing = false;
        }
        log.info("Music stopped {}", this.name);
    }

    public void join() {
        if (!released) return;
        synchronized (clip) {
            try {
                while (playing) {
                    clip.wait();
                }
            } catch (InterruptedException ignored) {
            }
        }
    }

    @Override
    public void close() {
        if (clip != null) {
            clip.close();
        }
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                log.error("Couldn't close the audio stream. {}", e.getMessage(), e);
            }
        }
        log.info("Audio input stream closed. {}", this.name);
    }

    public void stopAndClose() {
        this.stop();
        this.close();
    }

    public synchronized void switchPlayingState() {
        if (forceStopped) {
            this.play(true);
        } else {
            this.stop();
        }
    }
}
