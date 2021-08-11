package commands;

import config.Permissions;
import models.Members;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ShowMembers extends ListenerAdapter {

    public Permissions permissions = new Permissions();

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {

        try {

            if (event.getMessage().getContentRaw().equals("$members")
                    && permissions.MANAGE_SERVER(event, event.getMember())) {

                List<Member> members = event.getGuild().getMembers();

                EmbedBuilder dashboard = new EmbedBuilder();

                dashboard.setTitle(event.getGuild().getName() + " members");
                dashboard.setColor(Color.BLACK);

                MessageBuilder builder = new MessageBuilder();

                for (Member member : members) {

                    Members members1 = new Members(member.getUser().getName(), member.getIdLong());

                    builder.append(members1.toString()).append("\n");

                    dashboard.setDescription(builder.getStringBuilder());

                }

                event.getChannel().sendMessage("> **Processing server members**...").complete().delete().queueAfter(2, TimeUnit.SECONDS);
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage(dashboard.build()).queueAfter(3, TimeUnit.SECONDS);

            } else if (event.getMessage().getContentRaw().equals("$members")
                    && !permissions.MANAGE_SERVER(event, event.getMember())) {

                event.getChannel().sendMessage(":x: You need **MANAGE_SERVER** permission to use this command.").complete().delete().queueAfter(5, TimeUnit.SECONDS);

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
