package player;

import java.util.ArrayList;
import java.util.Random;

public class Playlist {
    private ArrayList<Song> playlist;
    private Random rand;

    public Playlist() {
        this.playlist = new ArrayList<>();
        this.rand = new Random();
    }

    public void addSongToPlaylist(Song song) {
        this.playlist.add(song);
    }

    public void removeSongFromPlaylist(int songIndex) {
        this.playlist.remove(songIndex);
    }

    public Song songFromIndex(int index) {
        return this.playlist.get(index);
    }

    public Song randomSong() {
        int playlistSize = this.playlist.size();
        int randomIndex = this.rand.nextInt(playlistSize);
        return this.playlist.get(randomIndex);
    }

    public void show(){
        int cnt = 0;
        for(Song s: this.playlist){
            System.out.println("Song "+ cnt + " from playlist");
            System.out.println("Title: "+s.getTitle());
            System.out.println("Artist: "+s.getArtist());
            System.out.println("Album: "+s.getAlbum());
            System.out.println("Length: "+s.getLength());
            System.out.println("\n");
            cnt++;
        }
    }


}
