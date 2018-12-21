package app.domain.priceAlgo;

import app.domain.seat.Seat;
import app.domain.section.Section;
import app.domain.shape.Point;
import app.domain.shape.ShapeUtils;

import java.util.ArrayList;
import java.util.List;

public final class SeatAlgo extends PriceAlgoAbstract {
    @Override
    public void extremeDistribution(List<Section> sections, Point stageCenter, double minPrice, double maxPrice) {
        ArrayList<Double> distances = new ArrayList<>();
        ArrayList<Seat> seatArray = new ArrayList<>();

        double minDist = Double.MAX_VALUE;
        double maxDist = Double.MIN_VALUE;

        for (Section section: sections){
            Seat[][] seats=section.getSeats();
            for (Seat[] row: seats){
                for (Seat seat: row){
                    Point seatCenter = seat.getShape().computeCentroid();
                    double distance = ShapeUtils.distance(seatCenter, stageCenter);
                    seatArray.add(seat);
                    distances.add(distance);
                    minDist = Math.min(minDist, distance);
                    maxDist = Math.max(maxDist,distance);
                }
            }
        }

        for (int i = 0; i < distances.size(); i++) {
            double seatPrice = computePrice(distances.get(i), minDist, maxDist, minPrice, maxPrice);
            seatArray.get(i).setPrice(seatPrice);
            int k = (int) computePrice(distances.get(i),minDist,maxDist,0,255);// on utilise cette fct pour obtenir la couleur
            int[] color = {0,0,k,255};
            seatArray.get(i).getShape().setColor(color);
        }

    }
}
