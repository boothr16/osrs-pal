package com.rb.osrspal.ge;

import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class GeService {

    private final GeClient geClient;

    public GeService(GeClient geClient) {
        this.geClient = geClient;
    }

    public CompletableFuture<String> lookup(String itemName) {
        return geClient.fetch(itemName)
                .thenApply(price -> "**" + price.name() + "**\nPrice: " + price.price() + " gp");
    }
}
