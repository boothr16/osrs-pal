package com.rb.osrspal.ge;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class PricesWikiClient {

    private final WebClient webClient;

    public PricesWikiClient(@Qualifier("wikiPricesWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public record LatestPriceResponse(Map<String, PriceData> data) { }

    public record PriceData(
            Integer high,
            Long highTime,
            Integer low,
            Long lowTime
    ) { }

    public CompletableFuture<LatestPriceResponse> fetchLatestPrice(int itemId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/latest")
                        .queryParam("id", itemId)
                        .build())
                .retrieve()
                .bodyToMono(LatestPriceResponse.class)
                .toFuture();
    }
}
