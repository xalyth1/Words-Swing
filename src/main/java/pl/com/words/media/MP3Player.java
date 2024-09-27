package pl.com.words.media;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

public class MP3Player {
    private Player player;
    private InputStream mp3Stream;
    public MP3Player(InputStream mp3Stream) {
        this.mp3Stream = mp3Stream;
        try {
            this.player = new Player(mp3Stream);
        } catch (JavaLayerException e) {
            System.out.println("Error playing mp3 in MP3Player constructor");
        }
    }

    public void close() { if (player != null) player.close(); }

    public void play() {
        new Thread(() -> {
            try {
                //Thread.sleep(2000);
                player.play();
                close();
            }
            catch (Exception e) { System.out.println(e); }
        }).start();
    }

    public static void play(String headword) {
//        String path = new StringBuilder().append(System.getProperty("user.dir"))
//                .append("\\src\\main\\resources\\Data\\Pronunciation\\")
//                .append(headword)
//                .append(".mp3")
//                .toString();
//        new MP3Player(path).play();
    }

}
