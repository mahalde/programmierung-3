package controller.handler;

import controller.ProgramController;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import model.exception.InvalidProgramNameException;
import model.exception.ProgramAlreadyOpenException;

import java.util.Optional;

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
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Programm bereits geöffnet");
                alert.setHeaderText(null);
                alert.setContentText("Das Programm ist bereits geöffnet");

                alert.showAndWait();
            } catch (InvalidProgramNameException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalider Name");
                alert.setHeaderText(null);
                alert.setContentText("Der gegebene Name ist kein valider Java-Bezeichner");

                alert.showAndWait();
            }
        });
    }
}
