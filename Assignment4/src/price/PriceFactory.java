package price;

import java.util.HashMap;

public abstract class PriceFactory {

    private static final HashMap<Integer, Price> priceCache = new HashMap<>();

    public static Price makePrice(int value){
        if (priceCache.containsKey(value)){
            return priceCache.get(value);
        }
        Price price = new Price (value);
        priceCache.put(value, price);
        return price;
    }

    public static Price makePrice (String stringValueIn) {
        String cleanedString = stringValueIn.replace("$", "").replace(",", "").replace("-", "");

        String[] parts = cleanedString.split("\\.");

        int dollars = 0;
        int cents = 0;

        if (parts.length > 0 && !parts[0].isEmpty()) {
            dollars = Integer.parseInt(parts[0]);
        }

        if (parts.length > 1 && !parts[1].isEmpty()) {
            String centsString = parts[1];
            if (centsString.length() > 2) {
                centsString = centsString.substring(0, 2);
            }
            cents = Integer.parseInt(centsString);
        }

        int centsValue = dollars * 100 + cents;

        if (stringValueIn.contains("-")) {
            centsValue *= -1;
        }

        if (priceCache.containsKey(centsValue)){
            return priceCache.get(centsValue);
        }

        Price price = new Price(centsValue);
        priceCache.put(centsValue, price);
        return price;
    }
}
