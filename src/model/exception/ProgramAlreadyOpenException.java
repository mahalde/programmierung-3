package model.exception;

/**
 * Gets thrown when the program which the user wants to open is already opened
 */
public class ProgramAlreadyOpenException extends Exception {

    public ProgramAlreadyOpenException(String name) {
        super(String.format("Das Programm mit dem Namen %s ist schon geöffnet.", name));
    }
}
