package app.domain.shape;

import app.domain.Room;
import app.domain.seat.Seat;
import app.domain.Stage;
import app.domain.section.SeatedSection;
import app.domain.section.StandingSection;

public interface Painter<T> {
    void draw(T g, Room room);
    void draw(T g, Stage stage);
    void draw(T g, SeatedSection seatedSection);
    void draw(T g, StandingSection standingSection);
    void draw(T g, Seat seat);
    void draw(T g, Rectangle rectangle);
    void draw(T g, Polygon polygon);
    void draw(T g, Rectangle.Builder rectangle);
    void draw(T g, Polygon.Builder polygon);
}
