package controller.simulation;

import controller.ExceptionObservable;
import model.Territory;

import java.util.Observable;

/**
 * Handles the management of a single simulation
 */
public class SimulationManager extends Observable {

    private Simulation simulation;
    private final Territory territory;
    private final SimulationState state;
    private final ExceptionObservable exceptionObservable;
    private int speed;

    public SimulationManager(Territory territory, ExceptionObservable exceptionObservable) {
        this.territory = territory;
        this.state = new SimulationState();
        this.exceptionObservable = exceptionObservable;
    }

    /** Max speed of the simulation measured in milliseconds */
    private static final int MAX_SPEED = 1100;

    public int getSpeed() {
        return MAX_SPEED - speed * 10;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public SimulationState getState() {
        return this.state;
    }

    public void setState(SimulationState.State state) {
        this.state.setSelected(state);
    }

    public void start() {
        if (this.simulation != null) return;

        this.simulation = new Simulation(this.territory, this, this.exceptionObservable);
        this.simulation.startSimulation();
        this.state.setSelected(SimulationState.State.STARTED);

        this.setChanged();
        this.notifyObservers(this.state);
    }

    public void resume() {
        this.simulation.resumeSimulation();
        this.state.setSelected(SimulationState.State.STARTED);

        this.setChanged();
        this.notifyObservers(this.state);
    }

    public void pause() {
        this.simulation.pauseSimulation();
        this.state.setSelected(SimulationState.State.PAUSED);

        this.setChanged();
        this.notifyObservers(this.state);
    }

    public void stop() {
        if (simulation != null) {
            this.simulation.stopSimulation();
        }
    }

    public void simulationEnded() {
        this.simulation = null;
        this.state.setSelected(SimulationState.State.STOPPED);

        this.setChanged();
        this.notifyObservers(this.state);
    }
}
