package pubsub;

public interface CurrentMarketObserver {
    public void updateCurrentMarket(String symbol, CurrentMarketSide buySide, CurrentMarketSide sellSide);
}
