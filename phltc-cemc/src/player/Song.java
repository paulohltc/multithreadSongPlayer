package player;

public class Song {
    private String title;
    private String artist;
    private String album;
    private double length;

    public Song(String title, String artist, String album, double length) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.length = length;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public double getLength() {
        return length;
    }
}
