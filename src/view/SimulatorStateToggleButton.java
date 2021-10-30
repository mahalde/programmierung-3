package view;

import controller.simulation.SimulationState;
import controller.simulation.SimulationManager;
import javafx.scene.control.ToggleButton;

import java.util.Observable;
import java.util.Observer;

public class SimulatorStateToggleButton extends ToggleButton implements Observer {

    private final SimulationState.State state;

    public SimulatorStateToggleButton(SimulationState.State state) {
        this.state = state;

        this.setUserData(state);
    }

    @Override
    public void update(Observable o, Object arg) {
        SimulationManager manager = (SimulationManager) o;
        setSelected(this.state == manager.getState().getSelected());

        switch (manager.getState().getSelected()) {
            case STARTED:
                setDisabled(this.state == SimulationState.State.STARTED);
                break;
            case PAUSED:
                setDisabled(this.state == SimulationState.State.PAUSED);
                break;
            case STOPPED:
                setDisabled(this.state != SimulationState.State.STARTED);
                break;
        }
    }
}
