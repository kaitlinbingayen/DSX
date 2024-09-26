package user;

import exceptions.InvalidValueException;
import pubsub.CurrentMarketObserver;
import pubsub.CurrentMarketSide;
import tradable.TradableDTO;

import java.util.HashMap;

public class User implements CurrentMarketObserver {

    private String userId;
    private HashMap<String, TradableDTO> tradables;
    private HashMap<String, CurrentMarketSide[]> currentMarkets;

    public User (String userId) throws InvalidValueException{
        setUserId(userId);
        this.tradables = new HashMap<>();
        this.currentMarkets = new HashMap<>();
    }

    private void setUserId(String u) throws InvalidValueException{
        if (u.length() != 3 || !u.matches("[a-zA-Z]+")) {
            throw new InvalidValueException("User must be a 3-letter code");
        }
        this.userId = u;
    }
    public String getUserId(){
        return this.userId;
    }

    public void addTradable(TradableDTO o){
        if (o != null){
            tradables.put(o.id, o);
        }
    }

    public boolean hasTradableWithRemainingQty(){
        for (TradableDTO tradable : tradables.values()){
            if (tradable.remainingVolume > 0){
                return true;
            }
        }
        return false;
    }

    public TradableDTO getTradableWithRemainingQty(){
        for (TradableDTO tradable : tradables.values()){
            if (tradable.remainingVolume > 0){
                return tradable;
            }
        }
        return null;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("User Id: ").append(userId).append("\n");
        for (TradableDTO tradable : tradables.values()){
            sb.append("\t").append("Product: ").append(tradable.product)
                    .append(", Price: ").append(tradable.price)
                    .append(", OriginalVolume: ").append(tradable.originalVolume)
                    .append(", RemainingVolume: ").append(tradable.remainingVolume)
                    .append(", CancelledVolume: ").append(tradable.cancelledVolume)
                    .append(", FilledVolume: ").append(tradable.filledVolume)
                    .append(", User: ").append(tradable.user)
                    .append(", Side: ").append(tradable.side)
                    .append(", Id: ").append(tradable.id)
                    .append("\n");
        }
        return sb.toString();
    }

    @Override
    public void updateCurrentMarket(String symbol, CurrentMarketSide buySide, CurrentMarketSide sellSide) {
        CurrentMarketSide[] marketSides = new CurrentMarketSide[]{buySide, sellSide};
        currentMarkets.put(symbol, marketSides);
    }
    public String getCurrentMarkets(){
        StringBuilder summary = new StringBuilder();
        for (String symbol : currentMarkets.keySet()){
            CurrentMarketSide[] marketSides = currentMarkets.get(symbol);
            CurrentMarketSide buySide = marketSides[0];
            CurrentMarketSide sellSide = marketSides[1];
            summary.append(String.format("%s %s - %s\n", symbol, buySide, sellSide));
        }
        return summary.toString();
    }
}
