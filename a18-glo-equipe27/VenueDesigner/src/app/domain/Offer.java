package app.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public final class Offer {
    public enum DiscountMode {
        Dollar,
        Percent
    }

    private String name;
    private DiscountMode mode;
    private int discount;

    @JsonCreator
    public Offer(
            @JsonProperty("name") String name,
            @JsonProperty("mode") DiscountMode mode,
            @JsonProperty("discount") int discount
    ) {
        this.name = Objects.requireNonNull(name);
        this.mode = Objects.requireNonNull(mode);
        this.discount = discount;
    }

    public double Discount(String discountMode, int originPrice,int valueDiscount ){
        double p;
        if(discountMode.equals("%")){
            double price = (double) Math.round(((double)originPrice - ((valueDiscount/100f)*originPrice)) * 100) / 100; // 9.456 --> 9.46;
            if(price < 0){
                p = 0;
            }else{
                p = price;
            }
        }else{
            double price = (double) Math.round(((double)originPrice - valueDiscount) * 100) / 100;
            if (price < 0) {
                p = 0;
            }else{
                p = price;
            }
        }
        return p;
    }


    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public DiscountMode getMode() {
        return mode;
    }

    public void setMode(DiscountMode mode) {
        this.mode = Objects.requireNonNull(mode);
    }

    public int getDiscount(){
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return discount == offer.discount &&
                Objects.equals(name, offer.name) &&
                mode == offer.mode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, mode, discount);
    }
}
