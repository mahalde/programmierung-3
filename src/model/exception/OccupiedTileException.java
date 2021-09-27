package model.exception;

public class OccupiedTileException extends SimulatorException {

    public OccupiedTileException(String occupant, int x, int y) {
        super(String.format("Das gegebene Feld (%s, %s) ist bereits besetzt durch %s", x, y, occupant));
    }
}
