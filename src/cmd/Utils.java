package cmd;

import javafx.scene.control.Control;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;

/**
 * Contains utility functions for the cmd.FlightSimulator project
 */
public class Utils {

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

}
