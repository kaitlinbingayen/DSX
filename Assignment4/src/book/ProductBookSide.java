package book;

import exceptions.DataValidationException;
import exceptions.InvalidValueException;
import price.Price;
import tradable.Tradable;
import tradable.TradableDTO;
import user.UserManager;

import java.util.*;

public class ProductBookSide {

    private BookSide side;
    private final HashMap<Price, ArrayList<Tradable>> bookEntries;
    public ProductBookSide(BookSide side) throws InvalidValueException{
        setSide(side);
        this.bookEntries = new HashMap<>();
    }

    private void setSide(BookSide side) throws InvalidValueException{
        if (side == null){
            throw new InvalidValueException("Invalid book side");
        }
        this.side = side;
    }
    public TradableDTO add(Tradable o){
        Price price = o.getPrice();
        if (!bookEntries.containsKey(price)){
            bookEntries.put(price, new ArrayList<Tradable>());
        }
        bookEntries.get(price).add(o);
        return new TradableDTO(o.getUser(), o.getProduct(), o.getSide(), o.getPrice(), o.getId(), o.getOriginalVolume(), o.getRemainingVolume(), o.getCancelledVolume(), o.getFilledVolume());
    }

    public TradableDTO cancel(String tradableId) throws DataValidationException{
        for (ArrayList<Tradable> tradablesAtPrice : bookEntries.values()){
            for (Tradable tradable : tradablesAtPrice){
                if (tradable.getId().equals(tradableId)) {
                    tradable.setCancelledVolume(tradable.getCancelledVolume() + tradable.getRemainingVolume());
                    tradable.setRemainingVolume(0);
                    tradablesAtPrice.remove(tradable);

                    if (tradablesAtPrice.isEmpty()) {
                        bookEntries.values().remove(tradablesAtPrice);
                    }

                    UserManager.getInstance().addToUser(tradable.getUser(), tradable.makeTradableDTO());
                    return new TradableDTO(
                            tradable.getUser(),
                            tradable.getProduct(),
                            tradable.getSide(),
                            tradable.getPrice(),
                            tradable.getId(),
                            tradable.getOriginalVolume(),
                            tradable.getRemainingVolume(),
                            tradable.getCancelledVolume(),
                            tradable.getFilledVolume()
                    );
                }
            }
        }
        return null;
    }

    public TradableDTO removeQuotesForUser(String userName) throws DataValidationException{
        for (Price price : bookEntries.keySet()){
            ArrayList<Tradable> tradablesAtPrice = bookEntries.get(price);
            for (Tradable tradable : tradablesAtPrice){
                if (tradable.getUser().equals(userName)){
                    TradableDTO cancelledDTO = cancel(tradable.getId());

                    if (tradablesAtPrice.isEmpty()){
                        bookEntries.remove(price);
                    }
                    return cancelledDTO;
                }
            }
        }
        return null;
    }

    public Price topOfBookPrice(){
        if (bookEntries.isEmpty()){
            return null;
        }

        if (side == BookSide.BUY){
            return Collections.max(bookEntries.keySet());
        } else if (side == BookSide.SELL) {
            return Collections.min(bookEntries.keySet());
        }
        return null;
    }

    public int topOfBookVolume(){
        if (bookEntries.isEmpty()){
            return 0;
        }
        Price topPrice;
        if (side == BookSide.BUY){
            topPrice = Collections.max(bookEntries.keySet());
        } else if (side == BookSide.SELL) {
            topPrice = Collections.min(bookEntries.keySet());
        } else {
            return 0;
        }

        int totalVolume = 0;
        for (Tradable tradable: bookEntries.get(topPrice)){
            totalVolume += tradable.getRemainingVolume();
        }
        return totalVolume;
    }
    public void tradeOut(Price price, int vol) throws DataValidationException {
        int remainingVol = vol;
        ArrayList<Tradable> orders = bookEntries.getOrDefault(price, new ArrayList<>());

        while (remainingVol > 0) {
            for (Tradable order : orders){
                if (order.getRemainingVolume() <= remainingVol) {
                    order.setFilledVolume(order.getFilledVolume() + order.getRemainingVolume());
                    order.setRemainingVolume(0);
                    remainingVol -= order.getFilledVolume();
                    System.out.println(String.format("FULL FILL: (%s %d) %s", side, order.getFilledVolume(), order));
                    UserManager.getInstance().addToUser(order.getUser(), order.makeTradableDTO());
                } else {
                    order.setFilledVolume(order.getFilledVolume() + remainingVol);
                    order.setRemainingVolume(order.getRemainingVolume() - remainingVol);
                    System.out.println(String.format("PARTIAL FILL: (%s %d) %s", side, order.getFilledVolume(), order));
                    remainingVol = 0;
                    UserManager.getInstance().addToUser(order.getUser(), order.makeTradableDTO());
                }
            }
        }
        orders.removeIf(order -> order.getRemainingVolume() == 0);
        if (orders.isEmpty()){
            bookEntries.remove(price);
        } else {
            bookEntries.put(price, orders);
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Side: ").append(side).append("\n");

        List<Price> sortedPrices = new ArrayList<>(bookEntries.keySet());
        if (side == BookSide.BUY){
            sortedPrices.sort(Collections.reverseOrder());
        } else {
            sortedPrices.reversed();
        }

        for (Price price : sortedPrices){
            sb.append("\t\t").append("Price: ").append(price).append("\n");
            for (Tradable t : bookEntries.get(price)){
                sb.append("\t\t\t").append(t.toString()).append("\n");
            }
        }

        return sb.toString();
    }

}
