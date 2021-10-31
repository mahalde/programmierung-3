package view;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * Contains methods for playing specific sounds.
 * Acts as an singleton with exclusively static fields and methods
 */
public class Sound {

    private static MediaPlayer deathSound;

    /**
     * Plays a death sound on a user generated error (e.g. plane flying into a thunderstorm)
     */
    public static void death() {
        // Cache the death sound so it doesn't have to be loaded again
        if (deathSound == null) {
            deathSound = new MediaPlayer(new Media(Sound.class.getResource("/resources/death.wav").toExternalForm()));
        }

        deathSound.play();
        // Resets the sound to the beginning
        deathSound.seek(Duration.ZERO);
    }
}
