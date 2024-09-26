package book;

import exceptions.DataValidationException;
import exceptions.InvalidPriceOperation;
import exceptions.InvalidValueException;
import tradable.Quote;
import tradable.Tradable;
import tradable.TradableDTO;
import user.UserManager;

import java.util.HashMap;
import java.util.Random;

public final class ProductManager {

    private static ProductManager instance;
    private HashMap<String, ProductBook> productBooks;

    private ProductManager() {
        productBooks = new HashMap<>();
    }

    public static ProductManager getInstance(){
        if (instance == null){
            instance = new ProductManager();
        }
        return instance;
    }

    public void addProduct(String symbol) throws DataValidationException, InvalidValueException {
        if (symbol == null | symbol.length() < 1 | symbol.length() > 5 | !symbol.matches("[a-zA-Z0-9.]+")){
            throw new DataValidationException("Invalid symbol");
        }
        ProductBook pb = new ProductBook(symbol);
        productBooks.put(symbol, pb);
    }

    public ProductBook getProductBook(String symbol) throws DataValidationException{
        if (!productBooks.containsKey(symbol)){
            throw new DataValidationException(String.format("No %s products exist", symbol));
        }
        return productBooks.get(symbol);
    }

    public String getRandomProduct() throws DataValidationException {
        if (productBooks.isEmpty()){
            throw new DataValidationException("Product Book is empty");
        }
        Object[] pbArray = productBooks.values().toArray();
        return (String) pbArray[new Random().nextInt(pbArray.length)];
    }

    public TradableDTO addTradable(Tradable o) throws DataValidationException, InvalidPriceOperation {
        if (o == null){
            throw new DataValidationException("Tradable cannot be null");
        }

        ProductBook book = getProductBook(o.getProduct());
        TradableDTO tradableDTO = book.add(o);

        UserManager userMgr = UserManager.getInstance();
        TradableDTO tDTO = o.makeTradableDTO();
        userMgr.addToUser(o.getUser(), tDTO);

        return tradableDTO;
    }

    public TradableDTO[] addQuote(Quote q) throws DataValidationException, InvalidPriceOperation {
        if (q == null){
            throw new DataValidationException("Quote cannot be null");
        }
        ProductBook book = getProductBook(q.getSymbol());
        book.removeQuotesForUser(q.getUser());

        TradableDTO buyTradableDTO = ProductManager.getInstance().addTradable(q.getQuoteSide(BookSide.BUY));
        TradableDTO sellTradableDTO = ProductManager.getInstance().addTradable(q.getQuoteSide(BookSide.SELL));

        return new TradableDTO[]{buyTradableDTO, sellTradableDTO};
    }

    public TradableDTO cancel(TradableDTO o) throws DataValidationException, InvalidPriceOperation {
        if (o.product == null){
            throw new DataValidationException("Product does not exist for the specified symbol");
        }
        ProductBook pb = getProductBook(o.product);
        TradableDTO cancelledDTO = pb.cancel(o.side, o.id);

        if (cancelledDTO == null) {
            System.out.println("Cancel failed");
            return null;
        }
        return cancelledDTO;
    }

    public TradableDTO[] cancelQuote(String symbol, String user) throws DataValidationException, InvalidPriceOperation {
        if (symbol == null | user == null ){
            throw new DataValidationException("Symbol and user cannot be null");
        }
        ProductBook pb = getProductBook(symbol);
        if (pb == null){
            throw new DataValidationException("Product does not exist for the specified symbol");
        }

        return pb.removeQuotesForUser(user);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (ProductBook book : productBooks.values()){
            sb.append(book.toString()).append("\n");
        }
        return sb.toString();
    }
}
