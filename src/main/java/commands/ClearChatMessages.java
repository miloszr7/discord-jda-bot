package commands;

import config.Permissions;
import ratelimit.Cooldown;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class ClearChatMessages extends ListenerAdapter {

    private final Permissions permissions = new Permissions();
    private final Cooldown cooldown = new Cooldown();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        try {

            String[] command = event.getMessage().getContentRaw().split(" ");

            if (command.length == 2 && command[0].equalsIgnoreCase("$clear")
                    && permissions.MANAGE_CHANNELS(event, event.getMember()) && !cooldown.hasCooldown(event.getMember())) {

                cooldown.activate(event, "$clear");

                if (Integer.parseInt(command[1]) > 200) {
                    event.getChannel().sendMessage("You cant remove more than **200** messages.").queue();
                } else {
                    event.getMessage().delete().queue();

                    TextChannel target = event.getChannel();

                    removeMessages(target, Integer.parseInt(command[1]));
                }

            }

        } catch (Exception ex) {
            event.getChannel().sendMessage(ex.getMessage()).complete().delete().queueAfter(5, TimeUnit.SECONDS);
        }

    }

    private void removeMessages(TextChannel channel, int messages) {

        channel.getIterableHistory()
                .takeAsync(messages)
                    .thenAccept(channel::purgeMessages);

    }

}
