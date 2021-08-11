package commands;

import config.Permissions;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;

public class SendDiscordInvitation extends ListenerAdapter {

    private Permissions permissions = new Permissions();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        try {

            if (event.getMessage().getContentRaw().equalsIgnoreCase("$glados")) {

                String url = "";

                event.getChannel().sendMessage("Your invitation:").setActionRow(
                        Button.link(url, "Click me!")
                ).queue();

            }

            if (event.getMessage().getContentRaw().equalsIgnoreCase("$inv") && permissions.CREATE_INSTANT_INVITE(event.getMember())) {

                String url = event.getChannel().createInvite().setTemporary(true).complete().getUrl();

                event.getChannel().sendMessage(url).queue();
            }

        } catch (Exception e) {
            event.getChannel().sendMessage(e.getMessage()).queue();
        }

    }
}
