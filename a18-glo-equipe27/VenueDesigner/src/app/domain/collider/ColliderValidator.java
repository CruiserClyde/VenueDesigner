package app.domain.collider;

import app.domain.Room;
import app.domain.section.Section;
import app.domain.shape.Point;
import app.domain.shape.Shape;

import java.util.Objects;
import java.util.Vector;

public final class ColliderValidator {
    private final Collider collider;

    public ColliderValidator(Collider collider) {
        this.collider = Objects.requireNonNull(collider);
    }

    public boolean validShape(Shape shape, Room room) {
        if (invalidShapeRoom(shape, room)) {
            return false;
        }
        if (room.getStage().isPresent()) {
            if (collider.hasCollide(shape, room.getStage().get().getShape())) {
                return false;
            }
        }
        for (Section section : room.getSections()) {
            if (collider.hasCollide(shape, section.getShape())) {
                return false;
            }
        }
        return true;
    }

    public boolean validPredictShape(Shape shape, Shape predict, Room room) {
        if (invalidShapeRoom(predict, room)) {
            return false;
        }
        if (room.getStage().isPresent() && shape != room.getStage().get().getShape()) {
            if (collider.hasCollide(predict, room.getStage().get().getShape())) {
                return false;
            }
        }
        for (Section section : room.getSections()) {
            if (shape != section.getShape() && collider.hasCollide(predict, section.getShape())) {
                return false;
            }
        }
        return true;
    }

    public boolean invalidShapeRoom(Shape shape, Room room) {
        Vector<Point> points = room.getShape().getPoints();
        double x = points.firstElement().x;
        double y = points.firstElement().y;

        for (Point p : shape.getPoints()) {
            if (p.x  < x || p.x  > x + room.getWidth()) {
                return true;
            }
            if (p.y  < y || p.y  > y + room.getHeight()) {
                return true;
            }
        }

        return false;
    }
}
