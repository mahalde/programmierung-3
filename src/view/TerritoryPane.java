package view;

import controller.handler.TerritoryEventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import model.Territory;

import java.util.Observable;
import java.util.Observer;

public class TerritoryPane extends Region implements Observer {

    private static final int IMG_SIZE = 32;
    private static final int CELL_SIZE = 40;
    private static final int PADDING = (CELL_SIZE - IMG_SIZE) / 2;

    private final Territory territory;
    private final Canvas canvas;

    private Image[] planeImages;
    private Image thunderstormImage;
    private Image passengerImage;

    public TerritoryPane(Territory territory) {
        this.territory = territory;
        this.canvas = new Canvas(territory.getWidth() * CELL_SIZE, territory.getHeight() * CELL_SIZE);

        this.getChildren().add(this.canvas);

        loadImages();
        draw();

        TerritoryEventHandler territoryEventHandler = new TerritoryEventHandler(territory, this);
        this.canvas.setOnMousePressed(territoryEventHandler);
        this.canvas.setOnMouseDragged(territoryEventHandler);
        this.canvas.setOnMouseReleased(territoryEventHandler);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Territory) {
            Territory territory = (Territory) o;
            this.canvas.setWidth(territory.getWidth() * CELL_SIZE);
            this.canvas.setHeight(territory.getHeight() * CELL_SIZE);
        }

        clearView();
        draw();
    }

    public Territory.Tile getTile(double x, double y) {
        Territory.Tile tile = new Territory.Tile((int) x / CELL_SIZE, (int) y / CELL_SIZE);

        return (tile.getX() >= 0 && tile.getX() < this.territory.getWidth()
                        && tile.getY() >= 0 && tile.getY() < this.territory.getHeight()) ? tile : null;
    }

    private void loadImages() {
        this.planeImages = new Image[]{
                new Image("resources/Plane32North.png"),
                new Image("resources/Plane32East.png"),
                new Image("resources/Plane32South.png"),
                new Image("resources/Plane32West.png"),
        };
        this.thunderstormImage = new Image("resources/Thunderstorm32.png");
        this.passengerImage = new Image("resources/Passenger32.png");
    }

    private void clearView() {
        this.canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void draw() {
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        int width = this.territory.getWidth();
        int height = this.territory.getHeight();
        int planeX = this.territory.getPlaneX();
        int planeY = this.territory.getPlaneY();

        gc.setStroke(Color.BLACK);
        gc.setFill(Color.WHITE);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                gc.strokeRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);

                if (this.territory.hasPassenger(x, y)) {
                    drawPassengers(x, y, this.territory.getPassengers(x, y), gc);
                }

                if (x == planeX && y == planeY) {
                    Image planeImage = this.planeImages[this.territory.getPlaneDirection().ordinal()];
                    gc.drawImage(planeImage, x * CELL_SIZE + PADDING, y * CELL_SIZE + PADDING);
                } else if (this.territory.isThunderstorm(x, y)) {
                    gc.drawImage(this.thunderstormImage, x * CELL_SIZE + PADDING, y * CELL_SIZE + PADDING);
                }
            }
        }
    }

    private void drawPassengers(int x, int y, int amount, GraphicsContext gc) {
        final int NUM_PER_LINE = 3;
        final int MARGIN = 1;
        final int SIZE = (IMG_SIZE - 2) / NUM_PER_LINE;

        for (int i = 0; i < Math.min(NUM_PER_LINE * NUM_PER_LINE, amount); i++) {
            final int HORIZONTAL_MARGIN_BETWEEN_IMAGES = (i % NUM_PER_LINE) * (SIZE + MARGIN);
            final int VERTICAL_MARGIN_BETWEEN_IMAGES = (i / NUM_PER_LINE) * (SIZE + MARGIN);

            gc.drawImage(
                    this.passengerImage,
                    x * CELL_SIZE + PADDING + HORIZONTAL_MARGIN_BETWEEN_IMAGES,
                    y * CELL_SIZE + PADDING + VERTICAL_MARGIN_BETWEEN_IMAGES,
                    SIZE, SIZE
            );
        }
    }
}
