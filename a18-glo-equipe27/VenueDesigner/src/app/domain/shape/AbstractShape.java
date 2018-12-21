package app.domain.shape;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Objects;
import java.util.Vector;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Polygon.class, name = "Polygon"),
        @JsonSubTypes.Type(value = Rectangle.class, name = "Rectangle")
})
abstract class AbstractShape implements Shape {
    private final Vector<Point> points;
    private int[] color;
    private boolean selected;

    AbstractShape(Vector<Point> points, int[] color) {
        this.points = Objects.requireNonNull(points);
        this.color = Objects.requireNonNull(color);
        this.selected = false;
    }

    AbstractShape(AbstractShape shape) {
        this.points = new Vector<>();
        for (Point p : shape.points) {
            points.add(new Point(p));
        }
        this.color = shape.color;
        this.selected = shape.selected;
    }

    @Override
    @JsonIgnore
    public void setSelected(boolean bool) {
        selected = bool;
    }

    @Override
    @JsonIgnore
    public boolean isSelected() {
        return selected;
    }

    @Override
    public Vector<Point> getPoints() {
        return points;
    }

    @Override
    public int[] getColor() {
        return color;
    }

    @Override
    public void setColor(int[] color) {
        this.color = Objects.requireNonNull(color);
    }

    public double area(){ // Polygon Area Calculation
        double area = 0;
        for(Point p: points){
            if(p == points.lastElement()){
                area += 0.5*(p.x)*(points.firstElement().y)-(points.firstElement().x)*(p.y);
            } else{
                area += 0.5*(p.x) * (points.get(points.indexOf(p)+1).y) - (points.get(points.indexOf(p)+1).y)*(p.y);
            }
        }
        return area;
    }

    @Override
    public Point computeCentroid() {
        double cx =0.0;
        double cy =0.0;
        double signedArea = 0.0;
        double x0; // Current vertex X
        double y0; // Current vertex Y
        double x1; // Next vertex X
        double y1; // Next vertex Y
        double a;  // Partial signed area

        // For all vertices
        for (int i=0; i<points.size(); ++i)
        {
            x0 = points.elementAt(i).x;
            y0 = points.elementAt(i).y;
            x1 = points.elementAt((i+1)%points.size()).x;
            y1 = points.elementAt((i+1)%points.size()).y;
            a = x0*y1 - x1*y0;
            signedArea += a;
            cx += (x0 + x1)*a;
            cy += (y0 + y1)*a;
        }

        signedArea *= 0.5;
        cx /= (6.0*signedArea);
        cy /= (6.0*signedArea);

        return new Point(cx,cy);
    }

    @Override
    public void move(double x, double y) {
        for (Point p : points) {
            p.offset(x, y);
        }
    }

    @Override
    public void rotate(double thetaRadian, Point rotationCenter){
        double gx = rotationCenter.x;
        double gy = rotationCenter.y;
        for(Point p : points){
            double dx = p.x-gx;
            double dy = p.y-gy;
            double px = dx*Math.cos(thetaRadian)-dy*Math.sin(thetaRadian) + gx;
            double py =  dx*Math.sin(thetaRadian)+dy*Math.cos(thetaRadian)+ gy;
            p.set(px,py);
        }
    }

    @Override
    public Shape clone() {
        return null;
    }
}
