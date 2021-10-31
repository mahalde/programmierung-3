package model.exception;

/**
 * Gets thrown when a click somehow is outside of the territory bounds
 */
public class OutOfBoundsException extends SimulatorException {

    public OutOfBoundsException(int x, int y) {
        super(String.format("Die gegebenen Koordinaten (%s, %s) liegen nicht im Territorium", x, y));
    }
}
