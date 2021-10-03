package model.exception;

public class ProgramAlreadyOpenException extends Exception {

    public ProgramAlreadyOpenException(String name) {
        super(String.format("Das Programm mit dem Namen %s ist schon geöffnet", name));
    }
}
