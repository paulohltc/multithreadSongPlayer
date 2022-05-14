package player;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws InterruptedException, UnsupportedAudioFileException, IOException, LineUnavailableException {
        Scanner in = new Scanner(System.in);
        Player player = new Player();
        String command = "";
        boolean play = true;

        while(!command.equals("QUIT")){
            System.out.println("\n\n\n 1: ADD \n 2: REMOVE \n 3: PLAY/PAUSE \n 4: PREV \n 5: NEXT \n 6: SHOW");
            command = in.nextLine();

            switch(command){
                case "1":
                    int songPosFromSongFolder = in.nextInt();
                    Song selectedSong = player.getSongs().get(songPosFromSongFolder);
                    player.initAdd(selectedSong);
                    break;
                case "2":
                    int songPos = in.nextInt();
                    player.initRemove(songPos);
                    break;
                case "3":
                    player.initPlayPause(play);
                    play = !play;
                    break;
                case "4":
                    player.initPlayPause(false);
                    player.initNextPrevSong(false);
                    player.initPlayPause(true);
                    play = true;
                    break;
                case "5":
                    player.initPlayPause(false);
                    player.initNextPrevSong(true);
                    player.initPlayPause(true);
                    play = true;
                    break;
                case "6":
                    player.getPlaylist().show();
                    break;
            }
        }
        for(var entry : player.getMapThreads().entrySet()){
            entry.getValue().join();
        }
//        player.getMapThreads().get("nextSongThread").join();
//        player.getMapThreads().get("prevSongThread").join();
        in.close();
    }
}