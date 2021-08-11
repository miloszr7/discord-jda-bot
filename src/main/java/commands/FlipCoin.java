package commands;

import ratelimit.Cooldown;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class FlipCoin extends ListenerAdapter {

    private Cooldown cooldown = new Cooldown();

    private String[] content = {
            "Your coin landed on **Heads**!",
            "Your coin landed on **Tails**!"
    };

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        try {

            if (event.getMessage().getContentRaw().equalsIgnoreCase("$flip")
                    && !cooldown.hasCooldown(event.getMember())) {

                cooldown.activate(event, "$flip");

                int r = (int) (Math.random() * content.length);

                event.getChannel().sendMessage("**`Flipping coin...`**").queue(e -> {

                    if (content[r].equalsIgnoreCase(content[0])) {
                        e.editMessage(content[0]).queueAfter(2, TimeUnit.SECONDS);
                    } else {
                        e.editMessage(content[1]).queueAfter(2, TimeUnit.SECONDS);
                    }

                });

            }

        } catch (Exception e) {
            event.getChannel().sendMessage(e.getMessage()).complete().delete().queueAfter(5, TimeUnit.SECONDS);
        }

    }
}
