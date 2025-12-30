package com.rb.osrspal.ge;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GeItemPrice(
        @JsonProperty("name") String name,
        @JsonProperty("current") Current current
) {
    public int price() {
        return current.price();
    }

    public record Current(@JsonProperty("price") int price) {}
}
