package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class ShowEmotes extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        String[] command = event.getMessage().getContentRaw().split(" ");

        if (command.length == 1 && command[0].equalsIgnoreCase("$emotes")) {

            List<Emote> emotes = event.getGuild().getEmotes();

            MessageBuilder builder = new MessageBuilder();
            EmbedBuilder message = new EmbedBuilder();

            message.setColor(Color.CYAN);
            message.setTitle("**" + event.getGuild().getName() + "**");
            message.setThumbnail(event.getGuild().getIconUrl());

            int count = 0;
            int maxEmotes = event.getGuild().getMaxEmotes();

            for (Emote e : emotes) {
                count++;
                builder.append(e.getAsMention() + " ");
            }

            message.setDescription(builder.getStringBuilder() + "\n\n" +
                   "Currently **" + count + "**/**" + maxEmotes + "** emotes.");

            event.getChannel().sendMessage(message.build()).queue();

        }

    }
}
