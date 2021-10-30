package view;

import controller.simulation.SimulationState;
import controller.simulation.SimulationManager;
import javafx.scene.Node;
import javafx.scene.control.RadioMenuItem;

import java.util.Observable;
import java.util.Observer;

public class SimulatorStateRadioMenuItem extends RadioMenuItem implements Observer {

    private final SimulationState.State state;

    public SimulatorStateRadioMenuItem(String text, Node image, SimulationState.State state, SimulationManager manager) {
        super(text, image);

        this.state = state;

        manager.addObserver(this);
        this.setUserData(state);
    }

    @Override
    public void update(Observable o, Object arg) {
        SimulationManager manager = (SimulationManager) o;
        setSelected(this.state == manager.getState().getSelected());

        switch (manager.getState().getSelected()) {
            case STARTED:
                setDisable(this.state == SimulationState.State.STARTED);
                break;
            case PAUSED:
                setDisable(this.state == SimulationState.State.PAUSED);
                break;
            case STOPPED:
                setDisable(this.state != SimulationState.State.STARTED);
                break;
        }
    }
}
