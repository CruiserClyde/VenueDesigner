package app.domain.collider;

import app.domain.shape.Shape;

public interface Collider {
    boolean hasCollide(double x, double y, Shape shape);
    boolean hasCollide(Shape shape1, Shape shape2);
    boolean contains(Shape shape1, Shape shape2);
}
