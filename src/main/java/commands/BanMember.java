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

public class BanMember extends ListenerAdapter {

    public Permissions permissions = new Permissions();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

            String[] command = event.getMessage().getContentRaw().split(" ");

            if (command[0].equalsIgnoreCase("$ban")
                    && permissions.BAN_MEMBERS(event, event.getMember())) {

                Member mentioned = event.getMessage().getMentionedMembers().get(0);

                if (mentioned.getId().equals(Entity.GLADOS) || mentioned.getId().equals(Entity.GLADOS_TESTER)) {
                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + " Nice try.").queue();
                } else {

                    StringBuilder reason = new StringBuilder();

                    for (int i = 2; i < command.length; i++) {
                        reason.append(command[i]).append(" ");
                    }

                    event.getGuild().ban(mentioned.getId(), 7).queue();

                    EmbedBuilder message = new EmbedBuilder();

                    message.setColor(Color.CYAN);

                    if (reason.toString().isEmpty() || reason.toString().equalsIgnoreCase(" ")) {
                        message.setAuthor(mentioned.getUser().getAsTag() + " has been banned", "https://discord.com", mentioned.getUser().getAvatarUrl());
                    } else {
                        message.setAuthor(mentioned.getUser().getAsTag() + " has been banned", "https://discord.com", mentioned.getUser().getAvatarUrl());
                        message.setFooter("Reason: " + reason);
                    }

                    EmbedBuilder privateMessageEmbed = new EmbedBuilder();

                    privateMessageEmbed.setColor(Color.decode(CleanMessages.EmbedColor));

                    privateMessageEmbed.setDescription("You have been banned from **" + event.getGuild().getName() + "** \n **Reason**: " + reason + "");

                    PrivateMessage.sendPrivateMessage(mentioned, privateMessageEmbed, true);

                    event.getChannel().sendMessage(message.build()).queue();

                }

            }


    }
}
