package controller.simulation;

/**
 * The state of a simulation, which can be started, stopped, or paused
 */
public class SimulationState {

    public enum State {
        STARTED,
        PAUSED,
        STOPPED
    }

    private State selected;

    public SimulationState() {
        this.setSelected(State.STOPPED);
    }

    public void setSelected(State state) {
        this.selected = state;
    }

    public State getSelected() {
        return this.selected;
    }
}
