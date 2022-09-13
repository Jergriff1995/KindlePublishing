package com.amazon.stock;

public class BuyStockRequest {
    private final String symbol;
    private final int quantity;

    private BuyStockRequest(String symbol, int quantity) {
        this.symbol = symbol;
        this.quantity = quantity;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String symbol;
        private int quantity;

        public Builder withSymbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Builder withQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public BuyStockRequest build() {
            return new BuyStockRequest(symbol, quantity);
        }
    }
}
