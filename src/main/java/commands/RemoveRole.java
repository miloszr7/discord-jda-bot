package commands;

import config.Permissions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class RemoveRole extends ListenerAdapter {

    private final Permissions permissions = new Permissions();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        String[] command = event.getMessage().getContentRaw().split(" ");

        if (command.length == 2 && command[0].equalsIgnoreCase("$remove")
                && permissions.MANAGE_ROLES(event, event.getMember())) {

            Role role = event.getMessage().getMentionedRoles().get(0);

            event.getGuild().getRoleById(role.getId()).delete().queue();

            event.getChannel().sendMessage(
                    new EmbedBuilder()
                            .setColor(Color.CYAN)
                            .setDescription("Removed `" + role.getName() + "` from server roles")
                    .build()
            ).queue();

        }

    }
}
