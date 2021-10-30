package controller;

import java.util.Observable;

public class PlacingState extends Observable {

    public enum State {
        PLANE,
        PASSENGER,
        THUNDERSTORM,
        DELETE
    }

    private State selected;

    public PlacingState() {
        this.setSelected(State.PLANE);
    }

    public void setSelected(State state) {
        this.selected = state;

        this.setChanged();
        this.notifyObservers();
    }

    public State getSelected() {
        return this.selected;
    }
}
