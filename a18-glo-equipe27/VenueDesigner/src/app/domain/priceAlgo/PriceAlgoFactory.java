package app.domain.priceAlgo;

public final class PriceAlgoFactory {
    public static PriceAlgo create(AlgoType type) {
        if (type==AlgoType.Seat){
            return new SeatAlgo();
        } else if (type==AlgoType.Row) {
            return new RowAlgo();
        }
        return new SectionAlgo();
    }
}
