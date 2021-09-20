package exceptions;

public class IllegalMoveException extends SimulatorException {

    public IllegalMoveException() {
        super("Das Flugzeug kann die Aktion nicht ausführen");
    }
}
