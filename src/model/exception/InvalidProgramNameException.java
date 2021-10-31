package model.exception;

/**
 * Gets thrown when the given program name is not a valid Java identifier
 */
public class InvalidProgramNameException extends Exception {

    public InvalidProgramNameException(String name) {
        super(String.format("Der gegebene Name '%s' ist kein valider Java-Bezeichner.", name));
    }
}
