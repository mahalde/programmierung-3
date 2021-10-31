package controller;

import java.util.Observable;

/**
 * Observable which notifies observers about occurring SimulatorExceptions
 */
public class ExceptionObservable extends Observable {

    /**
     * Notifies the observers about an occurred exception
     *
     * @param e the occurred exception
     */
    public void notifyAboutException(Throwable e) {
        this.setChanged();
        this.notifyObservers(e);
    }
}
