package controller.handler;

import controller.ExceptionObservable;
import controller.ProgramController;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import model.Territory;
import model.exception.SimulatorException;
import view.TerritoryPane;

/**
 * Event handler for the mouse clicking and dragging on the territory
 */
public class TerritoryEventHandler implements EventHandler<MouseEvent> {

    private final Territory territory;
    private final TerritoryPane territoryPane;
    private final ExceptionObservable exceptionObservable;
    private boolean isDraggingPlane = false;
    /** Used for detecting if the plane has been dragged, so we don't place a passenger or thunderstorm while dragging the plane. */
    private boolean hasDragged = false;

    public TerritoryEventHandler(Territory territory, TerritoryPane territoryPane, ExceptionObservable exceptionObservable) {
        this.territory = territory;
        this.territoryPane = territoryPane;
        this.exceptionObservable = exceptionObservable;
    }

    @Override
    public void handle(MouseEvent event) {
        Territory.Tile tile = territoryPane.getTile(event.getX(), event.getY());

        // Right clicks get handled by the context menu handler instead.
        if (!event.getButton().equals(MouseButton.PRIMARY)) return;

        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            handleMousePressed(tile);
        } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            handleMouseDragged(tile);
        } else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
            handleMouseReleased(tile);
        }
    }

    private void handleMousePressed(Territory.Tile tile) {
        if (tile != null && territory.getPlaneX() == tile.getX() && territory.getPlaneY() == tile.getY()) {
            this.isDraggingPlane = true;
        }
    }

    private void handleMouseDragged(Territory.Tile tile) {
        if (this.isDraggingPlane && tile != null) {
            this.hasDragged = true;

            try {
                this.territory.setPlaneCoordinates(tile.getX(), tile.getY());
            } catch (SimulatorException e) {
                this.exceptionObservable.notifyAboutException(e);
            }
        }
    }

    private void handleMouseReleased(Territory.Tile tile) {
        this.isDraggingPlane = false;
        if (this.hasDragged) {
            this.hasDragged = false;
            return;
        }

        try {
            switch (ProgramController.getPlacingState().getSelected()) {
                case PLANE:
                    this.territory.setPlaneCoordinates(tile.getX(), tile.getY());
                    break;
                case PASSENGER:
                    int passengers = this.territory.getPassengers(tile.getX(), tile.getY());
                    this.territory.setPassenger(tile.getX(), tile.getY(), passengers > 0 ? passengers + 1 : 1);
                    break;
                case THUNDERSTORM:
                    this.territory.setThunderstorm(tile.getX(), tile.getY());
                    break;
                case DELETE:
                    this.territory.clearTile(tile.getX(), tile.getY());
                    break;
                default:
                    throw new SimulatorException("Should not be reached");
            }
        } catch (SimulatorException e) {
            this.exceptionObservable.notifyAboutException(e);
        }
    }
}
