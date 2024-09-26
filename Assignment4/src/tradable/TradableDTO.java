package tradable;

import book.BookSide;
import price.Price;

public class TradableDTO {

    public String user;
    public String product;
    public Price price;
    public BookSide side;
    public String id;
    public int originalVolume;
    public int remainingVolume;
    public int cancelledVolume;
    public int filledVolume;

    public TradableDTO(String user, String product, BookSide side, Price price, String id, int originalVolume, int remainingVolume, int cancelledVolume, int filledVolume){
        this.user = user;
        this.product = product;
        this.price = price;
        this.side = side;
        this.id = id;
        this.originalVolume = originalVolume;
        this.remainingVolume = remainingVolume;
        this.cancelledVolume = cancelledVolume;
        this.filledVolume = filledVolume;
    }

    @Override
    public String toString(){
        return this.user + " order: " + this.side + " " + this.product + " at " + this.price.toString() + ", Orig Vol: " + this.originalVolume +
                ", Rem Vol: " + this.remainingVolume + ", Fill Vol: " + this.filledVolume
                + ", CXL Vol: " + this.cancelledVolume + ", ID: " + this.id;
    }
}
