package app.domain.selection;

import app.domain.seat.Seat;
import app.domain.seat.SeatSection;
import app.domain.Stage;
import app.domain.section.SeatedSection;
import app.domain.section.StandingSection;
import app.domain.shape.PointSelection;

public class SelectionAdapter implements SelectionVisitor {
    @Override
    public void visit(Stage stage) {
    }

    @Override
    public void visit(SeatedSection section) {
    }

    @Override
    public void visit(StandingSection section) {
    }

    @Override
    public void visit(Seat seat) {
    }

    @Override
    public void visit(SeatSection seatSection) {
    }

    @Override
    public void visit(PointSelection point) {
    }
}
