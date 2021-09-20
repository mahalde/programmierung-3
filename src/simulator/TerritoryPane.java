package simulator;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class TerritoryPane extends Region {

    private static final int IMG_SIZE = 32;
    private static final int CELL_SIZE = 40;
    private static final int PADDING = (CELL_SIZE - IMG_SIZE) / 2;

    private final Territory territory;
    private final Canvas canvas;

    private Image planeImage;
    private Image thunderstormImage;
    private Image passengerImage;

    public TerritoryPane(Territory territory) {
        this.territory = territory;
        this.canvas = new Canvas(territory.getWidth() * CELL_SIZE, territory.getHeight() * CELL_SIZE);

        this.getChildren().add(this.canvas);

        loadImages();
        draw();
    }

    private void loadImages() {
        this.planeImage = new Image("resources/Plane32.png");
        this.thunderstormImage = new Image("resources/Thunderstorm32.png");
        this.passengerImage = new Image("resources/Passenger32.png");

    }

    private void draw() {
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        int width = this.territory.getWidth();
        int height = this.territory.getHeight();
        int planeX = this.territory.getPlane().getX();
        int planeY = this.territory.getPlane().getY();

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
                    gc.drawImage(this.planeImage, x * CELL_SIZE + PADDING, y * CELL_SIZE + PADDING);
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
