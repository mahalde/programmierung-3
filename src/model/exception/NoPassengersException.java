package model.exception;

/**
 * Gets thrown when the plane has no passengers on board and tries to offboard or no passengers are on the field and it tries to onboard
 */
public class NoPassengersException extends SimulatorException{

    public NoPassengersException(String message) {
        super(message);
    }

    public NoPassengersException(String message, int x, int y) {
        super(String.format(message, x, y));
    }
}
