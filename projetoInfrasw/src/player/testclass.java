package player;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class testclass {
    public static void main(String[] args) throws LineUnavailableException, UnsupportedAudioFileException, IOException, InterruptedException {
        File path = new File("src/songs");
        String[] pathnames = path.list();
        System.out.println(-1%2);
        long[] frames = new long[3];
        int cnt = 0;
        for(String s: pathnames){
            Clip clip = AudioSystem.getClip();
            File song = new File(path,s);
            AudioInputStream audioSong = AudioSystem.getAudioInputStream(song);
            clip.open(audioSong);
            frames[cnt] = audioSong.getFrameLength();
            clip.start();
            Thread.sleep(5000);
            clip.setFramePosition((int)frames[cnt]-1);
            cnt++;
        }
    }
}
