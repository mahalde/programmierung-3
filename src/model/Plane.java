package model;

public class Plane {

    public enum Direction {
        NORTH,
        EAST,
        SOUTH,
        WEST
    }

    private final Territory territory;

    private int numberOfPassengers;

    private int x;
    private int y;
    private Direction direction = Direction.EAST;

    Plane(Territory territory) {
        this.territory = territory;
    }

    int getX() {
        return x;
    }

    void setX(int x) {
        this.x = x;
    }

    int getY() {
        return y;
    }

    void setY(int y) {
        this.y = y;
    }

    Direction getDirection() {
        return direction;
    }

    void setDirection(Direction direction) {
        this.direction = direction;
    }

    int getNumberOfPassengers() {
        return numberOfPassengers;
    }

    void setNumberOfPassengers(int numberOfPassengers) {
        this.numberOfPassengers = numberOfPassengers;
    }

    public void vor() {
        this.territory.movePlaneForward(x, y);
    }

    public void linksUm() {
        this.territory.turnPlaneLeft();
    }

    public void onboarden() {
        this.territory.boardOn(x, y);
    }

    public void offboarden() {
        this.territory.boardOff(x, y);
    }

    public boolean vornFrei() {
        return this.territory.freeInFrontOfPlane(x, y);
    }

    public boolean keinePassagiere() {
        return numberOfPassengers == 0;
    }

    public boolean passagierDa() {
        return this.territory.hasPassenger(x, y);
    }
}
