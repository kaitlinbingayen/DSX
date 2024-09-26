package book;

import exceptions.DataValidationException;
import exceptions.InvalidPriceOperation;
import exceptions.InvalidValueException;
import price.Price;
import pubsub.CurrentMarketTracker;
import tradable.Quote;
import tradable.Tradable;
import tradable.TradableDTO;
import user.UserManager;

public class ProductBook {

    private final String product;
    private final ProductBookSide buySide;
    private final ProductBookSide sellSide;
    public ProductBook(String product) throws InvalidValueException {
        if (!isValidProduct(product)){
                throw new InvalidValueException("Invalid product symbol");
        }
        this.product = product;
        this.buySide = new ProductBookSide(BookSide.BUY);
        this.sellSide = new ProductBookSide(BookSide.SELL);
    }

    private boolean isValidProduct (String p) {
        return (p.length() > 1 && p.length() < 5 && p.matches("[a-zA-Z0-9.]+"));
    }

    public TradableDTO add(Tradable t) throws DataValidationException, InvalidPriceOperation {
        ProductBookSide side = (t.getSide() == BookSide.BUY ? buySide : sellSide);
        TradableDTO tradableDTO = side.add(t);
        tryTrade();
        updateMarket();
        return tradableDTO;
    }

    public TradableDTO[] add(Quote qte) throws DataValidationException {
        TradableDTO buyDTO = buySide.add(qte.getQuoteSide(BookSide.BUY));
        TradableDTO sellDTO = sellSide.add(qte.getQuoteSide(BookSide.SELL));
        tryTrade();

        return new TradableDTO[]{buyDTO, sellDTO};
    }

    public TradableDTO cancel(BookSide side, String orderId) throws DataValidationException, InvalidPriceOperation {
        TradableDTO result = null;
        if (side == BookSide.BUY) {
            result = buySide.cancel(orderId);
        } else if (side == BookSide.SELL) {
            result = sellSide.cancel(orderId);
        }
        updateMarket();
        return result;
    }

    public void tryTrade() throws DataValidationException {
        Price topBuyPrice = buySide.topOfBookPrice();
        Price topSellPrice = sellSide.topOfBookPrice();
        while (topBuyPrice != null && topSellPrice != null && topBuyPrice.compareTo(topSellPrice) >= 0) {
            int topBuyVol = buySide.topOfBookVolume();
            int topSellVol = sellSide.topOfBookVolume();

            int volTrade = Math.min(topBuyVol, topSellVol);

            sellSide.tradeOut(topSellPrice, volTrade);
            buySide.tradeOut(topBuyPrice, volTrade);
            topBuyPrice = buySide.topOfBookPrice();
            topSellPrice = sellSide.topOfBookPrice();
        }
    }

    public TradableDTO[] removeQuotesForUser(String userName) throws DataValidationException, InvalidPriceOperation {
        TradableDTO tradableDTOBuy = buySide.removeQuotesForUser(userName);
        TradableDTO tradableDTOSell = sellSide.removeQuotesForUser(userName);

        UserManager.getInstance().getUser(userName).addTradable(tradableDTOBuy);
        UserManager.getInstance().getUser(userName).addTradable(tradableDTOSell);

        updateMarket();

        return new TradableDTO[]{tradableDTOBuy, tradableDTOSell};
    }

    private void updateMarket() throws InvalidPriceOperation {
        Price topBuyPrice = buySide.topOfBookPrice();
        int topBuyVol = buySide.topOfBookVolume();

        Price topSellPrice = sellSide.topOfBookPrice();
        int topSellVol = sellSide.topOfBookVolume();

        CurrentMarketTracker.getInstance().updateMarket(product, topBuyPrice, topBuyVol, topSellPrice, topSellVol);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Product Book: ").append(product).append("\n");
        sb.append("\t").append(buySide.toString());
        sb.append("\t").append(sellSide.toString());
        return sb.toString();
    }
}
