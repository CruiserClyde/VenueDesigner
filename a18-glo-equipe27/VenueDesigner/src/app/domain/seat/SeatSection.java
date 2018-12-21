package app.domain.seat;

import app.domain.selection.Selection;
import app.domain.selection.SelectionVisitor;
import app.domain.shape.Point;
import app.domain.shape.Shape;

import java.util.Objects;

public final class SeatSection implements Selection {
    private final Seat[] seats;

    public SeatSection(Seat[] seats) {
        this.seats = Objects.requireNonNull(seats);
    }

    @Override
    public boolean isSelected() {
        return seats[0].isSelected();
    }

    @Override
    public void setSelected(boolean selected) {
        for (Seat seat : seats) {
            seat.setSelected(selected);
        }
    }

    @Override
    public void move(double x, double y) {}

    @Override
    public void rotate(double theta_radian) {}

    @Override
    public Shape getShape() {
        return seats[0].getShape();
    }

    @Override
    public void accept(SelectionVisitor visitor) {
        visitor.visit(this);
    }

    public Seat[] getSeats() {
        return seats;
    }

    @Override
    public boolean isAuto(){
        return false;
    }
}
