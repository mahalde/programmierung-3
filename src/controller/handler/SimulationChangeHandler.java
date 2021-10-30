package controller.handler;

import controller.simulation.SimulationState;
import controller.simulation.SimulationManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Toggle;

public class SimulationChangeHandler implements ChangeListener<Toggle> {

    private final SimulationManager simulationManager;

    public SimulationChangeHandler(SimulationManager manager) {
        this.simulationManager = manager;
    }

    public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
        SimulationState.State oldState = null;

        if (oldValue != null) {
            oldState = ((SimulationState.State) oldValue.getUserData());
        }

        if (newValue != null) {
            SimulationState.State state = ((SimulationState.State) newValue.getUserData());
            switch (state) {
                case STARTED:
                    if (oldState == SimulationState.State.PAUSED) {
                        this.simulationManager.resume();
                    } else {
                        this.simulationManager.start();
                    }
                    break;
                case PAUSED:
                    this.simulationManager.pause();
                    break;
                case STOPPED:
                    this.simulationManager.stop();
                    break;
            }
        }
    }
}
