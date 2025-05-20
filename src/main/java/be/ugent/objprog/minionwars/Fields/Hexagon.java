package be.ugent.objprog.minionwars.Fields;

import javafx.scene.shape.Polygon;

public abstract class Hexagon extends Polygon {

    // Voor het maken van zeshoeken heb ik gebruik gemaakt van deze sites:
    // https://stackoverflow.com/questions/54165602/create-hexagonal-field-with-javafx
    // https://www.redblobgames.com/grids/hexagons/
    private final static double r = 60; // the inner radius from hexagon center to outer corner
    private final static double n = Math.sqrt(r * r * 0.75); // the inner radius from hexagon center to middle of the axis
    private final static double TILE_HEIGHT = 2 * r;
    private final static double TILE_WIDTH = 2 * n;
    private final static int xStartOffset = 20;
    private final static int yStartOffset = 40;

    protected final int x;
    protected final int y;

    public Hexagon(int x, int y) {
        this.x = y % 2 == 0 ? 2 * x : 2 * x + 1;
        this.y = y;

        double xCoord = (x*2 + 1)* TILE_WIDTH / 2 + (y % 2) * n + xStartOffset;
        double yCoord = y * TILE_HEIGHT * 0.75 + yStartOffset;
        getPoints().addAll(
                xCoord, yCoord,
                xCoord, yCoord + r,
                xCoord + n, yCoord + r * 1.5,
                xCoord + TILE_WIDTH, yCoord + r,
                xCoord + TILE_WIDTH, yCoord,
                xCoord + n, yCoord - r * 0.5
        );
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

}
