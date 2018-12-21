package app.domain.selection;

import app.domain.seat.Seat;
import app.domain.seat.SeatSection;
import app.domain.Stage;
import app.domain.section.SeatedSection;
import app.domain.section.StandingSection;
import app.domain.shape.PointSelection;

public interface SelectionVisitor {
    void visit(Stage stage);
    void visit(SeatedSection section);
    void visit(StandingSection section);
    void visit(Seat seat);
    void visit(SeatSection seatSection);
    void visit(PointSelection point);
}
