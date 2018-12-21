package app.domain.section;

import app.domain.shape.Shape;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SeatedSection.class, name = "SeatedSection"),
        @JsonSubTypes.Type(value = StandingSection.class, name = "StandingSection")
})
abstract class AbstractSection implements Section {
    private String name;
    private double elevation;
    private Shape shape;

    AbstractSection(String name, int elevation, Shape shape) {
        this.name = name;
        this.elevation = elevation;
        this.shape = shape;
    }

    @Override
    public String getName() {
        return name == null ? "" : name;
    }

    @Override
    public void setName(String name) {
        this.name = name == null ? "" : name;
    }

    @Override
    public double getElevation() {
        return elevation;
    }

    @Override
    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    @Override
    public Shape getShape() {
        return shape;
    }

    @Override
    public void setShape(Shape shape) {
        this.shape = shape;
    }

    @Override
    @JsonIgnore
    public boolean isSelected() {
        return shape.isSelected();
    }

    @Override
    @JsonIgnore
    public void setSelected(boolean selected) {
        shape.setSelected(selected);
    }
}
