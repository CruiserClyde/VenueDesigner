package app.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class VitalSpace {
    private double width;
    private double height;

    @JsonCreator
    public VitalSpace(
            @JsonProperty("width") double width,
            @JsonProperty("height") double height
    ) {
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public VitalSpace clone() {
        return new VitalSpace(width, height);
    }
}
