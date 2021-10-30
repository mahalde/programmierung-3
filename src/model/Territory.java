package model;

import model.exception.IllegalMoveException;
import model.exception.IllegalSizeException;
import model.exception.NoPassengersException;
import model.exception.OccupiedTileException;
import model.exception.OutOfBoundsException;
import model.exception.SimulatorException;

import java.util.Arrays;
import java.util.Observable;

/**
 * Contains methods and fields for the whole territory
 */
public class Territory extends Observable {

    private static final int THUNDERSTORM = -1;
    private static final int CLEAR_TILE = 0;

    private int[][] tiles;
    private int width;
    private int height;
    private Plane plane;

    public Territory(int width, int height) {
        this.width = width;
        this.height = height;

        createNewTiles();
        this.plane = new Plane(this);
    }

    private void createNewTiles() {
        tiles = new int[height][width];
        for (int i = 0; i < height; i++) {
            tiles[i] = new int[width];

            for (int j = 0; j < width; j++) {
                tiles[i][j] = CLEAR_TILE;
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[][] getTiles() {
        return tiles;
    }

    public Plane getPlane() {
        return plane;
    }

    public void setPlane(Plane plane) {
        synchronized (this) {
            plane.setX(this.plane.getX());
            plane.setY(this.plane.getY());
            plane.setDirection(this.plane.getDirection());
            plane.setNumberOfPassengers(this.plane.getNumberOfPassengers());

            this.plane = plane;
            this.plane.setTerritory(this);
        }

        this.setChanged();
        this.notifyObservers();
    }

    public void setPlaneCoordinates(int x, int y) {
        synchronized (this) {
            checkIfOutOfBounds(x, y);

            if (isThunderstorm(x, y)) {
                throw new OccupiedTileException("Gewitter", x, y);
            }

            this.plane.setX(x);
            this.plane.setY(y);
        }

        this.setChanged();
        this.notifyObservers();
    }

    public int getPlaneX() {
        return this.plane.getX();
    }

    public int getPlaneY() {
        return this.plane.getY();
    }

    public Plane.Direction getPlaneDirection() {
        return this.plane.getDirection();
    }

    public int getNumberOfPlanePassengers() {
        return this.plane.getNumberOfPassengers();
    }

    public void setThunderstorm(int x, int y) {
        synchronized (this) {
            checkIfOutOfBounds(x, y);

            if (this.plane.getX() == x && this.plane.getY() == y) {
                throw new OccupiedTileException("Flugzeug", x, y);
            } else if (tiles[y][x] > 0) {
                throw new OccupiedTileException("Passagiere", x, y);
            }

            tiles[y][x] = THUNDERSTORM;
        }

        this.setChanged();
        this.notifyObservers();
    }

    public void setPassenger(int x, int y, int amount) {
        synchronized (this) {
            if (amount < 1 || amount > 50) {
                throw new IllegalSizeException(1, 50);
            }

            checkIfOutOfBounds(x, y);

            if (isThunderstorm(x, y)) {
                throw new OccupiedTileException("Gewitter", x, y);
            }

            tiles[y][x] = amount;
        }

        this.setChanged();
        this.notifyObservers();
    }

    public void clearTile(int x, int y) {
        synchronized (this) {
            checkIfOutOfBounds(x, y);

            tiles[y][x] = CLEAR_TILE;
        }

        this.setChanged();
        this.notifyObservers();
    }

    public void changeSize(int newWidth, int newHeight) {
        synchronized (this) {
            if (newWidth < 1 || newHeight < 1 || newWidth > 100 || newHeight > 100) {
                throw new IllegalSizeException(1, 100);
            }

            if (this.plane.getX() >= newWidth || this.plane.getY() >= newHeight) {
                this.plane.setX(0);
                this.plane.setY(0);

                if (isThunderstorm(0, 0)) {
                    tiles[0][0] = CLEAR_TILE;
                }
            }

            int[][] newTiles = new int[newHeight][newWidth];

            for (int y = 0; y < newHeight; y++) {
                try {
                    newTiles[y] = Arrays.copyOf(tiles[y], newWidth);
                } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
                    newTiles[y] = new int[newWidth];
                }
            }

            tiles = newTiles;
            this.width = newWidth;
            this.height = newHeight;
        }

        this.setChanged();
        this.notifyObservers();
    }

    private void checkIfOutOfBounds(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            throw new OutOfBoundsException(x, y);
        }
    }

    public void movePlaneForward(int x, int y) {
        synchronized (this) {
            switch (this.plane.getDirection()) {
                case NORTH:
                    throwIllegalMoveIfNotFreeInFrontOfPlane(x, y);

                    this.plane.setY(y - 1);
                    break;
                case EAST:
                    throwIllegalMoveIfNotFreeInFrontOfPlane(x, y);

                    this.plane.setX(x + 1);
                    break;
                case SOUTH:
                    throwIllegalMoveIfNotFreeInFrontOfPlane(x, y);

                    this.plane.setY(y + 1);
                    break;
                case WEST:
                    throwIllegalMoveIfNotFreeInFrontOfPlane(x, y);

                    this.plane.setX(x - 1);
                    break;
                default:
                    throw new SimulatorException("This should absolutely not happen");
            }
        }

        this.setChanged();
        this.notifyObservers();
    }

    private void throwIllegalMoveIfNotFreeInFrontOfPlane(int x, int y) {
        if (!freeInFrontOfPlane(x, y)) {
            throw new IllegalMoveException();
        }
    }

    public void turnPlaneLeft() {
        synchronized (this) {
            switch (this.plane.getDirection()) {
                case NORTH:
                    this.plane.setDirection(Plane.Direction.WEST);
                    break;
                case EAST:
                    this.plane.setDirection(Plane.Direction.NORTH);
                    break;
                case SOUTH:
                    this.plane.setDirection(Plane.Direction.EAST);
                    break;
                case WEST:
                    this.plane.setDirection(Plane.Direction.SOUTH);
                    break;
                default:
                    throw new SimulatorException("This should absolutely not happen");
            }
        }

        this.setChanged();
        this.notifyObservers();
    }

    public void boardOn(int x, int y) {
        synchronized (this) {
            if (tiles[y][x] <= 0) {
                throw new NoPassengersException("Das gegebene Feld (%s, %s) hat keine Passagiere", x, y);
            }

            tiles[y][x]--;
            this.plane.setNumberOfPassengers(this.plane.getNumberOfPassengers() + 1);
        }

        this.setChanged();
        this.notifyObservers();
    }

    public void boardOff(int x, int y) {
        synchronized (this) {
            int numberOfPassengers = this.plane.getNumberOfPassengers();
            if (numberOfPassengers <= 0) {
                throw new NoPassengersException("Im Flugzeug befinden sich keine Passagiere");
            }

            this.plane.setNumberOfPassengers(numberOfPassengers - 1);
            tiles[y][x]++;
        }

        this.setChanged();
        this.notifyObservers();
    }

    public boolean freeInFrontOfPlane(int x, int y) {
        boolean result;
        switch (this.plane.getDirection()) {
            case NORTH:
                result = y == 0 || isThunderstorm(x, y - 1);
                break;
            case EAST:
                result = x == width - 1 || isThunderstorm(x + 1, y);
                break;
            case SOUTH:
                result = y == height - 1 || isThunderstorm(x, y + 1);
                break;
            case WEST:
                result = x == 0 || isThunderstorm(x - 1, y);
                break;
            default:
                throw new SimulatorException("This should absolutely not happen");
        }

        return !result;
    }

    public boolean hasPassenger(int x, int y) {
        return tiles[y][x] >= 1;
    }

    public int getPassengers(int x, int y) {
        if (isThunderstorm(x, y)) {
            throw new OccupiedTileException("Gewitter", x, y);
        }

        return tiles[y][x];
    }

    public boolean isThunderstorm(int x, int y) {
        return tiles[y][x] == THUNDERSTORM;
    }

    public static class Tile {
        private final int x;
        private final int y;

        public Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}
