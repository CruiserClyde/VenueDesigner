package app.domain.selection;

import app.domain.shape.Point;
import app.domain.shape.Shape;

public interface Selection {
    boolean isSelected();
    void setSelected(boolean selected);
    void move(double x, double y);
    void rotate(double thetaRadian);
    Shape getShape();
    void accept(SelectionVisitor visitor);
    boolean isAuto();
}
