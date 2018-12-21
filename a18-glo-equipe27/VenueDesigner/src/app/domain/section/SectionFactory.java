package app.domain.section;

import app.domain.Mode;
import app.domain.VitalSpace;
import app.domain.shape.Shape;

public final class SectionFactory {
    public static Section create(Mode mode, Shape shape, VitalSpace vs) {
        switch (mode) {
            case IrregularSeatedSection:
                return new SeatedSection("", 0, shape, vs);
            case RegularStandingSection:
                return new StandingSection("",0, shape,10, 0);
            case IrregularStandingSection:
                return new StandingSection("",0, shape,10, 0);
        }
        throw new RuntimeException();
    }
}
