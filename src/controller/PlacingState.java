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

    private static PlacingState state;

    private PlacingState() {
        this.setSelected(State.PLANE);
    }

    public static PlacingState getState() {
        if (state == null) state = new PlacingState();

        return state;
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
