package controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import model.Plane;
import model.exception.SimulatorException;

public class ButtonClickEventHandler<T extends Event> implements EventHandler<T> {

    public enum Action {
        PASSENGERS_IN_PLANE,
        LEFT,
        FORWARD,
        BOARD_ON,
        BOARD_OFF
    }

    private final Action action;
    private final Plane plane;

    public ButtonClickEventHandler(Action action, Plane plane) {
        this.action = action;
        this.plane = plane;
    }

    @Override
    public void handle(Event event) {
        switch (action) {
            case PASSENGERS_IN_PLANE:
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Passagiere im Flugzeug: " + plane.getNumberOfPassengers());

                alert.showAndWait();
                break;
            case LEFT:
                plane.linksUm();
                break;
            case FORWARD:
                plane.vor();
                break;
            case BOARD_ON:
                plane.onboarden();
                break;
            case BOARD_OFF:
                plane.offboarden();
                break;
            default:
                throw new SimulatorException("should not be reached");
        }
    }
}
