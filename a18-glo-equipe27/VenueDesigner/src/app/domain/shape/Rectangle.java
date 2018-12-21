package app.domain.shape;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Vector;

public final class Rectangle extends AbstractShape {
    public static class Builder implements ShapeBuilder {
        private final Vector<Point> points = new Vector<>();
        private final int[] color = new int[4];

        Builder(int r, int g, int b, int a) {
            color[0] = r;
            color[1] = g;
            color[2] = b;
            color[3] = a;
        }

        @Override
        public void addPoint(Point point) {
            if (points.isEmpty()) {
                points.add(point);
            } else {
                points.add(new Point(point.x, points.firstElement().y));
                points.add(point);
                points.add(new Point(points.firstElement().x, point.y));
            }
        }

        @Override
        public boolean isComplete() {
            return points.size() == 4;
        }

        @Override
        public void correctLastPoint() {}

        @Override
        public Rectangle build() {
            return new Rectangle(points, color);
        }

        @Override
        public Vector<Point> getPoints() {
            return points;
        }

        @Override
        public <T> void accept(T g, Painter<T> painter) {
            painter.draw(g, this);
        }
    }

    @JsonCreator
    public Rectangle(
            @JsonProperty("points") Vector<Point> points,
            @JsonProperty("color") int[] color
    ) {
        super(points, color);
    }

    private Rectangle(Rectangle rectangle) {
        super(rectangle);
    }

    public static Rectangle create(double x, double y, double width, double height, int[] color) {
        Vector<Point> points = new Vector<>();
        points.add(new Point(x, y));
        points.add(new Point(x + width, y));
        points.add(new Point(x + width, y + height));
        points.add(new Point(x, y + height));

        return new Rectangle(points, color);
    }

    public static Rectangle create(double x, double y, double width, double height, int[] color, double theta) {
        Vector<Point> points = new Vector<>();
        double diag = Math.sqrt(Math.pow(height,2)+Math.pow(width,2));
        double alpha = Math.atan(height/width);
        points.add(new Point(x, y));
        points.add(new Point(x+width*Math.cos(theta-Math.PI/2), y+width*Math.sin(theta-Math.PI/2)));
        points.add(new Point(x+diag*Math.cos(theta-Math.PI/2+alpha),y +diag*Math.sin(theta-Math.PI/2+alpha)));
        points.add(new Point(x+height*Math.cos(theta), y+height*Math.sin(theta)));
        return new Rectangle(points, color);
    }

    public static Rectangle create(Vector<Point> points, int[] color){
        return new Rectangle(points, color);
    }

    @Override
    public <T> void accept(T g, Painter<T> painter) {
        painter.draw(g, this);
    }

    @Override
    public Shape clone() {
        return new Rectangle(this);
    }
}
