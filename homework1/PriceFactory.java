public abstract class PriceFactory {

    public static Price makePrice(int value) {
        return new Price (value);
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
            // truncate extra cents digits
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

        return new Price(centsValue);
    }
}
