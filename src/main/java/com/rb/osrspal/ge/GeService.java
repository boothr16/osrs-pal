package com.rb.osrspal.ge;

import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class GeService {

    private final GeClient geClient;

    public GeService(GeClient geClient) {
        this.geClient = geClient;
    }

    public CompletableFuture<String> lookupPriceById(Integer itemId) {
        return geClient.fetchPriceFromItemId(itemId)
                .thenApply(price -> "**" + price.name() + "**\nPrice: " + price.price() + " gp");
    }
}
