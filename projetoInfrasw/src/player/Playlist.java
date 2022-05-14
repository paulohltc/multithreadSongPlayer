package player;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Playlist {

    private ArrayList<Song> playlist;
    private int currSongIndex;
    private int playlistSize;
    private boolean songStarted; // To avoid playing a song in an
    private Clip clip;
    private Random rand;

    public Playlist() throws LineUnavailableException {
        this.playlist = new ArrayList<>();
        this.currSongIndex = 0;
        this.playlistSize = 0;
        this.songStarted = false;
        this.rand = new Random();
    }


    // Getters
    public ArrayList<Song> getPlaylist() {
        return this.playlist;
    }

    public int getCurrSongIndex() {
        return this.currSongIndex;
    }

    public int getPlaylistSize() {
        return playlistSize;
    }

    public boolean isSongStarted() {
        return this.songStarted;
    }


    public Clip getClip() {
        return this.clip;
    }


    // Setters
    public void setCurrSongIndex(int currSongIndex) {
        this.currSongIndex = currSongIndex;
    }

    public void setSongStarted(boolean songStarted) {
        this.songStarted = songStarted;
    }


    public void setClip(Clip clip) {
        this.clip = clip;
    }


    // Methods
    public Song getSongFromIndex(int index) {
        return this.playlist.get(index);
    }

    public Song getCurrSong() {
        return this.getSongFromIndex(this.getCurrSongIndex());
    }

    public void endClip() {
        this.clip.start();
        this.clip.setFramePosition(Math.abs(this.clip.getFrameLength() - 1));
        this.clip.close();
    }

    public void addSongToPlaylist(Song song) {
        this.playlist.add(song);
        this.playlistSize++;
    }

    public void removeSongFromPlaylist(int songIndex) {
        this.playlist.remove(songIndex);
        this.playlistSize--;
    }

    public void loadCurrSongToClip() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        setClip(AudioSystem.getClip()); // reset cliP
        AudioInputStream currSongAudioStream = AudioSystem.getAudioInputStream(getCurrSong().getSongFile());
        getClip().open(currSongAudioStream);
    }


    public void show() { // gui related
        int cnt = 0;
        System.out.println(getCurrSongIndex());
        for (Song s : this.playlist) {
            System.out.println("Song " + cnt + " from playlist");
            System.out.println("Title: " + s.getTitle());
            System.out.println("\n");
            cnt++;
        }
    }


    // Threads

    class PlayPauseSongThread extends Thread {

        private boolean play; // false: pause, true: play

        public PlayPauseSongThread(boolean play) {
            this.play = play;
        }

        public void run() {

            if (!Playlist.this.playlist.isEmpty()) {
                try {
                    if (!Playlist.this.isSongStarted()) {
                        Playlist.this.setSongStarted(true);
                        Playlist.this.loadCurrSongToClip();
                    }
                    if (play) Playlist.this.getClip().start();
                    else Playlist.this.getClip().stop();
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (UnsupportedAudioFileException e) {
                    e.printStackTrace();
                }
            }

        }

    }


    class NextPrevSongThread extends Thread {

        private boolean next;// false: prev , true: prev

        public NextPrevSongThread(boolean next) {
            this.next = next;
        }

        public void run() {
            if (!Playlist.this.playlist.isEmpty()) {

                int newIndex = 0;
                int size = Playlist.this.getPlaylistSize();
                if (next) {
                    Playlist.this.endClip();
                    newIndex = (Playlist.this.getCurrSongIndex() + 1) % size;
                    Playlist.this.setCurrSongIndex(newIndex);
                    Playlist.this.setSongStarted(false);
                } else {
                    Playlist.this.endClip();
                    newIndex = (Playlist.this.getCurrSongIndex() - 1 + size) % size; // +size to avoid negative modulus
                    Playlist.this.setCurrSongIndex(newIndex);
                    Playlist.this.setSongStarted(false);
                }

            }
        }
    }

    //    // Threads getters
//
    public Thread getPlayPauseThread(boolean play) {
        return new PlayPauseSongThread(play);
    }

    public Thread getNextPrevSongThread(boolean next) {
        return new NextPrevSongThread(next);
    }


//    public void randomSong() {
//        int playlistSize = this.playlist.size();
//        int randomIndex = this.rand.nextInt(playlistSize);
//        this.setCurrSongIndex(randomIndex);
//    }


}