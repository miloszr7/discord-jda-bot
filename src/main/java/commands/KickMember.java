package commands;

import admin.Entity;
import config.Permissions;
import privateMessages.PrivateMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class KickMember extends ListenerAdapter {

    public Permissions permissions = new Permissions();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        try {

            String[] space = event.getMessage().getContentRaw().split(" ");

            if (space.length != 1 && space[0].equalsIgnoreCase("$kick") &&
                    permissions.KICK_MEMBERS(event, event.getMember())) {

                Member mentioned = event.getMessage().getMentionedMembers().get(0);

                if (mentioned.getId().equals(Entity.GLADOS) || mentioned.getId().equals(Entity.GLADOS_TESTER)) {
                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + " Nice try.").queue();
                } else {

                    String reason = event.getMessage().getContentRaw().replace("$kick", "")
                            .replace("<@!" + mentioned.getId() + ">", "").replace("<@" + mentioned.getId() + ">", "");

                    event.getGuild().kick(mentioned).queue();

                    EmbedBuilder message = new EmbedBuilder();

                    message.setColor(Color.CYAN);

                    if (reason.equals(" ")) {
                        message.setAuthor(mentioned.getUser().getAsTag() + " has been kicked", "https://discord.com", mentioned.getUser().getAvatarUrl());
                    } else {
                        message.setAuthor(mentioned.getUser().getAsTag() + " has been kicked", "https://discord.com", mentioned.getUser().getAvatarUrl());
                        message.setFooter("Reason: " + reason);
                    }

                    EmbedBuilder privateMessageEmbed = new EmbedBuilder();

                    privateMessageEmbed.setColor(Color.decode(CleanMessages.EmbedColor));

                    PrivateMessage privateMessage = new PrivateMessage();

                    privateMessageEmbed.setDescription("You have been kicked from **" + event.getGuild().getName() + "** \n **Reason**: " + reason + "");

                    privateMessage.sendPrivateMessage(mentioned, privateMessageEmbed, true);


                    event.getChannel().sendMessage(message.build()).queue();

                }

            }

        } catch (Exception ex) {
            event.getChannel().sendMessage(ex.getMessage()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
        }

    }
}
