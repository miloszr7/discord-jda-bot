package commands;

import admin.Entity;
import config.Permissions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class Vote extends ListenerAdapter {

    private final Permissions permissions = new Permissions();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        String[] command = event.getMessage().getContentRaw().split(" ");

        if (command.length > 1 && command[0].equalsIgnoreCase("$poll")
                && permissions.KICK_MEMBERS(event, event.getMember())) {

            boolean hasPermission = event.getGuild().getMemberById(Entity.GLADOS)
                    .hasPermission(Permission.MESSAGE_EXT_EMOJI);

            if (hasPermission) {

                event.getMessage().delete().queue();

                StringBuilder description = new StringBuilder();

                for (int i = 1; i < command.length; i++) {
                    description.append(command[i]).append(" ");
                }

                Emote cross = event.getJDA().getGuildById("").getEmoteById("");
                Emote check = event.getJDA().getGuildById("").getEmoteById("");

                event.getChannel().sendMessage(
                        new EmbedBuilder()
                                .setTitle("**" + event.getGuild().getName().toUpperCase() + "**")
                                .setDescription(description)
                                .setTimestamp(event.getMessage().getTimeCreated())
                                .build()
                ).queue(action -> {

                    action.addReaction(check).queue();
                    action.addReaction(cross).queue();

                });

            } else {
                event.getChannel().sendMessage("Missing permission: **Use External Emoji**.").queue();
            }

        }

    }
}
