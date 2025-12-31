package com.rb.osrspal.ge;

import com.rb.osrspal.util.ItemNameNormalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GeItemMappingService {

    private final WebClient webClient;
    private final Map<String, Integer> normalizedNameToId = new ConcurrentHashMap<>();
    private static final Logger log = LoggerFactory.getLogger(GeItemMappingService.class);

    public GeItemMappingService(@Qualifier("geMappingWebClient") WebClient webClient) {
        this.webClient = webClient;
        fetchMapping();
    }

    private void fetchMapping() {
        log.info("Fetching RuneLite item mapping JSON...");
        webClient.get()
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .doOnNext(map -> {
                    map.forEach((id, name) ->
                            normalizedNameToId.put(ItemNameNormalizer.normalize(name), Integer.parseInt(id))
                    );
                    log.info("Loaded {} item mappings.", map.size());
                })
                .doOnError(e -> log.error("Failed to fetch item mapping", e))
                .block(); // block once at startup
    }

    public CompletableFuture<Integer> getItemIdFromNormalizedName(String normalizedName) {
        Integer id = normalizedNameToId.get(normalizedName.toLowerCase(Locale.ROOT));
        if (id != null) {
            return CompletableFuture.completedFuture(id);
        } else {
            return CompletableFuture.failedFuture(
                    new RuntimeException("No item ID found for: " + normalizedName));
        }
    }
}