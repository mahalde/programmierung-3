package controller;

import java.util.Observable;

public class ExceptionObservable extends Observable {

    public void notifyAboutException(Throwable e) {
        this.setChanged();
        this.notifyObservers(e);
    }
}
