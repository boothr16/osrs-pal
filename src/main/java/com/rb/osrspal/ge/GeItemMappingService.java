package com.rb.osrspal.ge;

import com.rb.osrspal.util.ItemNameNormalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GeItemMappingService {

    private static final Logger log = LoggerFactory.getLogger(GeItemMappingService.class);

    private final WebClient webClient;
    private final Map<String, Integer> normalizedNameToId = new ConcurrentHashMap<>();

    public GeItemMappingService(@Qualifier("wikiPricesWebClient") WebClient webClient) {
        this.webClient = webClient;
        fetchMapping();
    }

    private void fetchMapping() {
        log.info("Fetching RuneLite item mapping JSON from /mapping endpoint...");

        webClient.get()
                .uri("/mapping")
                .retrieve()
                .bodyToFlux(ItemMappingDTO.class)  // deserialize each array element into ItemMapping
                .doOnNext(item -> normalizedNameToId.put(
                        ItemNameNormalizer.normalize(item.name()), item.id()))
                .doOnComplete(() -> log.info("Loaded {} item mappings.", normalizedNameToId.size()))
                .doOnError(e -> log.error("Failed to fetch item mapping", e))
                .blockLast(); // block until the flux completes
    }

    public record ItemMappingDTO(int id, String name) { }

    public Optional<Integer> getItemIdFromNormalizedName(String normalizedName) {
        return Optional.ofNullable(normalizedNameToId.get(normalizedName));
    }
}
