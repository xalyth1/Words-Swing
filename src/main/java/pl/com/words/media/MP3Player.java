package pl.com.words.media;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import java.io.InputStream;

public class MP3Player {
    private Player player;
    public MP3Player(InputStream mp3Stream) {
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
                player.play();
                close();
            }
            catch (Exception e) {
                System.out.println(e);
            }
        }).start();
    }
}
