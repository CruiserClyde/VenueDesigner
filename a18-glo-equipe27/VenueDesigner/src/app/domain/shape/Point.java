package app.domain.shape;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Point {
    public double x;
    public double y;

    @JsonCreator
    public Point(
            @JsonProperty("x") double x,
            @JsonProperty("y") double y
    ) {
        this.x = x;
        this.y = y;
    }

    public Point() {
        this(0, 0);
    }

    public Point(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void offset(double x, double y) {
        this.x += x;
        this.y += y;
    }
}
