package com.rb.osrspal.ge;

import com.rb.osrspal.discord.GePriceCommand;
import com.rb.osrspal.util.ItemNameNormalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

@Component
public class GeItemLookupService {
    private final WebClient webClient;

    public GeItemLookupService(@Qualifier("osrsWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    private static final Logger log =
            LoggerFactory.getLogger(GeItemLookupService.class);

    public CompletableFuture<Integer> getItemIdFromNormalizedName(String normalizedName) {
        char alpha = normalizedName.charAt(0);

        String path = "/m=itemdb_oldschool/api/catalogue/items.json";
        URI uri = UriComponentsBuilder.fromUriString(path)
                .queryParam("category", 1)
                .queryParam("alpha", alpha)
                .build()
                .toUri();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(ItemSearchResponse.class)
                .flatMap(response -> Flux.fromIterable(response.items)
                        .filter(item ->
                                ItemNameNormalizer.normalize(item.name)
                                        .equals(normalizedName)
                        )
                        .next()
                        .switchIfEmpty(Mono.error(
                                new RuntimeException("Item not found: " + normalizedName)
                        ))
                        .map(item -> item.id)
                )
                .doOnError(ex -> log.error("Item lookup failed for {}", normalizedName, ex))
                .toFuture();
    }

    private static class ItemSearchResponse {
        public java.util.List<Item> items;
    }

    private static class Item {
        public int id;
        public String name;
    }
}