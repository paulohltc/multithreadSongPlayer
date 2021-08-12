package player;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class Song {
    private String title; // Song file name e.g, "Everybody Falls (Fall Guys Theme)"
    private long length; // in seconds
    private AudioInputStream audioStream;


    public Song(File song) throws UnsupportedAudioFileException, IOException {
        this.audioStream = AudioSystem.getAudioInputStream(song);
        this.length = audioStream.getFrameLength() / (long) audioStream.getFormat().getFrameRate();
        this.title = song.getName().substring(0, song.getName().length() - 4); // remove .wav
    }

    // Getters


    public String getTitle() {
        return title;
    }

    public double getLength() {
        return length;
    }

    public AudioInputStream getAudioStream() {
        return audioStream;
    }
}
