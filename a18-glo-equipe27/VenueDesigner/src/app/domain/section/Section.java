package app.domain.section;

import app.domain.collider.Collider;
import app.domain.Drawable;
import app.domain.seat.Seat;
import app.domain.Stage;
import app.domain.selection.Selection;
import app.domain.shape.Shape;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.function.Consumer;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = AbstractSection.class, name = "AbstractSection")
})
public interface Section extends Drawable, Selection {
    String getName();
    void setName(String name);
    double getElevation();
    void setElevation(double elevation);
    Seat[][] getSeats();
    Shape getShape();
    void setShape(Shape shape);
    void forEachSeats(Consumer<Seat> consumer);
    void autoSetSeats(Stage stage, Collider collider);
}
