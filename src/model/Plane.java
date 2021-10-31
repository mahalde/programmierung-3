package model;

/**
 * The plane which is shown in the territory.
 * All methods get delegated to the territory.
 */
public class Plane {

    /**
     * The direction of the plane
     */
    public enum Direction {
        NORTH,
        EAST,
        SOUTH,
        WEST
    }

    private Territory territory;

    private int numberOfPassengers;

    private int x;
    private int y;
    private Direction direction = Direction.EAST;

    /** Empty construcor so the written code can be compiled successfully */
    public Plane() {}

    /** Empty main method which is invoked by the simulation */
    public void main() {}

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

    void setTerritory(Territory territory) {
        this.territory = territory;
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
        this.territory.movePlaneForward(this.x, this.y);
    }

    public void linksUm() {
        this.territory.turnPlaneLeft();
    }

    public void onboarden() {
        this.territory.boardOn(this.x, this.y);
    }

    public void offboarden() {
        this.territory.boardOff(this.x, this.y);
    }

    public boolean vornFrei() {
        return this.territory.freeInFrontOfPlane(this.x, this.y);
    }

    public boolean keinePassagiere() {
        return numberOfPassengers == 0;
    }

    public boolean passagierDa() {
        return this.territory.hasPassenger(this.x, this.y);
    }
}
