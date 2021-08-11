package commands;

import ratelimit.Cooldown;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class DisplayMemberAvatar extends ListenerAdapter {

    private Cooldown cooldown = new Cooldown();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        String[] command = event.getMessage().getContentRaw().split(" ");

        if (command.length == 2 && command[0].equalsIgnoreCase("$avatar") && !cooldown.hasCooldown(event.getMember())) {

            cooldown.activate(event, "$avatar");

            Member mentioned = event.getMessage().getMentionedMembers().get(0);

            MessageBuilder messageBuilder = new MessageBuilder()
                    .setEmbed(new EmbedBuilder()
                            .setColor(Color.decode(CleanMessages.EmbedColor))
                            .setAuthor(mentioned.getUser().getAsTag())
                            .setTitle("Link to avatar", mentioned.getUser().getAvatarUrl())
                            .setImage(mentioned.getUser().getAvatarUrl())
                            .build());

            event.getChannel().sendMessage(messageBuilder.build()).queue();

        } else if (event.getMessage().getContentRaw().equals("$avatar") && !cooldown.hasCooldown(event.getMember())) {

            cooldown.activate(event, "$avatar");

            MessageBuilder messageBuilder = new MessageBuilder()
                    .setEmbed(new EmbedBuilder()
                            .setColor(Color.decode(CleanMessages.EmbedColor))
                            .setAuthor(event.getMember().getUser().getAsTag())
                            .setTitle("Link to avatar", event.getMember().getUser().getAvatarUrl())
                            .setImage(event.getMember().getUser().getAvatarUrl())
                            .build());

            event.getChannel().sendMessage(messageBuilder.build()).queue();

        }

    }
}
