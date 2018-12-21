package app.domain.shape;

import app.domain.Mode;

public final class ShapeBuilderFactory {
    public static ShapeBuilder create(Mode mode) {
        switch (mode) {
            case Stage:
                return new Rectangle.Builder(97, 36, 5, 255);
            case IrregularSeatedSection:
                return new Polygon.Builder(63, 63, 76, 255);
            case IrregularStandingSection:
                return new Polygon.Builder(63, 63, 76, 255);
        }
        throw new RuntimeException();
    }
}
