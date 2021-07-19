package player;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Scanner in = new Scanner(System.in);
        Player player = new Player();
        String command = "";

        while(!command.equals("QUIT")){
            command = in.nextLine();

            switch(command){
                case "ADD":
                    String title = in.nextLine();
                    String artist = in.nextLine();
                    String album = in.nextLine();
                    double length = in.nextDouble();
                    Song currSong = new Song(title,artist,album,length);
                    player.initAdd(currSong);
                    player.addThread.start();
                    break;
                case "REMOVE":
                    int songPos = in.nextInt();
                    player.initRemove(songPos);
                    player.removeThread.start();
                    break;
                case "SHOW":
                    player.getPlaylist().show();
                    break;
            }
        }
        player.addThread.join();
        player.removeThread.join();
        in.close();
    }
}