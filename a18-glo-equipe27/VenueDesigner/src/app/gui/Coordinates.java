package app.gui;

import app.domain.shape.Point;

import java.util.Vector;

final class Coordinates {
    final Vector<Point> points;
    final int[] xCoords;
    final int[] yCoords;

    Coordinates(Vector<Point> points, int[] xCoords, int[] yCoords) {
        this.points = points;
        this.xCoords = xCoords;
        this.yCoords = yCoords;
    }
}
