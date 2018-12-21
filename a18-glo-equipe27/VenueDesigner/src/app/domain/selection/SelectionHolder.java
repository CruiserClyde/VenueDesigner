package app.domain.selection;

import app.domain.Room;
import app.domain.Stage;
import app.domain.collider.Collider;
import app.domain.seat.Seat;
import app.domain.seat.SeatSection;
import app.domain.section.SeatedSection;
import app.domain.section.Section;
import app.domain.section.StandingSection;
import app.domain.shape.Point;
import app.domain.shape.PointSelection;
import app.domain.shape.Shape;
import app.domain.shape.ShapeUtils;

import java.util.Objects;
import java.util.Optional;

public final class SelectionHolder {
    private final Collider collider;
    private Selection current;
    private Section preSelection;

    public SelectionHolder(Collider collider) {
        this.collider = Objects.requireNonNull(collider);
    }

    public boolean checkSelection(Room room, double x, double y) {
        Selection backup = current;
        resetSelection(room);

        if (backup != null) {
            boolean[] selection = new boolean[]{false};
            backup.accept(new SelectionAdapter() {
                @Override
                public void visit(SeatedSection section) {
                    section.forEachSeats(seat -> {
                        if (!selection[0] && selectionCheck(seat.getShape(), x, y)) {
                            current = seat;
                            current.setSelected(true);
                            preSelection.setSelected(true);
                            selection[0] = true;
                        }
                    });
                    if (!selection[0] && !section.isRegular) {
                        PointSelection pointSelection = pointSelection(section.getShape());
                        if (pointSelection == null) {
                            return;
                        }
                        pointSelection.setSeatedSection(section);
                    }
                }

                @Override
                public void visit(Seat seat) {
                    Seat[] seats = preSelection.getSeats()[seat.getRow()];
                    for (Seat s : seats) {
                        if (selectionCheck(s.getShape(), x, y)) {
                            current = new SeatSection(seats);
                            current.setSelected(true);
                            preSelection.setSelected(true);
                            selection[0] = true;
                            return;
                        }
                    }
                }

                @Override
                public void visit(StandingSection section) {
                    pointSelection(section.getShape());
                }

                private PointSelection pointSelection(Shape shape) {
                    for (Point p : shape.getPoints()) {
                        if (selectionCheck(p, x, y) ){
                            PointSelection pointSelection = new PointSelection(shape, p);
                            current = pointSelection;
                            preSelection.setSelected(true);
                            selection[0] = true;
                            return pointSelection;
                        }
                    }
                    return null;
                }
            });
            if (selection[0]) {
                return true;
            }
        }

        if (room.getStage().isPresent()) {
            Stage stage = room.getStage().get();
            if (selectionCheck(stage.getShape(), x, y)) {
                current = stage;
                current.setSelected(true);
                return true;
            }
        }

        for (Section section : room.getSections()) {
            if (selectionCheck(section.getShape(), x, y)) {
                current = section;
                current.setSelected(true);
                preSelection = section;
                return true;
            }
        }

        return false;
    }

    public boolean selectionCheck(Shape shape, double x, double y) {
        return collider.hasCollide(x , y , shape);
    }

    public boolean selectionCheck(Point point, double x, double y) {
        return ShapeUtils.distance(point, x , y ) < 50;
    }

    public void resetSelection(Room room) {
        if (current != null) {
            current.setSelected(false);
            current = null;
        }
        room.getStage().ifPresent(stage -> stage.setSelected(false));
        room.getSections().forEach(section -> {
            section.setSelected(false);
            section.forEachSeats(seat -> seat.setSelected(false));
        });
    }

    public Optional<Selection> getSelection() {
        return Optional.ofNullable(current);
    }

    public Section getPreSelection() {
        return preSelection;
    }

    public boolean applySelection(SelectionVisitor visitor) {
        if (current != null) {
            current.accept(visitor);
            return true;
        }
        return false;
    }
}
