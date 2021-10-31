package controller.handler;

import controller.ProgramController;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;

/**
 * Event handler for the saving of the content of the textarea into a file.
 *
 * @param <T> the type of the event
 */
public class SaveProgramEventHandler<T extends Event> implements EventHandler<T> {

    private final TextArea textArea;

    public SaveProgramEventHandler(TextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void handle(T event) {
        String content = textArea.getText();

        ProgramController.saveProgram(content);
    }
}
