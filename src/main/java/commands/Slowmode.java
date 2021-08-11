package commands;

import config.Permissions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.*;

public class Slowmode extends ListenerAdapter {

    public Permissions permissions = new Permissions();

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {

        try {

            String[] command = event.getMessage().getContentRaw().split(" ");

            if (command.length == 2 && command[0].equalsIgnoreCase("$slowmode")
                    && permissions.MANAGE_CHANNELS(event, event.getMember())) {

                int cooldown = Integer.parseInt(command[1]);

                event.getChannel().getManager().setSlowmode(cooldown).queue();

                if (cooldown > 1) {

                    EmbedBuilder message = new EmbedBuilder();

                    message.setDescription("**SLOWMODE** is now enabled for " + event.getChannel().getAsMention() + "\n" +
                            "with `" + cooldown + "` seconds cooldown.");

                    message.setThumbnail("https://i.imgur.com/nuKU0q5.png");
                    message.setColor(Color.decode(CleanMessages.EmbedColor));
                    message.setAuthor(event.getAuthor().getName(), "https://discord.com", event.getAuthor().getAvatarUrl());

                    event.getChannel().sendMessage(message.build()).queue();

                } else if (cooldown == 0) {

                    EmbedBuilder message = new EmbedBuilder();

                    message.setDescription("**SLOWMODE** is no longer enabled for " + event.getChannel().getAsMention());
                    message.setThumbnail("https://i.imgur.com/nuKU0q5.png");
                    message.setColor(Color.decode(CleanMessages.EmbedColor));
                    message.setAuthor(event.getAuthor().getName(), "https://discord.com", event.getAuthor().getAvatarUrl());

                    event.getChannel().sendMessage(message.build()).queue();

                }

            }

        } catch (Exception e) {

            if (e.getMessage().equals("Slowmode per user must be between 0 and 21600 (seconds)!")) {
                event.getChannel().sendMessage("> :no_entry: ** Slowmode must be between 0 and 21600s**.").queue();
            } else {
                event.getChannel().sendMessage("> :no_entry: **" + e.getMessage() + "**").queue();
            }
        }

    }
}
