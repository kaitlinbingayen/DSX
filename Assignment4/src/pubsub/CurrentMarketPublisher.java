package pubsub;

import java.util.ArrayList;
import java.util.HashMap;

public final class CurrentMarketPublisher {

    private static CurrentMarketPublisher instance;
    private HashMap<String, ArrayList<CurrentMarketObserver>> filters;

    private CurrentMarketPublisher(){
        this.filters = new HashMap<>();
    }
    public static CurrentMarketPublisher getInstance(){
        if (instance == null){
            instance = new CurrentMarketPublisher();
        }
        return instance;
    }
    public void subscribeCurrentMarket(String symbol, CurrentMarketObserver cmo){
        if (!filters.containsKey(symbol)){
            filters.put(symbol, new ArrayList<>());
        }
        ArrayList<CurrentMarketObserver> observers = filters.get(symbol);
        observers.add(cmo);
    }

    public void unSubscribeCurrentMarket(String symbol, CurrentMarketObserver cmo){
        if (!filters.containsKey(symbol)){
            return;
        }
        ArrayList<CurrentMarketObserver> observers = filters.get(symbol);
        observers.remove(cmo);
    }

    public void acceptCurrentMarket(String symbol, CurrentMarketSide buySide, CurrentMarketSide sellSide){
        if (!filters.containsKey(symbol)){
            return;
        }
        ArrayList<CurrentMarketObserver> observers = filters.get(symbol);
        for (CurrentMarketObserver observer : observers){
            observer.updateCurrentMarket(symbol, buySide, sellSide);
        }
    }
}
