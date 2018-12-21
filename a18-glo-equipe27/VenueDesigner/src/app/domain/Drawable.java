package app.domain;

import app.domain.shape.Painter;

public interface Drawable {
    <T> void accept(T g, Painter<T> painter);
}
