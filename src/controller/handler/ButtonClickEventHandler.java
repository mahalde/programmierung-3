package controller.handler;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import model.Plane;
import model.Territory;
import model.exception.SimulatorException;
import utils.ViewUtils;

/**
 * Handles click events for the toolbar buttons which manouver the plane
 *
 * @param <T> the type of the event
 */
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
                ViewUtils.showAlert(Alert.AlertType.INFORMATION,
                        "Passagiere im Flugzeug",
                        null,
                        "Passagiere im Flugzeug: " + territory.getNumberOfPlanePassengers());
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
