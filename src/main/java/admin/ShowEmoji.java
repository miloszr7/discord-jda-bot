package admin;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class ShowEmoji extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        String[] command = event.getMessage().getContentRaw().split(" ", 2);

        if (command[0].equalsIgnoreCase("*emotes")
                && event.getAuthor().getId().equals(Entity.DEVELOPER)) {

            String guildId = command[1];

            List<Emote> emoteList = event.getJDA().getGuildById(guildId).getEmotes();

            StringBuilder emotes = new StringBuilder();

            String guildName = "";

            int count = 0;

            for (Emote emote : emoteList) {
                guildName = emote.getGuild().getName();
                emotes.append(emote).append("\n");
                count++;
            }

            event.getChannel().sendMessage(
                    new EmbedBuilder()
                            .setTitle("**" + guildName + "**")
                            .setDescription(emotes + "\n\n" + "**Found** `" + count
                                    + "` **emotes**.")
                            .setColor(Color.CYAN)
                    .build()
            ).queue();

        }

    }
}
