package app.domain.shape;

import app.domain.Drawable;

import java.util.Vector;

public interface ShapeBuilder extends Drawable {
    void addPoint(Point point);
    boolean isComplete();
    Shape build();
    void correctLastPoint();
    Vector<Point> getPoints();
}
