package app.domain.shape;

public final class ShapeUtils {
    public static double distance(Point p, double x, double y) {
        return distance(p.x, p.y, x, y);
    }

    public static double distance(Point p1, Point p2) {
        return distance(p1.x, p1.y, p2.x, p2.y);
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}
