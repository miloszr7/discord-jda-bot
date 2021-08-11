package commands;

import config.Permissions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class UnbanMember extends ListenerAdapter {

    public Permissions permissions = new Permissions();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        try {

            String[] unban = event.getMessage().getContentRaw().split(" ");

            if (unban.length == 2 && unban[0].equalsIgnoreCase("$unban")
                    && permissions.BAN_MEMBERS(event, event.getMember())) {

                String bannedID = unban[1];

                event.getGuild().unban(bannedID).queue();

                unbanMessage(event.getGuild(), event.getChannel(), event.getMember(), event.getMessage());

            }

        } catch (Exception e) {
            event.getChannel().sendMessage(e.getMessage()).complete().delete().queueAfter(5, TimeUnit.SECONDS);
        }

    }

    private void unbanMessage(Guild guild, TextChannel channel, Member admin, Message message) {

        SimpleDateFormat stf4 = new SimpleDateFormat("hh:mm a");
        Date date4 = new Date();

        String[] space = message.getContentRaw().split(" ");

        String id = space[1];

        EmbedBuilder unban = new EmbedBuilder();
        unban.setColor(Color.decode(CleanMessages.EmbedColor));
        unban.setDescription("**" + id + "**" + " has been successfully unbanned.");
        unban.setFooter("\u2022 " + stf4.format(date4), guild.getIconUrl());
        channel.sendMessage(unban.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);

    }

}
