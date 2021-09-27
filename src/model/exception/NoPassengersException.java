package model.exception;

public class NoPassengersException extends SimulatorException{


    public NoPassengersException(String message) {
        super(message);
    }

    public NoPassengersException(String message, int x, int y) {
        super(String.format(message, x, y));
    }
}
