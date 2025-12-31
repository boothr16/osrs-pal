package com.rb.osrspal.ge;

import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class GeService {

    private final PricesWikiClient wikiClient;

    public GeService(PricesWikiClient wikiClient) {
        this.wikiClient = wikiClient;
    }

    public CompletableFuture<String> lookupPriceById(String queryString, int itemId) {
        return wikiClient.fetchLatestPrice(itemId)
                .thenApply(response -> {
                    PricesWikiClient.PriceData pd = response.data().get(String.valueOf(itemId));
                    if (pd == null) {
                        return "No price data available.";
                    }
                    return String.format(
                            "💰%s💰\nHigh: %d | Low: %d | Mid: %d",
                            queryString,
                            pd.high(),
                            pd.low(),
                            (pd.high() + pd.low()) / 2
                    );
                });
    }
}
