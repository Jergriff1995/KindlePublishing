package com.amazon.stock;

import java.math.BigDecimal;

public class StockPriceResponse {
    private final String symbol;
    private final BigDecimal price;

    private StockPriceResponse(String symbol, BigDecimal price) {
        this.symbol = symbol;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String symbol;
        private BigDecimal price;

        public Builder withSymbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Builder withPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public StockPriceResponse build() {
            return new StockPriceResponse(symbol, price);
        }
    }
}
