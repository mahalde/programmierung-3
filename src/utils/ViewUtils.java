package utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;

/**
 * Contains utility functions for the visible simulator window
 */
public class ViewUtils {

    /**
     * Adds an Accelerator to the given menu item
     *
     * @param menuItem the given MenuItem
     * @param shortcut the shortcut which the given menuItem can be accessed with
     */
    public static void addAccelerator(MenuItem menuItem, String shortcut) {
        menuItem.setAccelerator(KeyCombination.valueOf(shortcut));
    }

    /**
     * Adds a tooltip to the given control
     *
     * @param control     the control
     * @param tooltipText the text of the tooltip
     */
    public static void addTooltip(Control control, String tooltipText) {
        Tooltip tooltip = new Tooltip(tooltipText);
        control.setTooltip(tooltip);
    }

    /**
     * Creates a new image view with the given path
     *
     * @param path the path to the image
     * @return a new instance of an image view
     */
    public static ImageView createImage(String path) {
        return new ImageView(new Image(Object.class.getResource(path).toExternalForm()));
    }

    /**
     * Shows an alert and waits for user interaction
     *
     * @param alertType   the type of the alert
     * @param title       the text which is shown in the title bar of the alert
     * @param headerText  the text which is shown in the header. Can be null to remove the header
     * @param contentText the text which is shown in the content of the alert
     */
    public static void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        alert.showAndWait();
    }

    /**
     * Runs a runnable in the main Java FX Application Thread
     *
     * @param runnable the given runnable
     */
    public static void runOnPlatform(Runnable runnable) {
        if (Platform.isFxApplicationThread()) {
            runnable.run();
        } else {
            Platform.runLater(runnable);
        }
    }

}
