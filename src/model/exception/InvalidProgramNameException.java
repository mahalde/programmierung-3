package model.exception;

public class InvalidProgramNameException extends Exception {

    public InvalidProgramNameException(String name) {
        super(String.format("Der gegebene Name '%s' ist kein valider Java-Bezeichner", name));
    }
}
