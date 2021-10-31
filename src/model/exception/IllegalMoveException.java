package model.exception;

/**
 * Gets thrown on an illegal move by the plane
 */
public class IllegalMoveException extends SimulatorException {

    public IllegalMoveException() {
        super("Das Flugzeug kann die Aktion nicht ausführen");
    }
}
