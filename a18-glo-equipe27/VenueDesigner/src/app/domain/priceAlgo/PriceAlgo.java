package app.domain.priceAlgo;

import app.domain.section.Section;
import app.domain.shape.Point;
import java.util.List;

public interface PriceAlgo {
    void extremeDistribution(List<Section> sections, Point stageCenter, double min, double max);
}
