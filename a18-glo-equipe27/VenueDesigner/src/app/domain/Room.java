package app.domain;

import app.domain.section.Section;
import app.domain.shape.Painter;
import app.domain.shape.Rectangle;
import app.domain.shape.Shape;
import com.fasterxml.jackson.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class Room implements Drawable {
    private final ArrayList<Section> sections;
    private final ArrayList<Offer> offers;

    private Shape shape;
    private double width;
    private double height;
    private VitalSpace vitalSpace;
    @JsonProperty
    private Stage stage;


    public Room(double width, double height, VitalSpace vitalSpace) {
        this.sections = new ArrayList<>();
        this.offers = new ArrayList<>();
        this.shape = Rectangle.create(0, 0, width, height, new int[]{20, 38, 52, 255});
        this.width = width;
        this.height = height;
        this.vitalSpace = Objects.requireNonNull(vitalSpace);
    }

    @JsonCreator
    public Room(
            @JsonProperty("sections") ArrayList<Section> sections,
            @JsonProperty("offers") ArrayList<Offer> offers,
            @JsonProperty("shape") Shape shape,
            @JsonProperty("width") double width,
            @JsonProperty("height") double height,
            @JsonProperty("vitalSpace") VitalSpace vitalSpace,
            @JsonProperty("stage") Stage stage
    ) {
        this.sections = Objects.requireNonNull(sections);
        this.offers = Objects.requireNonNull(offers);
        this.shape = shape;
        this.width = width;
        this.height = height;
        this.vitalSpace = Objects.requireNonNull(vitalSpace);
        this.stage =stage;
    }

    public void setDimensions(double width, double height) {
        this.width = width;
        this.height = height;
        this.shape = Rectangle.create(0, 0, width, height, new int[]{20, 38, 52, 255});
    }

    public double getWidth() { return this.width; }

    public double getHeight() { return this.height; }

    public void setWidth(double width) { this.width = width; }

    public void setHeight(double height) { this.height = height; }

    public VitalSpace getVitalSpace() { return vitalSpace; }

    public Shape getShape() {
        return shape;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @JsonIgnore
    public Optional<Stage> getStage() { return Optional.ofNullable(stage); }

    public void addSection(Section section) {
        sections.add(Objects.requireNonNull(section));
    }

    public List<Section> getSections() {
        return sections;
    }

    @Override
    public <T> void accept(T g, Painter<T> painter) {
        painter.draw(g, this);
    }

    @JsonIgnore
    public boolean isStageSet(){
        return !(stage ==  null);
    }

    public List<Offer> getOffers() {
        return offers;
    }
}
