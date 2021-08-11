package commands;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ChangeActivity extends ListenerAdapter {

    //private String ID = configuration.document.getElementsByTagName("developerID").item(0).getTextContent();

    private String ID = "";

    private String msg = "";

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        String[] message = event.getMessage().getContentRaw().split(" ");

        if (message.length != 1 && message[0].equals("*activity")
                && event.getAuthor().getId().equals(ID)) {

            event.getMessage().delete().queue();

            for (int i = 1; i < message.length; i++) {

                msg += message[i] + " ";

            }

            event.getChannel().sendMessage("`New activity has been set`").queue();

        }

        if (event.getMessage().getContentRaw().equalsIgnoreCase("*set activity")
                && event.getAuthor().getId().equals(ID)) {

            event.getMessage().delete().queue();

            event.getChannel().sendTyping().queue();

            event.getJDA().getPresence().setPresence(Activity.playing(msg), false);

        }

        if (event.getMessage().getContentRaw().equalsIgnoreCase("*reset activity")
                && event.getAuthor().getId().equals(ID)) {

            event.getMessage().delete().queue();

            msg = "";

            event.getJDA().getPresence().setActivity(Activity.streaming("\u200E", "https://www.twitch.tv/wherearethehoneyberries"));

        }

    }
}
