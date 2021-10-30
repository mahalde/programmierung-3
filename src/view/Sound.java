package view;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sound {

    private static MediaPlayer deathSound;

    public static void death() {
        if (deathSound == null) {
            deathSound = new MediaPlayer(new Media(Sound.class.getResource("/resources/death.wav").toExternalForm()));
        }

        deathSound.play();
    }
}
