package exceptions;

public class IllegalSizeException extends SimulatorException {

    public IllegalSizeException(int min, int max) {
        super(String.format("Die gegebene Größe darf minimal %s und maximal %s betragen", min, max));
    }
}
