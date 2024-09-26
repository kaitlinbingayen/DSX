package price;

import exceptions.InvalidPriceOperation;

import java.util.Objects;

public class Price implements Comparable<Price>{

    private final int cents;

    public Price(int value) {
        this.cents = value;
    }

    public boolean isNegative(){
        return this.cents < 0;
    }

    public Price add(Price p) throws InvalidPriceOperation {
        if (p == null){
            throw new InvalidPriceOperation("Cannot add null value");
        }
        return new Price(this.cents + p.cents);
    }

    public Price subtract(Price p) throws InvalidPriceOperation {
        if (p == null){
            throw new InvalidPriceOperation("Cannot subtract null value");
        }
        return new Price(this.cents - p.cents);
    }

    public Price multiply(int n) {
        return new Price(this.cents * n);
    }

    public boolean greaterOrEqual(Price p) throws InvalidPriceOperation{
        if (p == null){
            throw new InvalidPriceOperation("Cannot compare null value");
        }
        return this.cents >= p.cents;
    }

    public boolean lessOrEqual(Price p) throws InvalidPriceOperation{
        if (p == null){
            throw new InvalidPriceOperation("Cannot compare null value");
        }
        return this.cents <= p.cents;
    }

    public boolean greaterThan(Price p) throws InvalidPriceOperation{
        if (p == null){
            throw new InvalidPriceOperation("Cannot compare null value");
        }
        return this.cents > p.cents;
    }

    public boolean lessThan(Price p) throws InvalidPriceOperation {
        if (p == null){
            throw new InvalidPriceOperation("Cannot compare null value");
        }
        return this.cents < p.cents;
    }

    @Override
    public boolean equals(Object p) {
        if (this == p) return true;
        if (p == null || getClass() != p.getClass()) return false;
        Price price = (Price) p;
        return cents == price.cents;
    }

    @Override
    public int compareTo(Price p) {
        return this.cents - p.cents;
    }

    @Override
    public String toString() {
        double value = this.cents / 100.0;
        return String.format("$%,.2f", value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cents);
    }

}
