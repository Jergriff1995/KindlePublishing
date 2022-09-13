package com.amazon.stock;

import java.math.BigDecimal;

public class BuyStockResponse {
    private final String symbol;
    private final BigDecimal price;
    private final int quantity;

    private BuyStockResponse(String symbol, BigDecimal price, int quantity) {
        this.symbol = symbol;
        this.price = price;
        this.quantity = quantity;
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String symbol;
        private BigDecimal price;
        private int quantity;

        public Builder withSymbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Builder withPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder withQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public BuyStockResponse build() {
            return new BuyStockResponse(symbol, price, quantity);
        }
    }
}
