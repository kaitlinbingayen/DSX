package pubsub;

import exceptions.InvalidPriceOperation;
import price.Price;
import price.PriceFactory;

public class CurrentMarketSide {
    private Price price;
    private final int volume;

    public CurrentMarketSide(Price price, int vol) throws InvalidPriceOperation {
        setPrice(price);
        this.volume = vol;
    }

    private void setPrice(Price p) throws InvalidPriceOperation {
        if (p == null) {
            p = PriceFactory.makePrice(0);
        }
        this.price = p;
    }

    public String toString(){
        return price.toString() + "x" + volume;
    }
}
