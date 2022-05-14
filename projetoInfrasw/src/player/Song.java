package player;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class Song {
    private String title; // Song file name e.g, "Everybody Falls (Fall Guys Theme)"
    private File songFile;


    public Song(File song) throws UnsupportedAudioFileException, IOException {
        this.songFile = song;
        this.title = song.getName().substring(0, song.getName().length() - 4); // remove .wav
    }

    // Getters

    public String getTitle() {
        return title;
    }

    public File getSongFile() {
        return this.songFile;
    }
}