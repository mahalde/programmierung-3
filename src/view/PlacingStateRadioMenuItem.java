package view;

import controller.PlacingState;
import javafx.scene.control.RadioMenuItem;

import java.util.Observable;
import java.util.Observer;

public class PlacingStateRadioMenuItem extends RadioMenuItem implements Observer {

    private final PlacingState.State state;

    public PlacingStateRadioMenuItem(String text, PlacingState.State state) {
        super(text);

        this.state = state;

        PlacingState.getState().addObserver(this);
        this.setUserData(state);
    }

    @Override
    public void update(Observable o, Object arg) {
        PlacingState state = (PlacingState) o;
        setSelected(this.state == state.getSelected());
    }
}
