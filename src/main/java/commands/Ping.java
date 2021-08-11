package commands;

import ratelimit.Cooldown;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class Ping extends ListenerAdapter {

    private Cooldown cooldown = new Cooldown();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        try {

            if (event.getMessage().getContentRaw().equals("$ping") && !cooldown.hasCooldown(event.getMember())) {

                cooldown.activate(event, "$ping");

                long time = System.currentTimeMillis();

                event.getChannel().sendTyping().queue();


                event.getChannel().sendMessage("Checking your ping...").queue(response -> {
                    event.getChannel().sendTyping().queue();
                    response.editMessageFormat("**PING**: %d ms", System.currentTimeMillis() - time).queueAfter(3, TimeUnit.SECONDS);
                });
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
