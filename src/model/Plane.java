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

    public Plane(Territory territory) {
        this.territory = territory;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getNumberOfPassengers() {
        return numberOfPassengers;
    }

    public void setNumberOfPassengers(int numberOfPassengers) {
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
