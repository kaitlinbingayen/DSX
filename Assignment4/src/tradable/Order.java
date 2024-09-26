package tradable;

import book.BookSide;
import exceptions.InvalidPriceOperation;
import exceptions.InvalidValueException;
import price.Price;

public class Order implements Tradable {

    private String user;
    private String product;
    private Price price;
    private BookSide side;
    private final String id;
    private int originalVolume;
    private int remainingVolume;
    private int cancelledVolume;
    private int filledVolume;

    public Order (String user, String product, Price price, int originalVolume, BookSide side) throws InvalidValueException, InvalidPriceOperation {
        setUser(user);
        setProduct(product);
        setPrice(price);
        setOriginalVolume(originalVolume);
        setBookSide(side);
        setRemainingVolume(originalVolume);
        setCancelledVolume(0);
        setFilledVolume(0);

        this.id = user + product + price + System.nanoTime();
    }

    private void setUser(String u) throws InvalidValueException {
        if (u.length() != 3 || !u.matches("[a-zA-Z]+")) {
            throw new InvalidValueException("User must be a 3-letter code");
        }
        this.user = u;
    }

    private void setProduct(String p) throws InvalidValueException {
        if (p.length() < 1 || p.length() > 5 || !p.matches("[a-zA-Z0-9.]+")){
            throw new InvalidValueException("Invalid stock symbol");
        }
        this.product = p;
    }

    private void setPrice(Price pr) throws InvalidPriceOperation {
        if (pr == null){
            throw new InvalidPriceOperation("Invalid null price");
        }
        this.price = pr;
    }

    private void setBookSide (BookSide s) throws InvalidValueException {
        if (s == null) {
            throw new InvalidValueException("Invalid null side");
        }
        this.side = s;
    }

    private void setOriginalVolume(int ov) throws InvalidValueException {
        if (ov < 0 || ov > 10000){
            throw new InvalidValueException("Invalid quantity of stock");
        }
        this.originalVolume = ov;
    }
    @Override
    public String getId(){
        return this.id;
    }
    @Override
    public int getCancelledVolume() {
        return this.cancelledVolume;
    }
    @Override
    public void setCancelledVolume(int newVol){
        cancelledVolume = newVol;
    }
    @Override
    public int getRemainingVolume(){
        return this.remainingVolume;
    }
    @Override
    public void setRemainingVolume(int newVol) {
        remainingVolume = newVol;
    }

    @Override
    public TradableDTO makeTradableDTO() {
        return new TradableDTO(user, product, side, price, id, originalVolume, remainingVolume, cancelledVolume, filledVolume);
    }

    @Override
    public Price getPrice() {
        return price;
    }

    @Override
    public void setFilledVolume(int newVol) {
        filledVolume = newVol;
    }

    @Override
    public int getFilledVolume() {
        return this.filledVolume;
    }

    public BookSide getSide() {
        return this.side;
    }

    @Override
    public String getUser() {
        return this.user;
    }

    @Override
    public String getProduct() {
        return this.product;
    }

    @Override
    public int getOriginalVolume() {
        return this.originalVolume;
    }

    @Override
    public String toString(){
        return this.user + " order: " + this.side + " " + this.product + " at " + this.price.toString() + ", Orig Vol: " + this.originalVolume +
                ", Rem Vol: " + this.remainingVolume + ", Fill Vol: " + this.filledVolume
                + ", CXL Vol: " + this.cancelledVolume + ", ID: " + this.id;
    }
}
