package cmd;

import model.exception.SimulatorException;
import model.Plane;
import model.Territory;

public class ConsoleTestProgram {

    public static void main(String[] args) {
        Territory territory = new Territory(8, 10);
        Plane plane = territory.getPlane();

        draw(territory, plane);

        int input = makeInput();

        do {
            try {
                switch (input) {
                    case 1:
                        setPlaneCoordinates(territory);
                        break;
                    case 2:
                        placePassenger(territory);
                        break;
                    case 3:
                        placeThunderstorm(territory);
                        break;
                    case 4:
                        deleteTile(territory);
                        break;
                    case 5:
                        plane.vor();
                        break;
                    case 6:
                        plane.linksUm();
                        break;
                    case 7:
                        plane.onboarden();
                        break;
                    case 8:
                        plane.offboarden();
                    case 9:
                        System.out.println("Ausgabe: " + plane.vornFrei());
                        break;
                    case 10:
                        System.out.println("Ausgabe: " + plane.passagierDa());
                        break;
                    case 11:
                        System.out.println("Ausgabe: " + plane.keinePassagiere());
                        break;
                    case 12:
                        changeSize(territory);
                        break;
                    case 13:
                        break;
                    default:
                        throw new RuntimeException("Sollte nicht passieren");
                }
            } catch (SimulatorException e) {
                System.out.println("Fehler: " + e.getMessage());
            }

            draw(territory, plane);
            input = makeInput();
        } while (input != 13);

    }

    private static int makeInput() {
        int input = IO.readInt("?:");
        while (input < 0 || input > 13) {
            input = IO.readInt("Falsche Eingabe! ?:");
        }

        return input;
    }

    private static void draw(Territory territory, Plane plane) {
        System.out.println("\n\n\n\n\n\n");

        System.out.println("Was wollen Sie tun?");
        System.out.println("1.  Position des Flugzeugs ändern");
        System.out.println("2.  Passagier platzieren");
        System.out.println("3.  Gewitter platzieren");
        System.out.println("4.  Kachel löschen");
        System.out.println("5.  Flugzeug vor");
        System.out.println("6.  Flugzeug links um");
        System.out.println("7.  Onboarding");
        System.out.println("8.  Offboarding");
        System.out.println("9.  Vorn Frei?");
        System.out.println("10. Passagier da?");
        System.out.println("11. Keine Passagiere im Flugzeug?");
        System.out.println("12. Größe ändern");
        System.out.println("13. Beenden");

        int[][] tiles = territory.getTiles();
        int planeX = plane.getX(), planeY = plane.getY();
        Plane.Direction planeDirection = plane.getDirection();

        for (int y = 0; y < tiles.length; y++) {
            System.out.print("| ");
            for (int x = 0; x < tiles[y].length; x++) {
                if (x == planeX && y == planeY) {
                    drawPlane(planeDirection);
                }

                if (tiles[y][x] == -1) {
                    System.out.print(" 🌩 ");
                } else if (tiles[y][x] != 0) {
                    System.out.print(" " + tiles[y][x] + " ");
                } else {
                    System.out.print(" 🟦 ");
                }

                System.out.print("|");
            }

            System.out.println();
        }
    }

    private static void drawPlane(Plane.Direction direction) {
        switch (direction) {
            case NORTH:
                System.out.print("⬆");
                break;
            case EAST:
                System.out.print("➡");
                break;
            case SOUTH:
                System.out.print("⬇");
                break;
            case WEST:
                System.out.print("⬅");
                break;
        }
    }

    private static void setPlaneCoordinates(Territory territory) {
        int newX = IO.readInt("X-Koordinate?: ");
        int newY = IO.readInt("Y-Koordinate?: ");

        territory.setPlane(newX, newY);
    }

    private static void placePassenger(Territory territory) {
        int x = IO.readInt("X-Koordinate?: ");
        int y = IO.readInt("Y-Koordinate?: ");
        int amount = IO.readInt("Wie viele?: ");

        territory.setPassenger(x, y, amount);
    }

    private static void placeThunderstorm(Territory territory) {
        int x = IO.readInt("X-Koordinate?: ");
        int y = IO.readInt("Y-Koordinate?: ");

        territory.setThunderstorm(x, y);
    }

    private static void deleteTile(Territory territory) {
        int x = IO.readInt("X-Koordinate?: ");
        int y = IO.readInt("Y-Koordinate?: ");

        territory.clearTile(x, y);
    }

    private static void changeSize(Territory territory) {
        int width = IO.readInt("Breite?: ");
        int height = IO.readInt("Höhe?: ");

        territory.changeSize(width, height);
    }
}
