package model.exception;

public class OutOfBoundsException extends SimulatorException {

    public OutOfBoundsException(int x, int y) {
        super(String.format("Die gegebenen Koordinaten (%s, %s) liegen nicht im Territorium", x, y));
    }
}
