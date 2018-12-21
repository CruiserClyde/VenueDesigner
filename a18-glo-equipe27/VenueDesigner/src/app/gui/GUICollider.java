package app.gui;

import app.domain.collider.Collider;
import app.domain.shape.Point;
import app.domain.shape.Shape;

import java.awt.*;
import java.awt.geom.Area;

final class GUICollider implements Collider {
    @Override
    public boolean hasCollide(double x, double y, Shape shape) {
        Coordinates coordinates = GUIUtils.getCoordinates(shape.getPoints(), new Point(0, 0),1);
        Polygon polygon = new Polygon(coordinates.xCoords, coordinates.yCoords, coordinates.points.size());
        return polygon.contains(x, y);
    }

    @Override
    public boolean hasCollide(Shape shape1, Shape shape2) {
        Coordinates coordinates1 = GUIUtils.getCoordinates(shape1.getPoints(), new Point(0, 0),1);
        Polygon polygon1 = new Polygon(coordinates1.xCoords, coordinates1.yCoords, coordinates1.points.size());
        Area area1 = new Area(polygon1);

        Coordinates coordinates2 = GUIUtils.getCoordinates(shape2.getPoints(), new Point(0, 0),1);
        Polygon polygon2 = new Polygon(coordinates2.xCoords, coordinates2.yCoords, coordinates2.points.size());
        Area area2 = new Area(polygon2);
        area1.intersect(area2);

        return !area1.isEmpty();
    }

    @Override
    public boolean contains (Shape shape1, Shape shape2){
        Coordinates coordinates1 = GUIUtils.getCoordinates(shape1.getPoints(), new Point(0, 0),1);
        Polygon polygon1 = new Polygon(coordinates1.xCoords, coordinates1.yCoords, coordinates1.points.size());
        Area area1 = new Area(polygon1);

        Coordinates coordinates2 = GUIUtils.getCoordinates(shape2.getPoints(), new Point(0, 0),1);
        Polygon polygon2 = new Polygon(coordinates2.xCoords, coordinates2.yCoords, coordinates2.points.size());
        Area area2 = new Area(polygon2);
        area2.intersect(area1);

        return area2.equals(area1);
    }
}
