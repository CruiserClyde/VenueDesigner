package app.domain.shape;

import app.domain.Drawable;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Vector;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AbstractShape.class, name = "AbstractShape")
})
public interface Shape extends Drawable {
    Vector<Point> getPoints();
    int[] getColor();
    void setColor(int[] color);
    boolean isSelected();
    void setSelected(boolean selected);
    void move(double x, double y);
    void rotate(double thetaRadian, Point rotationCenter);
    Point computeCentroid();
    Shape clone();
}