package tradable;

import book.BookSide;
import exceptions.InvalidPriceOperation;
import exceptions.InvalidValueException;
import price.Price;

public class Quote {
    private final String user;
    private final String product;
    private final QuoteSide buySide;
    private final QuoteSide sellSide;

    public Quote (String symbol, Price buyPrice, int buyVolume, Price sellPrice, int sellVolume, String userName) throws InvalidValueException, InvalidPriceOperation {
        if (!isValidSymbol(symbol)){
            throw new InvalidValueException("Invalid stock symbol");
        }
        if (!isValidUser(userName)){
            throw new InvalidValueException("Invalid user");
        }
        this.product = symbol;
        this.user = userName;

        this.buySide = new QuoteSide(userName, symbol, BookSide.BUY, buyPrice, buyVolume);
        this.sellSide = new QuoteSide(userName, symbol, BookSide.SELL, sellPrice, sellVolume);
    }

    private boolean isValidSymbol(String symbol){
        return symbol != null && symbol.matches("[A-Za-z0-9.]+") && symbol.length() >= 1 && symbol.length() <= 5;
    }
    private boolean isValidUser(String user){
        return user != null && user.matches("[A-Za-z]+") && user.length() == 3;
    }
    public QuoteSide getQuoteSide(BookSide sideIn) {
        if (sideIn == BookSide.BUY) {
            return buySide;
        }
        return sellSide;
    }

    public String getSymbol(){
        return product;
    }

    public String getUser(){
        return user;
    }
}
