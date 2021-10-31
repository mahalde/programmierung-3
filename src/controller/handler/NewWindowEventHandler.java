package controller.handler;

import controller.ProgramController;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import model.exception.InvalidProgramNameException;
import model.exception.ProgramAlreadyOpenException;
import utils.ViewUtils;

import java.util.Optional;

/**
 * Event handler for the opening of a new window.
 *
 * @param <T> the type of the event
 */
public class NewWindowEventHandler<T extends Event> implements EventHandler<T> {

    @Override
    public void handle(T event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Neues Programm öffnen");
        dialog.setHeaderText("Geben Sie den Namen des neuen Programms ein");
        dialog.setContentText("Name des neuen Programms:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(name -> {
            try {
                ProgramController.createNewProgram(name);
            } catch (ProgramAlreadyOpenException e) {
                ViewUtils.showAlert(Alert.AlertType.ERROR,
                        "Programm bereits geöffnet",
                        null,
                        e.getMessage());
            } catch (InvalidProgramNameException e) {
                ViewUtils.showAlert(Alert.AlertType.ERROR,
                        "Invalider Name",
                        null,
                        e.getMessage());
            }
        });
    }
}
