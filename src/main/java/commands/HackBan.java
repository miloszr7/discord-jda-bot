package commands;

import config.Permissions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class HackBan extends ListenerAdapter {

    public Permissions permissions = new Permissions();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        try {

            String[] command = event.getMessage().getContentRaw().split(" ");

            if (command.length == 2 && command[0].equalsIgnoreCase("$hackban")
                    && permissions.BAN_MEMBERS(event, event.getMember())) {
                
                String memberID = command[1];

                event.getGuild().ban(memberID, 0).queue();

                event.getChannel().sendMessage(
                        new EmbedBuilder()
                                .setColor(Color.BLACK)
                                .setThumbnail("https://i.imgur.com/YtaLB7S.png")
                                .setDescription("Unknown discord user with ID \n**`" + memberID + "`**\n has been **HACKBANNED**!")
                                .setTimestamp(event.getMessage().getTimeCreated())
                        .build()
                ).queue();

            }

        } catch (Exception e) {
            event.getChannel().sendMessage(e.getMessage()).complete().delete().queueAfter(5, TimeUnit.SECONDS);
        }

    }

}
