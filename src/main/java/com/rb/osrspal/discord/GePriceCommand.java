package com.rb.osrspal.discord;

import com.rb.osrspal.ge.GeItemLookupService;
import com.rb.osrspal.ge.GeService;
import com.rb.osrspal.util.ItemNameNormalizer;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class GePriceCommand {
    private final GeService geService;
    private final GeItemLookupService geItemLookupService;

    public GePriceCommand(GeService geService, GeItemLookupService geItemLookupService) {
        this.geService = geService;
        this.geItemLookupService = geItemLookupService;
    }

    private static final Logger log =
            LoggerFactory.getLogger(GePriceCommand.class);

    public void handle(SlashCommandInteractionEvent event) {
        String normalizedItemName = ItemNameNormalizer.normalize(
                Objects.requireNonNull(
                        event.getOption("item")).getAsString());

        event.deferReply().queue();

        geItemLookupService.getItemIdFromNormalizedName(normalizedItemName)
                .thenApply(itemId -> {
                    log.debug("Resolved item ID: {}", itemId);
                    return itemId;
                })
                .thenCompose(geService::lookupPriceById)
                .thenAccept(response ->
                        event.getHook().sendMessage(response).queue()
                )
                .exceptionally(ex -> {
                    event.getHook()
                            .sendMessage("❌ Could not fetch GE price for **" + normalizedItemName + "**")
                            .queue();
                    return null;
                });
    }
}
