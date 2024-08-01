public class Main {

    public static void main(String[] args) {

        try {
            // Price Factory Tests
            Price pPriceFactInt = PriceFactory.makePrice(569);
            System.out.println(pPriceFactInt);

            Price pPriceFactStr1 = PriceFactory.makePrice("98765");
            Price pPriceFactStr2 = PriceFactory.makePrice("000");
            Price pPriceFactStr3 = PriceFactory.makePrice(".22");
            Price pPriceFactStr4 = PriceFactory.makePrice("14.7555");
            Price pPriceFactStr5 = PriceFactory.makePrice("25.79");
            Price pPriceFactStr6 = PriceFactory.makePrice("001.76");
            Price pPriceFactStr7 = PriceFactory.makePrice("4,567.89");
            Price pPriceFactStr8 = PriceFactory.makePrice("$-12.85");
            Price pPriceFactStr9 = PriceFactory.makePrice("$-12");
            Price pPriceFactStr0 = PriceFactory.makePrice("$-.89");

            System.out.println(pPriceFactStr1);
            System.out.println(pPriceFactStr2);
            System.out.println(pPriceFactStr3);
            System.out.println(pPriceFactStr4);
            System.out.println(pPriceFactStr5);
            System.out.println(pPriceFactStr6);
            System.out.println(pPriceFactStr7);
            System.out.println(pPriceFactStr8);
            System.out.println(pPriceFactStr9);
            System.out.println(pPriceFactStr0);

            // Price Class Tests
            Price p = new Price(0);
            boolean pNeg = p.isNegative();
            System.out.println(pNeg);

            Price p2 = new Price(300);
            Price p3 = new Price(25);
            Price pAdd = p2.add(p3);
            System.out.println(pAdd);

            Price pSub = p2.subtract(p3);
            System.out.println(pSub);

            Price pMultiply = p2.multiply(2);
            System.out.println(pMultiply);

            boolean pGrOrEq = p2.greaterOrEqual(p3);
            System.out.println(pGrOrEq);

            boolean pLeOrEq = p.lessOrEqual(p3);
            System.out.println(pLeOrEq);

            Price p4 = new Price(300);
            boolean pGrTh = p2.greaterThan(p4);
            System.out.println(pGrTh);

            Price p5 = new Price(560);
            boolean pLeTh = p2.lessThan(p5);
            System.out.println(pLeTh);

            boolean pEquals = p2.equals(p4);
            System.out.println(pEquals);

            int pDiff = p5.compareTo(p4);
            System.out.println(pDiff);

            Price p6 = new Price(32110);
            String pricePrint = p6.toString();
            System.out.println(pricePrint);

            int priceHash = p6.hashCode();
            System.out.println(priceHash);

        } catch (InvalidPriceOperation e) {
            System.out.println(e.getMessage());
        }

    }
}
