package model.exception;

/**
 * Gets thrown when an illegal size is given when changing sizes of the territory
 */
public class IllegalSizeException extends SimulatorException {

    public IllegalSizeException(int min, int max) {
        super(String.format("Die gegebene Größe darf minimal %s und maximal %s betragen", min, max));
    }
}
