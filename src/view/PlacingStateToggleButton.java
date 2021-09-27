package view;

import controller.PlacingState;
import javafx.scene.control.ToggleButton;

import java.util.Observable;
import java.util.Observer;

public class PlacingStateToggleButton extends ToggleButton implements Observer {

    private final PlacingState.State state;

    public PlacingStateToggleButton(PlacingState.State state) {
        this.state = state;

        this.setUserData(state);
    }

    @Override
    public void update(Observable o, Object arg) {
        PlacingState state = (PlacingState) o;
        setSelected(this.state == state.getSelected());
    }
}
