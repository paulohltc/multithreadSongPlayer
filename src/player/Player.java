package player;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Player {
    private Playlist playlist;
    private int currentSongPos;
    private ReentrantLock lock;
    private boolean busy;
    private Condition condition;
    Thread addThread;
    Thread removeThread;

    public Playlist getPlaylist() {
        return playlist;
    }

    public Player() {
        this.playlist = new Playlist();
        this.currentSongPos = 0;
        this.lock = new ReentrantLock();
        this.busy = false;
        this.condition = lock.newCondition();
    }

    class addSongThread extends Thread {
        private final Song song;

        public addSongThread(Song song) {
            this.song = song;
        }

        public void run() {
            try {
                lock.lock();

                while (busy) {
                    condition.await();
                }

                busy = true;
                playlist.addSongToPlaylist(song);
                busy = false;
                condition.signalAll();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    class removeSongThread extends Thread {
        private final int  songIndex;

        public removeSongThread(int songIndex) {
            this.songIndex = songIndex;
        }

        public void run() {
            try {
                lock.lock();

                while (busy) {
                    condition.await();
                }

                busy = true;
                playlist.removeSongFromPlaylist(songIndex);
                busy = false;
                condition.signalAll();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }


    }
    public void initAdd(Song song) {
        this.addThread = new Thread(new addSongThread(song));

    }

    public void initRemove(int index) {
        this.removeThread = new Thread(new removeSongThread(index));
    }
}
