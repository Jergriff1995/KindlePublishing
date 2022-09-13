package com.amazon.stock;

import java.math.BigDecimal;

public class StockExchange {
    private String amazonId = "amzn";
    private BigDecimal currentAmazonStockPrice = BigDecimal.valueOf(1_000L);

    private String wholefoodsId = "wfm";

    public StockPriceResponse getMarketPrice(String symbol) throws NonExistentStockException {
        if (symbol.equals(amazonId)) {
            return StockPriceResponse.builder()
                .withSymbol(amazonId)
                .withPrice(currentAmazonStockPrice)
                .build();
        }

        if (symbol.equals("wfm") | symbol.equals("nonexistent")) {
            return null;
        }

        throw new NonExistentStockException();
    }

    public SellStockResponse offer(SellStockRequest request) throws NonExistentStockException {
        if (request.getSymbol().equals(amazonId)) {
            return SellStockResponse.builder()
                .withSymbol(amazonId)
                .withPrice(currentAmazonStockPrice.multiply(BigDecimal.valueOf(request.getQuantity())))
                .withQuantity(request.getQuantity())
                .build();
        }

        if (request.getSymbol().equals("wfm") | request.getSymbol().equals("nonexistent")) {
            return null;
        }

        throw new NonExistentStockException();
    }

    public BuyStockResponse bid(BuyStockRequest request) throws NonExistentStockException {
        if (request.getSymbol().equals(amazonId)) {
            return BuyStockResponse.builder()
                .withSymbol(amazonId)
                .withPrice(currentAmazonStockPrice.multiply(BigDecimal.valueOf(request.getQuantity())))
                .withQuantity(request.getQuantity())
                .build();
        }

        if (request.getSymbol().equals("wfm") | request.getSymbol().equals("nonexistent")) {
            return null;
        }

        throw new NonExistentStockException();
    }
}
