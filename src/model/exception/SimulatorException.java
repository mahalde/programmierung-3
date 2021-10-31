package model.exception;

/**
 * Base class for all more defined simulator exceptions
 */
public class SimulatorException extends RuntimeException {

    public SimulatorException(String message) {
        super(message);
    }
}
