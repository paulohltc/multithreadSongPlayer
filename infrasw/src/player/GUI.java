package player;
//Atua

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class GUI extends JFrame {

    private JPanel MainJPANEL;
    private JPanel Playlist;
    private JList AddedMusicList;
    private JPanel Botões;
    private JButton backButton;
    private JButton playPauseButton;
    private JButton forwardButton;
    private JButton shuffleButton;
    private JButton AddSongButton;
    private JButton RemSongButton;
    private JList NonAddedMusicList;
    private JProgressBar progressBar1;
    DefaultListModel NonAddedMusicListModel, AddedMusicListModel;
    private boolean play = true;
    private boolean botaoRandom = false;
    public static JFrame frame;

    static {
        try {
            frame = new GUI();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ArrayList<Song> playlisttouseAdded = new ArrayList<>();
    ArrayList<Song> playlisttouseNonAdded = new ArrayList<>();

    //
    public static Player player;
    static {
        try {
            player = new Player();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    //Construtor
    public GUI() throws UnsupportedAudioFileException, IOException {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(MainJPANEL);
        this.pack();
        NonAddedMusicListModel = new DefaultListModel();
        AddedMusicListModel = new DefaultListModel();
        String musics[] = {"Cartoon - Why We Lose (feat. Coleman Trapp) [NCS Release]", "Everybody Falls (Fall Guys Theme)","Undertale Ost - 096 - Last Goodbye", "Void - Sudden Romance"};

        File file = new File("src/songs/");
        String[] pathnames;
        pathnames = file.list();
        for (String s : pathnames) {
            Song song = new Song(new File(file, s));
            playlisttouseAdded.add(song);
        }
        for (int i = 0; i < musics.length; i++) {
            NonAddedMusicListModel.addElement(musics[i]);
        }
        NonAddedMusicList.setModel(NonAddedMusicListModel);

        //ActionListeners
        AddSongButton.addActionListener(this::AddSongGButtonActionPerformed);
        RemSongButton.addActionListener(this::RemSongButtonActionPerformed);
        shuffleButton.addActionListener(this::ShuffleButtonActionPerformed);
        playPauseButton.addActionListener(e2 -> {
            try {
                PlayPauseButtonActionPerformed(e2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        backButton.addActionListener(e1 -> {
            try {
                PreviousButtonActionPerformed(e1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        forwardButton.addActionListener(e -> {
            try {
                ForwardButtonActionPerformed(e);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        });
    }

    //Remove button
    private void RemSongButtonActionPerformed(ActionEvent e) {
        String aux = AddedMusicList.getSelectedValue().toString();

        int y = 0;
        for (int x = 0; playlisttouseNonAdded.size() > x; x++) {
            if (aux.equals(playlisttouseNonAdded.get(x).getTitle())) {
                playlisttouseAdded.add(playlisttouseNonAdded.get(x));
                y = x;
                playlisttouseNonAdded.remove(x);
                break;
            }
        }
        //int f = AddedMusicList.getSelectedIndex();
        //int songPos = ((GUI) frame).getNonAddedMusicList().getSelectedIndex();
        player.initRemove(y);
        NonAddedMusicListModel.addElement(aux);
        NonAddedMusicList.setModel(NonAddedMusicListModel);
        if (AddedMusicListModel.getSize() != 0) {
            AddedMusicListModel.remove(y);
            JOptionPane.showMessageDialog(rootPane, "Musica removida com sucesso!");
        }
        AddedMusicList.setModel(AddedMusicListModel);
    }

    //Add button
    public void AddSongGButtonActionPerformed(ActionEvent e) {
        String aux = NonAddedMusicList.getSelectedValue().toString();
        Song y = null;
        for (int x = 0; playlisttouseAdded.size() > x; x++) {
            if (aux.equals(playlisttouseAdded.get(x).getTitle())) {
                playlisttouseNonAdded.add(playlisttouseAdded.get(x));
                y = playlisttouseAdded.get(x);
                playlisttouseAdded.remove(x);
                break;
            }
        }

        //int songPosFromSongFolder = ((GUI) frame).getNonAddedMusicList().getSelectedIndex();
        // Song selectedSong = player.getSongs().get(songPosFromSongFolder);
        player.initAdd(y);
        int f = NonAddedMusicList.getSelectedIndex();
        AddedMusicListModel.addElement(aux);
        AddedMusicList.setModel(AddedMusicListModel);
        if (NonAddedMusicListModel.getSize() != 0) {
            NonAddedMusicListModel.remove(f);
            JOptionPane.showMessageDialog(rootPane, "Musica adicionada à playlist.");
        }
        NonAddedMusicList.setModel(NonAddedMusicListModel);
    }

    //Play-Pause button
    public void PlayPauseButtonActionPerformed(ActionEvent e) throws InterruptedException {

        player.initPlayPause(play);
        play = !play;
    }

    //previous button
    public void PreviousButtonActionPerformed(ActionEvent e) throws InterruptedException {
        player.initPlayPause(false);
        player.initNextPrevSong(false,botaoRandom);
        player.initPlayPause(true);
        play = true;
        if (AddedMusicList.getSelectedIndex() == 0) {
            AddedMusicList.setSelectedIndex(AddedMusicList.getModel().getSize() - 1);
        } else {
            AddedMusicList.setSelectedIndex(AddedMusicList.getSelectedIndex() - 1);
        }
    }

    //forward button
    public void ForwardButtonActionPerformed(ActionEvent e) throws InterruptedException {
        player.initPlayPause(false);
        player.initNextPrevSong(true,botaoRandom);
        player.initPlayPause(true);
        play = true;
        if (AddedMusicList.getSelectedIndex() == AddedMusicList.getModel().getSize() - 1) {
            AddedMusicList.setSelectedIndex(0);
        } else {
            AddedMusicList.setSelectedIndex(AddedMusicList.getSelectedIndex() + 1);
        }
    }
    //Shuffle button
    public void ShuffleButtonActionPerformed(ActionEvent e){
        botaoRandom=!botaoRandom;
    }

    //Getter and setters
    public JList getNonAddedMusicList() {
        return NonAddedMusicList;
    }

    public static void main(String[] args) throws InterruptedException, UnsupportedAudioFileException, IOException, LineUnavailableException {
        Scanner in = new Scanner(System.in);
        frame.setVisible(true);
        String command = "";


    }
}
// for(var entry : player.getMapThreads().entrySet()){
//entry.getValue().join();
// }
//        player.getMapThreads().get("nextSongThread").join();
//        player.getMapThreads().get("prevSongThread").join();




