package player;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Map;
import java.util.HashMap;


public class Player {
    private ArrayList<Song> songs;
    private Playlist playlist;
    private Clip clip;
    private ReentrantLock lock;
    private boolean busy;
    private Map<String, Condition> mapConditions = new HashMap<>();
    private Map<String, Thread> mapThreads = new HashMap<>();


    // Getters
    public ArrayList<Song> getSongs() {
        return songs;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public Map<String, Thread> getMapThreads() {
        return mapThreads;
    }

    // Creating song objects from songs folder
    public ArrayList<Song> loadSongs() throws UnsupportedAudioFileException, IOException {
        ArrayList<Song> songs = new ArrayList<>();
        File file = new File("src/player/songs/");
        String[] pathnames;
        pathnames = file.list();
        for (String s : pathnames) {
            Song song = new Song(new File(file, s));
            songs.add(song);
        }
        return songs;
    }

    public Player() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.songs = this.loadSongs();
        this.playlist = new Playlist();
        this.clip = AudioSystem.getClip();
        this.lock = new ReentrantLock();
        this.busy = false;

        this.mapConditions.put("addRemoveCondition", this.lock.newCondition());

        this.mapThreads.put("addThread", new Thread());
        this.mapThreads.put("removeThread", new Thread());

    }

    // Threads classes
    class AddSongToPlaylistThread extends Thread {
        private final Song song;

        public AddSongToPlaylistThread(Song song) {
            this.song = song;
        }

        public void run() {
            try {
                Player.this.lock.lock();

                while (Player.this.busy) {
                    Player.this.mapConditions.get("addRemoveCondition").await(); // playlist esta sendo modificada, threads em espera
                }

                Player.this.busy = true; // comecando modificacao
                Player.this.playlist.addSongToPlaylist(song);
                Player.this.busy = false; // terminando modificacao
                Player.this.mapConditions.get("addRemoveCondition").signalAll(); // liberando threads em espera

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                Player.this.lock.unlock();
            }
        }
    }

    class RemoveSongFromPlaylistThread extends Thread { // Utilizaremos o indice da musica na playlist para remover
        private final int songIndex;

        public RemoveSongFromPlaylistThread(int songIndex) {
            this.songIndex = songIndex;
        }

        public void run() {
            try {
                Player.this.lock.lock();

                while (Player.this.busy) {
                    Player.this.mapConditions.get("addRemoveCondition").await(); // playlist esta sendo modificada, threads em espera
                }

                Player.this.busy = true; // comecando modificacao
                Player.this.playlist.removeSongFromPlaylist(songIndex);
                Player.this.busy = false; // terminando modificacao
                Player.this.mapConditions.get("addRemoveCondition").signalAll(); // liberando threads em espera

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                Player.this.lock.unlock();
            }
        }


    }





    public void initAdd(Song song) {
        Thread t = new Thread(new AddSongToPlaylistThread(song));
        this.mapThreads.put("addThread", t);
        this.mapThreads.get("addThread").start();
    }

    public void initRemove(int index) {
        Thread t = new Thread(new RemoveSongFromPlaylistThread(index));
        this.mapThreads.put("removeThread", t);
        this.mapThreads.get("removeThread").start();
    }

    public void initPlayPause(boolean play) throws InterruptedException {
        Thread t = this.getPlaylist().getPlayPauseThread(play);
        this.mapThreads.put("playPauseThread", t);
        this.mapThreads.get("playPauseThread").start();
    }



//    public void initNextSong() throws InterruptedException {
//        Thread t = this.getPlaylist().getNextSongThread();
//        this.mapThreads.put("nextSongThread", t);
//        this.mapThreads.get("nextSongThread").start();
//        if (this.mapThreads.get("playThread") != null) {
//            this.mapThreads.get("playThread").join();
//        }
//        initPlay();
//    }
//
//    public void initPrevSong() throws InterruptedException {
//        Thread t = this.getPlaylist().getPrevSongThread();
//        this.mapThreads.put("prevSongThread", t);
//        this.mapThreads.get("prevSongThread").start();
//        if (this.mapThreads.get("playThread") != null) {
//            this.mapThreads.get("playThread").join();
//        }
//        initPlay();
//    }
}
