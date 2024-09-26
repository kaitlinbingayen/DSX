package pubsub;

import exceptions.InvalidPriceOperation;
import price.Price;
import price.PriceFactory;

public final class CurrentMarketTracker {
    private static CurrentMarketTracker instance;

    public static CurrentMarketTracker getInstance(){
        if (instance == null){
            instance = new CurrentMarketTracker();
        }
        return instance;
    }

    private CurrentMarketTracker(){}

    public void updateMarket(String symbol, Price buyPrice, int buyVolume, Price sellPrice, int sellVolume) throws InvalidPriceOperation {
        Price marketWidth;
        if (sellPrice == null | buyPrice == null){
            marketWidth = PriceFactory.makePrice(0);
        } else {
            marketWidth = sellPrice.subtract(buyPrice);
        }

        CurrentMarketSide buySide = new CurrentMarketSide(buyPrice, buyVolume);
        CurrentMarketSide sellSide = new CurrentMarketSide(sellPrice, sellVolume);

        System.out.println("*********** Current Market ***********");
        System.out.printf("* %s \t%s - %s [%s] \n", symbol, buySide, sellSide, marketWidth);
        System.out.println("**************************************");

        CurrentMarketPublisher.getInstance().acceptCurrentMarket(symbol, buySide, sellSide);
    }
}
