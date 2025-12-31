package com.rb.osrspal.discord;

import com.rb.osrspal.ge.GeItemMappingService;
import com.rb.osrspal.ge.GeService;
import com.rb.osrspal.util.ItemNameNormalizer;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class GePriceCommand {

    private final GeItemMappingService mappingService;
    private final GeService geService;

    public GePriceCommand(GeItemMappingService mappingService, GeService geService) {
        this.mappingService = mappingService;
        this.geService = geService;
    }

    public void handle(SlashCommandInteractionEvent event) {

        String queryString = Objects.requireNonNull(event.getOption("item")).getAsString();
        String normalizedItemName = ItemNameNormalizer.normalize(queryString);

        event.deferReply().queue();

        mappingService.getItemIdFromNormalizedName(normalizedItemName)
                .ifPresentOrElse(
                        itemId -> geService.lookupPriceById(queryString, itemId)
                                .thenAccept(response ->
                                        event.getHook().sendMessage(response).queue()
                                )
                                .exceptionally(ex -> {
                                    event.getHook().sendMessage(
                                            "❌ Failed to fetch prices for: " + queryString).queue();
                                    return null;
                                }),
                        () -> event.getHook().sendMessage(
                                "❌ Unknown item: " + queryString).queue()
                );
    }
}
