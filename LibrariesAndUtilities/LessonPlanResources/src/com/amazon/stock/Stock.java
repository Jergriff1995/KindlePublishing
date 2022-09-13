package com.amazon.stock;

import java.util.Objects;

/**
 * A type of investment that represents an ownership share in a company.
 */
public class Stock {
    private String symbol;
    private String name;

    public Stock(String symbol, String name) {
        this.symbol = symbol;
        this.name = name;
    }

    /**
     * A unique series of letters assigned to a stock for trading purposes.
     * @return The unique identifier of this stock
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * The name of the company that this stock represents ownership of.
     * @return Company name
     */
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Stock stock = (Stock) o;
        return Objects.equals(symbol, stock.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }
}
