package commands;

import ratelimit.Cooldown;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class SearchForMember extends ListenerAdapter {

    private final Cooldown cooldown = new Cooldown();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        String[] command = event.getMessage().getContentRaw().split(" ");

        if (command.length == 2 && command[0].equalsIgnoreCase("$search")
                && !cooldown.hasCooldown(event.getMember())) {

            cooldown.activate(event, "$search");

            EmbedBuilder dashboard = new EmbedBuilder();

            MessageBuilder builder = new MessageBuilder();

            dashboard.setTitle("Your search result");

            dashboard.setColor(Color.BLACK);

                int count = 0;

                for (Member m : event.getGuild().getMembers()) {

                    if (m.getUser().getName().contains(command[1])) {
                        count++;

                        builder.append(m.getUser().getAsMention()).append("\n");

                    }

                    if ((!(m.getNickname() == null))) {
                        if (m.getNickname().contains(command[1])) {
                            count++;

                            builder.append(m.getUser().getAsMention()).append("\n");

                        }
                    }

                }

                dashboard.setDescription(builder.getStringBuilder());

                if (count == 0) {
                    dashboard.setTitle("Your search result");
                    dashboard.setDescription("I couldn't find anything for you :confounded: ");
                }

            event.getChannel().sendTyping().queue();

            event.getChannel().sendMessage(dashboard.build()).queueAfter(2, TimeUnit.SECONDS);


        }

    }

}
