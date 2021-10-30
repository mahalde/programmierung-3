package controller.handler;

import controller.simulation.SimulationManager;
import controller.simulation.SimulationState;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import model.Plane;
import model.Territory;
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
    private final Territory territory;

    public ButtonClickEventHandler(Action action, Territory territory) {
        this.action = action;
        this.territory = territory;
    }

    @Override
    public void handle(Event event) {
        Plane plane = territory.getPlane();
        switch (action) {
            case PASSENGERS_IN_PLANE:
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Passagiere im Flugzeug: " + territory.getNumberOfPlanePassengers());

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
