package app.domain.section;

import app.domain.collider.Collider;
import app.domain.seat.Seat;
import app.domain.Stage;
import app.domain.selection.SelectionVisitor;
import app.domain.shape.Painter;
import app.domain.shape.Point;
import app.domain.shape.Shape;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.function.Consumer;

public final class StandingSection extends AbstractSection {
    private int max;
    private double price;

    @JsonCreator
    StandingSection(
            @JsonProperty("name") String name,
            @JsonProperty("elevation") int elevation,
            @JsonProperty("shape") Shape shape,
            @JsonProperty("max") int max,
            @JsonProperty("price") double price
    ) {
        super(name, elevation, shape);
        this.max = max;
        this.price = price;
    }

    public static StandingSection create(int max, Shape shape) {
        return new StandingSection(null, 0, shape, max,0);
    }

    @Override
    public void move(double x, double y) {
        getShape().move(x, y);
    }

    @Override
    public <T> void accept(T g, Painter<T> painter) {
        painter.draw(g, this);
    }

    @Override
    public void accept(SelectionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    @JsonIgnore
    public Seat[][] getSeats() {
        return new Seat[0][0];
    }

    @Override
    public void forEachSeats(Consumer<Seat> consumer) {}

    @Override
    public void rotate(double thetaRadian){
        getShape().rotate(thetaRadian, getShape().computeCentroid());
    }

    @Override
    public void autoSetSeats(Stage stage, Collider collider){}

    @Override
    @JsonIgnore
    public boolean isAuto(){
        return false;
    }

    public double getPrice(){
        return price;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public int getMax(){
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
