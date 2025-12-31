package com.rb.osrspal.ge;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;

@Component
public class GeClient {

    private final WebClient webClient;

    public GeClient(@Qualifier("osrsWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public CompletableFuture<GeItemPrice> fetchPriceFromItemId(Integer itemId) {
        String url = "/m=itemdb_oldschool/api/catalogue/detail.json?item=" + itemId.toString();
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(GeItemPrice.class)
                .toFuture();
    }
}
