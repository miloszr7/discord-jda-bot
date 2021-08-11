package commands;

import admin.Entity;
import config.Permissions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class MuteMember extends ListenerAdapter {

    private final Permissions permissions = new Permissions();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        String[] command = event.getMessage().getContentRaw().split(" ");

        try {

            if (command[0].equalsIgnoreCase("$mute")
                    && permissions.KICK_MEMBERS(event, event.getMember())) {

                Member member = event.getMessage().getMentionedMembers().get(0);

                if (member.getId().equals(Entity.GLADOS) || member.getId().equals(Entity.GLADOS_TESTER)) {

                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + " Nice try.").queue();

                } else {

                    List<TextChannel> textChannels = event.getGuild().getTextChannels();

                    for (TextChannel channel : textChannels) {
                        channel.getManager().getChannel().createPermissionOverride(member).setDeny(Permission.MESSAGE_WRITE).queue();
                    }

                    event.getChannel().sendMessage(
                            new EmbedBuilder()
                                    .setColor(Color.CYAN)
                                    .setAuthor(member.getUser().getAsTag() + " is now muted", "https://discord.com", member.getUser().getAvatarUrl())
                                    .build()
                    ).queue();

                }

            }

            if (command[0].equalsIgnoreCase("$unmute")
                    && permissions.KICK_MEMBERS(event, event.getMember())) {

                Member member = event.getMessage().getMentionedMembers().get(0);

                List<TextChannel> textChannels = event.getGuild().getTextChannels();

                for (TextChannel channel : textChannels) {
                    channel.getManager().getChannel().getPermissionOverride(member).delete().queue();
                }

                event.getChannel().sendMessage(
                        new EmbedBuilder()
                                .setColor(Color.CYAN)
                                .setAuthor(member.getUser().getAsTag() + " is now unmuted", "https://discord.com", member.getUser().getAvatarUrl())
                                .build()
                ).queue();

            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }
}
