package controller.handler;

import controller.simulation.SimulationManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class SpeedChangeHandler implements ChangeListener<Number> {

    public static final int INITIAL_SPEED = 50;

    private final SimulationManager simulationManager;

    public SpeedChangeHandler(SimulationManager simulationManager) {
        this.simulationManager = simulationManager;
        this.simulationManager.setSpeed(INITIAL_SPEED);
    }

    @Override
    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        this.simulationManager.setSpeed(newValue.intValue());
    }
}
