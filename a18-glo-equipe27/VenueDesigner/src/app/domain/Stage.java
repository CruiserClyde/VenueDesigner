package app.domain;

import app.domain.selection.Selection;
import app.domain.selection.SelectionVisitor;
import app.domain.shape.Point;
import app.domain.shape.Shape;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.Vector;

public final class Stage implements Selection {
    private final Shape shape;
    private double elevation;

    public Stage(Shape shape) {
        this(shape, 0);
    }

    @JsonCreator
    public Stage(
            @JsonProperty("shape") Shape shape,
            @JsonProperty("elevation") double elevation
    ) {
        this.shape = Objects.requireNonNull(shape);
        this.elevation = elevation;
    }

    @Override
    @JsonIgnore
    public boolean isSelected() {
        return shape.isSelected();
    }

    @Override
    @JsonIgnore
    public void setSelected(boolean selected) {
        shape.setSelected(selected);
    }

    @Override
    public void move(double x, double y) {
        shape.move(x, y);
    }

    @Override
    public void accept(SelectionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void rotate(double thetaRadian){
        shape.rotate(thetaRadian, getShape().computeCentroid());
    }

    public Shape getShape() {
        return shape;
    }

    public double getWidth() {
        Vector<Point> points = shape.getPoints();
        return points.get(1).x - points.firstElement().x;
    }

    public double getHeight() {
        Vector<Point> points = shape.getPoints();
        return points.lastElement().y - points.firstElement().y;
    }

    public double getElevation() {
        return elevation;
    }

    public void setWidth(double width) {
        Vector<Point> points = shape.getPoints();
        double x = points.firstElement().x;
        points.get(1).x = x + width;
        points.get(2).x = x + width;
    }

    public void setHeight(double height) {
        Vector<Point> points = shape.getPoints();
        double y = points.firstElement().y;
        points.get(2).y = y + height;
        points.get(3).y = y + height;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    @Override
    @JsonIgnore
    public boolean isAuto(){
        return false;
    }
}
