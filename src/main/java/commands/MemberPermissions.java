package commands;

import config.Permissions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MemberPermissions extends ListenerAdapter {

    public Permissions permissions = new Permissions();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        try {

            String[] command = event.getMessage().getContentRaw().split(" ");

            if (command.length == 2 && command[0].equalsIgnoreCase("$p")
                    && permissions.MANAGE_ROLES(event, event.getMember())) {

                Member member = event.getMessage().getMentionedMembers().get(0);

                EnumSet<Permission> permissions = member.getPermissions();

                EmbedBuilder message = new EmbedBuilder();

                message.setColor(Color.CYAN);
                message.setTitle("**" + member.getUser().getAsTag() + "**");

                MessageBuilder builder = new MessageBuilder();

                List<Role> roles = member.getRoles();

                int count = 0;

                for (Permission p : permissions) {

                    count++;

                    builder.append("**" + count + "**. " + p.getName() + "\n");

                }


                message.setThumbnail(member.getUser().getAvatarUrl());

                if (roles.isEmpty()) {
                    message.setDescription(builder.getStringBuilder());
                } else {
                    message.setDescription(roles.get(0).getAsMention() + "\n\n" + builder.getStringBuilder());
                }

                event.getChannel().sendMessage(message.build()).queue();


            }

        } catch (Exception ex) {
            event.getChannel().sendMessage(ex.getMessage()).complete().delete().queueAfter(5, TimeUnit.SECONDS);
        }

    }
}
