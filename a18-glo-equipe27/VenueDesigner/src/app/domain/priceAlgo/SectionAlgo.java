package app.domain.priceAlgo;

import app.domain.seat.Seat;
import app.domain.section.Section;
import app.domain.shape.Point;
import app.domain.shape.ShapeUtils;

import java.util.ArrayList;
import java.util.List;

public final class SectionAlgo extends PriceAlgoAbstract{
    @Override
    public void extremeDistribution(List<Section> sections, Point stageCenter, double minPrice, double maxPrice) {
        ArrayList<Double> distances = new ArrayList<>();

        double minDist = Double.MAX_VALUE;
        double maxDist = Double.MIN_VALUE;

        for (Section section: sections) {
            Point sectionCenter = section.getShape().computeCentroid();
            double distance = ShapeUtils.distance(sectionCenter,stageCenter);
            distances.add(distance);
            minDist = Math.min(minDist, distance);
            maxDist = Math.max(maxDist,distance);
        }

        for (int i = 0; i < distances.size(); i++) {
            double sectionPrice = computePrice(distances.get(i), minDist, maxDist, minPrice, maxPrice);
            int k = (int) computePrice(distances.get(i),minDist,maxDist,0,255);
            for (Seat[] rows: sections.get(i).getSeats()) {
                for (Seat seat: rows){
                    seat.setPrice(sectionPrice);
                    int[] color = {k,0,0,255};
                    seat.getShape().setColor(color);
                }
            }
        }
    }
}
