package commands;

import config.Permissions;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Announce extends ListenerAdapter {

    private final Permissions permissions = new Permissions();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        String[] command = event.getMessage().getContentRaw().split(" ");

        if (command[0].equalsIgnoreCase("$ann")
                && permissions.MANAGE_CHANNELS(event, event.getMember())) {
            event.getMessage().delete().queue();

            StringBuilder title = new StringBuilder();
            StringBuilder desc = new StringBuilder();
            StringBuilder color = new StringBuilder();

            for (int i = 1; i < command.length; i++) {

                title.append(command[i]).append(" ");
                desc.append(command[i]).append(" ");
                color.append(command[i]).append(" ");

            }

            // $ann (title) [desc] <#color>

            String readyTitle = title.substring(title.indexOf("(")+1, title.indexOf(")"));
            String readyDesc = desc.substring(desc.indexOf("[")+1, desc.indexOf("]"));
            String readyColor = color.substring(desc.indexOf("<")+1, desc.indexOf(">"));

            System.out.println(readyColor);

            if (readyColor.equalsIgnoreCase("none")) {
                event.getChannel().sendMessage(
                        new EmbedBuilder()
                                .setTitle(readyTitle)
                                .setDescription(readyDesc)
                                .build()
                ).queue();
            } else {
                event.getChannel().sendMessage(
                        new EmbedBuilder()
                                .setTitle(readyTitle)
                                .setColor(Color.decode(readyColor))
                                .setDescription(readyDesc)
                                .build()
                ).queue();
            }



        }

    }
}
