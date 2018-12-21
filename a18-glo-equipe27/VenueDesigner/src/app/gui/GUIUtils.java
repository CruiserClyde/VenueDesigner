package app.gui;

import app.domain.shape.Point;

import java.awt.Color;
import java.util.Vector;

final class GUIUtils {
    static Coordinates getCoordinates(Vector<Point> points, Point offset, double scale) {
        int size = points.size();
        int[] xCoords = new int[size];
        int[] yCoords = new int[size];
        Vector<Point> newPoints = new Vector<>();
        int i = 0;

        for (Point point : points) {
            Point yp = getTransformedPoint(point, offset, scale);
            newPoints.add(yp);
            xCoords[i] = (int)yp.x;
            yCoords[i] = (int)yp.y;
            i++;
        }

        return new Coordinates(newPoints, xCoords, yCoords);
    }

    static Point getTransformedPoint(Point p0, Point offset, double scale){
        double px = scale*p0.x + offset.x;
        double py = scale*p0.y + offset.y;
        return new Point(px,py);
    }

    static boolean isNotInteger(String text) {
        try {
            Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return true;
        }
        return false;
    }
    static boolean isNotNumber(String text){
        try {
            Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return true;
        }
        return false;
    }

    static int[] colorToArray(Color color) {
        return new int[]{color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()};
    }
}
