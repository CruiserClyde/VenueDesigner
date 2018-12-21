package app.domain;

import app.domain.collider.Collider;
import app.domain.collider.ColliderValidator;
import app.domain.seat.Seat;
import app.domain.seat.SeatSection;
import app.domain.section.SeatedSection;
import app.domain.section.Section;
import app.domain.section.StandingSection;
import app.domain.selection.Selection;
import app.domain.selection.SelectionAdapter;
import app.domain.selection.SelectionHolder;
import app.domain.selection.SelectionVisitor;
import app.domain.shape.Point;
import app.domain.shape.PointSelection;
import app.domain.shape.Shape;
import app.domain.shape.ShapeBuilder;
import app.domain.shape.ShapeBuilderFactory;
import app.domain.section.SectionFactory;

import java.util.*;

public class Controller {
    private final Collider collider;
    private final ColliderValidator validator;
    private final SelectionHolder selectionHolder;

    private final Point cursor = new Point(-1, -1);
    private final Point offset = new Point(30, 30);
    private final History history = new History();
    private final JSONSerialize serializer = new JSONSerialize();

    private Room room;
    private Mode mode = Mode.None;
    private UIPanel ui;
    private ShapeBuilder current;
    private double scale = 1.0;
    private Seat hoveredSeat;
    private Section hoveredSection;
    private boolean seatHovered = false;
    private Timer timer = new Timer();
    private Observer observer;
    private double delta = 20.0;
    private double deltaScale = 1;
    private boolean isGridOn = true;

    public Controller(Collider collider) {
        this.collider = Objects.requireNonNull(collider);
        this.validator = new ColliderValidator(collider);
        this.selectionHolder = new SelectionHolder(collider);
        this.room = new Room(850, 850, new VitalSpace(30, 30));
    }

    public Room getRoom() {
        return room;
    }

    public void setDrawingPanel(UIPanel ui) {
        this.ui = Objects.requireNonNull(ui);
    }

    public void createRoom(double roomWidth, double roomHeight, double vitalSpaceWidth, double vitalSpaceHeight) {
        room = new Room(roomWidth, roomHeight, new VitalSpace(vitalSpaceWidth, vitalSpaceHeight));
    }

    public void save(String path) {
        selectionHolder.resetSelection(room);
        serializer.serializeToJson(room, path);
    }

    public void load(String path) {
        room = serializer.deserializeFromJson(path);
        selectionHolder.resetSelection(room);
        ui.repaint();
    }

    public double getXCursor () {
        return  cursor.x;
    }

    public double getYCursor () {
        return  cursor.y;
    }

    public double getScale() {
        return scale;
    }

    public void mouseDragged(int x, int y) {
        observer.onLeave();
        Point destination = getTransformedPoint(new Point(x,y));
        double dx;
        double dy;
        if (isGridOn){
            Point magnet = magnet(destination);
            dx= magnet.x-cursor.x;
            dy = magnet.y-cursor.y;
            cursor.set(magnet.x,magnet.y);
        } else {
            dx = destination.x - cursor.x;
            dy = destination.y - cursor.y;
            cursor.set(destination.x, destination.y);
        }
        if (!selectionHolder.applySelection(new SelectionVisitor() {
            @Override
            public void visit(Stage stage) {
                move(stage);
            }

            @Override
            public void visit(SeatedSection section) {
                move(section);
            }

            @Override
            public void visit(StandingSection section) {
                move(section);
            }

            @Override
            public void visit(Seat seat) {
                move(selectionHolder.getPreSelection());
            }

            @Override
            public void visit(SeatSection seatSection) {
                move(selectionHolder.getPreSelection());
            }

            @Override
            public void visit(PointSelection point) {

                Shape backup = selectionHolder.getPreSelection().getShape().clone();
                point.move(cursor.x, cursor.y);
                if(!validator.validPredictShape(selectionHolder.getPreSelection().getShape(),selectionHolder.getPreSelection().getShape(),getRoom())){
                 selectionHolder.getPreSelection().setShape(backup);
                }
                ui.repaint();
            }

            private void move(Selection selection) {
                if (isMovable(selection.getShape(), dx,dy)) {
                    selection.move(dx, dy);
                    if (isGridOn()){
                        anchor(selection);
                    }
                    ui.repaint();
                }
            }
        }))
        ui.repaint();
    }

    public void mouseReleased() {
        selectionHolder.applySelection(new SelectionVisitor() {
            @Override
            public void visit(Stage stage) {
                saveRoom();
                autoSetSeat();
            }

            @Override
            public void visit(SeatedSection section) {
                saveRoom();
                autoSetSeat();
            }

            @Override
            public void visit(StandingSection section) {
                saveRoom();
            }

            @Override
            public void visit(Seat seat) {
                saveRoom();
                autoSetSeat();
            }

            @Override
            public void visit(SeatSection seatSection) {
                saveRoom();
                autoSetSeat();
            }

            @Override
            public void visit(PointSelection point) {
                point.recalculate(room.getStage().get(), collider);
                saveRoom();
            }
        });
    }

    public void mouseClicked(int x, int y) {
        observer.onLeave();
        if (room == null) {
            return;
        }
        Point destination = getTransformedPoint(new Point(x,y));
        cursor.set(destination.x,destination.y);
        if(isGridOn){
            Point magnet = magnet(cursor);
            cursor.set(magnet.x,magnet.y);
        }
        if (mode == Mode.None || mode == Mode.Selection) {
            doSelection(cursor.x, cursor.y);
        } else {
            doShape(cursor.x, cursor.y);
        }
        ui.repaint();
    }

    public void mouseMoved(int x, int y) {
        seatHovered = false;
        Point destination = getTransformedPoint(new Point(x,y));
        cursor.set(destination.x, destination.y);
        if(isGridOn){
            Point magnet = magnet(cursor);
            cursor.set(magnet.x,magnet.y);
        }
        for (Section s: room.getSections()){
            s.forEachSeats( seat -> {
                if (selectionHolder.selectionCheck(seat.getShape(), cursor.x, cursor.y)) {
                    seatHovered = true;
                    if (hoveredSeat != seat) {
                        observer.onLeave();
                        hoveredSection = s;
                        hoveredSeat = seat;
                        timerReset();
                    }
                }
            });
        }
        if (!seatHovered){
            observer.onLeave();
            timer.cancel();
            timer.purge();
            timer = new Timer();
        }
        ui.repaint();
    }

    public void mouseWheelMoved(double rotation) {
        double scale = (0.05 *-(rotation));
        zoom(this.scale+scale);
        ui.repaint();
    }

    public void zoom(double scale) {
        delta = delta * (this.scale+2-this.scale%2);
        this.scale = scale;
        delta=delta/(this.scale+2-this.scale%2);
        ui.repaint();
    }

    public boolean toggleMode(Mode mode) {
        if (room == null) {
            return false;
        }
        current = null;
        if (this.mode == mode) {
            this.mode = Mode.None;
            return false;
        }
        this.mode = Objects.requireNonNull(mode);
        selectionHolder.resetSelection(room);
        ui.repaint();
        return true;
    }

    public Optional<ShapeBuilder> getCurrent() {
        return Optional.ofNullable(current);
    }

    public Mode getMode() {
        return mode;
    }

    public Point getOffset() {
        return offset;
    }

    public void editSelected(SelectionVisitor visitor) {
        selectionHolder.applySelection(visitor);
    }

    public void removeSelected() {
        selectionHolder.applySelection(new SelectionAdapter() {
            @Override
            public void visit(Stage stage) {
                room.setStage(null);
                mode = Mode.None;
                saveRoom();
            }

            @Override
            public void visit(SeatedSection section) {
                room.getSections().remove(section);
                saveRoom();
            }

            @Override
            public void visit(StandingSection section) {
                room.getSections().remove(section);
                saveRoom();
            }
        });
        ui.repaint();
    }

    public void rotateSelected(boolean direction) {
        selectionHolder.applySelection(new SelectionAdapter() {
            @Override
            public void visit(Stage stage) {
                rotate(stage);
                autoSetSeat();
            }

            @Override
            public void visit(SeatedSection section) {
                rotate(section);
                autoSetSeat();
            }

            @Override
            public void visit(StandingSection section) {
                rotate(section);
            }

            private void rotate(Selection select){
                if (isRotatable(select.getShape(), direction)){
                    if (direction){
                        select.rotate(Math.PI/32);
                    } else {
                        select.rotate(-Math.PI/32);
                    }
                    saveRoom();
                }
            }
        });
        ui.repaint();
    }

    public boolean isAutoSelected() {
        return selectionHolder.getSelection().map(Selection::isAuto).orElse(false);
    }

    public void autoSetSeatSelected(){
        selectionHolder.applySelection(new SelectionAdapter() {
            @Override
            public void visit(SeatedSection section) {
                section.autoSetSeat=!section.autoSetSeat;
            }
        });
    }

    public void autoSetSeat() {
        room.getStage().ifPresent(stage -> {
            room.getSections().forEach(section -> {
                section.accept(new SelectionAdapter() {
                    @Override
                    public void visit(SeatedSection section) {
                        if (section.autoSetSeat){
                            section.autoSetSeats(stage, collider);
                        }
                    }
                });
            });
            ui.repaint();
        });
    }

    public boolean createRegularSection(int x, int y, int xInt, int yInt) {
        if (!room.getStage().isPresent()) {
            return false;
        }
        Section section = SeatedSection.create(x , y , xInt, yInt, room.getVitalSpace(), room.getStage().get());
        if (!validator.validShape(section.getShape(), room)) {
            return false;
        }
        room.addSection(section);
        mode = Mode.None;
        saveRoom();
        return true;
    }

    private void doSelection(double x, double y) {
        mode = Mode.None;
        if (selectionHolder.checkSelection(room, x, y)) {
            mode = Mode.Selection;
        }
    }

    private void doShape(double x, double y) {
        if (current == null) {
            current = ShapeBuilderFactory.create(mode);
        }
        current.addPoint(new Point(x, y));
        if (current.isComplete()) {
            createShape();
        }
    }

    private void createShape() {
        current.correctLastPoint();
        Shape shape = current.build();
        current = null;
        if (!validator.validShape(shape, room)) {
            return;
        }
        if (mode == Mode.Stage) {
            room.setStage(new Stage(shape));
        } else {
            Section s = SectionFactory.create(mode, shape, room.getVitalSpace());
            if (mode == Mode.IrregularSeatedSection){
                s.autoSetSeats(room.getStage().get(), collider);
            }
            room.addSection(s);
        }
        mode = Mode.None;
        saveRoom();
    }

    private boolean isMovable(Shape shape, double x, double y) {
        Shape predict = shape.clone();
        predict.move(x, y);
        return validator.validPredictShape(shape, predict, room);
    }

    private boolean isRotatable(Shape shape, boolean direction) {
        Shape predict = shape.clone();
        if (direction){
            predict.rotate(Math.PI/32,predict.computeCentroid());
        } else {
            predict.rotate(-Math.PI/32,predict.computeCentroid());
        }
        return validator.validPredictShape(shape, predict, room);
    }

    public boolean validateRoomDimensions(double roomWidth, double roomHeight, double vitalSpaceWidth, double vitalSpaceHeight) {
        VitalSpace vs = new VitalSpace(vitalSpaceWidth, vitalSpaceHeight);
        Room predict = new Room(roomWidth, roomHeight, vs);
        Optional<Stage> opt = room.getStage();
        if (opt.isPresent()) {
            if (validator.invalidShapeRoom(opt.get().getShape(), predict)) {
                return false;
            }
            for (Section section : room.getSections()) {
                if (validator.invalidShapeRoom(section.getShape(), predict)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean validateSectionDimensions(Section section, double spaceWidth, double spaceHeight) {
        VitalSpace vs = new VitalSpace(spaceWidth, spaceHeight);
        Shape predict = section.getShape().clone();
        return validator.validPredictShape(section.getShape(), predict, room);
    }

    public boolean validateStageDimensions(Stage stage, double width, double height) {
        Shape shape = stage.getShape().clone();
        Stage predict = new Stage(shape);
        predict.setWidth(width);
        predict.setHeight(height);
        return validator.validPredictShape(stage.getShape(), predict.getShape(), room);
    }

    public void setObserver(Observer observer){
        this.observer = Objects.requireNonNull(observer);
    }
    
    private void timerReset(){
        timer.cancel();
        timer.purge();
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                observer.onHover();
            }
        };
        timer.schedule(task, 500);
    }

    public Seat getHoveredSeat() {
        return hoveredSeat;
    }

    public Section getHoveredSection() {
        return hoveredSection;
    }

    public double prepareSave(int panelWidth, int panelHeight) {
        double maxRoom;
        int maxPanel;
        double roomWidth = room.getWidth();
        double roomHeight = room.getHeight();
        if (roomHeight > roomWidth || roomWidth < 2 * roomHeight) {
            maxRoom = roomHeight;
            maxPanel = panelHeight;
        } else {
            maxRoom = roomWidth;
            maxPanel = panelWidth;
        }
        scale = (0.9 * maxPanel) / maxRoom;
        offset.x = 0;
        offset.y = 0;
        return scale;
    }

    public void offsetScale(Point offset, double scale) {
        this.offset.x = offset.x;
        this.offset.y = offset.y;
        this.scale = scale;
        ui.repaint();
    }

    private Point magnet(Point cursor){

        double dx =cursor.x%delta;
        double dy =cursor.y%delta;
        Point magnet = new Point();
        if (dx >= delta/2){ dx = dx - delta;}
        if (dy >= delta/2){ dy = dy - delta;}
        magnet.set(cursor.x-dx,cursor.y-dy);
        return magnet;
    }

    public void toggleGrid(){
        isGridOn=!isGridOn;
        ui.repaint();
    }

    public boolean isGridOn() {
        return isGridOn;
    }

    public double getDelta() {
        return delta;
    }

    public void setDeltaScale( double deltaScale){
        this.delta = delta/this.deltaScale;
        this.deltaScale=deltaScale;
        this.delta = delta*this.deltaScale;
        ui.repaint();
    }

    public Point getCursor(){
        return cursor;
    }

    private Point getTransformedPoint(Point p0){
        double px = (p0.x-offset.x)/scale;
        double py = (p0.y-offset.y)/scale;
        return new Point(px,py);
    }

    public void saveRoom() {
        history.add(serializer.toJson(room));
        observer.onUndoRedo();
    }

    public void undo() {
        if (history.canUndo()) {
            room = serializer.fromJson(history.undo());
            selectionHolder.resetSelection(room);
            observer.onUndoRedo();
            ui.repaint();
        }
    }

    public void redo() {
        if (history.canRedo()) {
            room = serializer.fromJson(history.redo());
            selectionHolder.resetSelection(room);
            observer.onUndoRedo();
            ui.repaint();
        }
    }

    public boolean canUndo() {
        return history.canUndo();
    }

    public boolean canRedo() {
        return history.canRedo();
    }

    public SelectionHolder getSelectionHolder(){
        return selectionHolder;
    }

    private void anchor(Selection selection){
        Point p0 =selection.getShape().getPoints().firstElement();
        Point anchor = magnet(p0);
        if (p0!=anchor){
            double dx = anchor.x-p0.x;
            double dy = anchor.y - p0.y;
            selection.move(dx,dy);
        }
    }
}
