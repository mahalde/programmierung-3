package controller;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import model.Plane;
import model.Territory;
import model.exception.SimulatorException;
import view.TerritoryPane;

public class TerritoryEventHandler implements EventHandler<MouseEvent> {

    private final Territory territory;
    private final TerritoryPane territoryPane;

    private boolean isDraggingPlane = false;
    private boolean hasDragged = false;

    public TerritoryEventHandler(Territory territory, TerritoryPane territoryPane) {
        this.territory = territory;
        this.territoryPane = territoryPane;
    }

    @Override
    public void handle(MouseEvent event) {
        Territory.Tile tile = territoryPane.getTile(event.getX(), event.getY());

        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            handleMousePressed(tile);
        } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            handleMouseDragged(tile);
        } else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
            handleMouseReleased(tile);
        }
    }

    private void handleMousePressed(Territory.Tile tile) {
        Plane plane = this.territory.getPlane();

        if (tile != null && plane.getX() == tile.getX() && plane.getY() == tile.getY()) {
            this.isDraggingPlane = true;
        }
    }

    private void handleMouseDragged(Territory.Tile tile) {
        if (this.isDraggingPlane && tile != null) {
            this.hasDragged = true;
            this.territory.setPlane(tile.getX(), tile.getY());
        }
    }

    private void handleMouseReleased(Territory.Tile tile) {
        this.isDraggingPlane = false;
        if (this.hasDragged) {
            this.hasDragged = false;
            return;
        }

        switch (PlacingState.getState().getSelected()) {
            case PLANE:
                this.territory.setPlane(tile.getX(), tile.getY());
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
                throw new SimulatorException("Should no be reached");
        }
    }
}
