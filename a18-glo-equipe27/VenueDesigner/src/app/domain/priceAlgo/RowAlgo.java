package app.domain.priceAlgo;

import app.domain.seat.Seat;
import app.domain.section.Section;
import app.domain.shape.Point;
import app.domain.shape.ShapeUtils;

import java.util.ArrayList;
import java.util.List;

public final class RowAlgo extends PriceAlgoAbstract{
    @Override
    public void extremeDistribution(List<Section> sections, Point stageCenter, double minPrice, double maxPrice) {
        ArrayList<Double> distances = new ArrayList<>();
        ArrayList<Seat[]> rows = new ArrayList<>();

        double minDist = Double.MAX_VALUE;
        double maxDist = Double.MIN_VALUE;

        for (Section section: sections){
            for (Seat[] row: section.getSeats()){
                double x = row[0].getShape().computeCentroid().x+(row[row.length-1].getShape().computeCentroid().x-
                        row[0].getShape().computeCentroid().x)/2;
                double y = row[0].getShape().computeCentroid().y+(row[row.length-1].getShape().computeCentroid().y-
                        row[0].getShape().computeCentroid().y)/2;
                Point rowCenter = new Point(x,y);
                double distance = ShapeUtils.distance(rowCenter, stageCenter);
                rows.add(row);
                distances.add(distance);
                minDist = Math.min(minDist, distance);
                maxDist = Math.max(maxDist,distance);
            }
        }

        for (int i = 0; i < distances.size(); i++) {
            double rowPrice = computePrice(distances.get(i), minDist, maxDist, minPrice, maxPrice);
            int k = (int) computePrice(distances.get(i), minDist, maxDist,0,255);
            for (Seat seat: rows.get(i)) {
                seat.setPrice(rowPrice);
                int[] color = {k, 0, 0, 255};
                seat.getShape().setColor(color);
            }
        }
    }
}
