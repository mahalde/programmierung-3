package controller.handler;

import controller.ProgramController;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import model.exception.InvalidProgramNameException;
import model.exception.ProgramAlreadyOpenException;
import utils.FileUtils;
import utils.ViewUtils;

import java.io.File;

public class OpenFileEventHandler<T extends Event> implements EventHandler<T> {

    @Override
    public void handle(T event) {
        Window window = ProgramController.getFocusedProgram().getStage();
        FileChooser fileChooser = new FileChooser();

        File file = fileChooser.showOpenDialog(window);

        if (file == null) {
            return;
        }

        String name = FileUtils.getJavaProgramName(file);

        try {
            ProgramController.createNewProgram(name);
        } catch (ProgramAlreadyOpenException e) {
            ViewUtils.showAlert(Alert.AlertType.ERROR,
                    "Programm bereits geöffnet",
                    null,
                    "Das Programm ist bereits geöffnet");
        } catch (InvalidProgramNameException e) {
            ViewUtils.showAlert(Alert.AlertType.ERROR,
                    "Fehlerhafter Name",
                    null,
                    "Der gegebene Dateienname ist kein valider Java-Bezeichner oder vom falschen Typ");
        }
    }
}