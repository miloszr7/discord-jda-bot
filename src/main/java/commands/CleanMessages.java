package commands;

import config.Permissions;
import ratelimit.Cooldown;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CleanMessages extends ListenerAdapter {

    private final Permissions permissions = new Permissions();
    private final Cooldown cooldown = new Cooldown();

    public static String EmbedColor = "#00FFFF";
    public static String developerID = "";


    private final String BOT = "";
    //private final String BOT_DEV = "740914695501774858";

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent e) {

        try {

            if (e.getMessage().getContentRaw().equalsIgnoreCase("$clean")
                    && permissions.MANAGE_CHANNELS(e, e.getMember()) && !cooldown.hasCooldown(e.getMember())) {

                cooldown.activate(e, "$clean");

                e.getMessage().delete().queue();

                List<Message> messages = new ArrayList<>();

                e.getChannel().getIterableHistory().forEachAsync(
                        m -> {
                            if (m.getAuthor().getId().equals(BOT)) messages.add(m);
                                return messages.size() < 100;
                        }
                ).thenRun(() -> e.getChannel().purgeMessages(messages));

            }

            String[] command = e.getMessage().getContentRaw().split(" ");

            if (command[0].equalsIgnoreCase("$uclear")
                    && permissions.MANAGE_CHANNELS(e, e.getMember()) && !cooldown.hasCooldown(e.getMember())) {

                cooldown.activate(e, "$uclear");

                int messagesNumber = Integer.parseInt(command[2]);

                if (messagesNumber > 100) {
                    e.getChannel().sendMessage(e.getAuthor().getAsMention() + " you can remove max `100` messages.").queue();
                } else if (messagesNumber < 3) {
                    e.getChannel().sendMessage(e.getAuthor().getAsMention() + " you can remove min `3` messages.").queue();
                } else {

                    Member member = e.getMessage().getMentionedMembers().get(0);

                    List<Message> messages = new ArrayList<>();

                    e.getChannel().getIterableHistory().forEachAsync(
                            m -> {
                                if (m.getAuthor().getId().equals(member.getId())) messages.add(m);
                                return messages.size() < messagesNumber;
                            }
                    ).thenRun(() -> e.getChannel().purgeMessages(messages));

                    EmbedBuilder info = new EmbedBuilder();

                    info.setDescription("Removed `" + messagesNumber + "` messages of **" + member.getUser().getAsTag() + "**");
                    info.setColor(Color.decode(EmbedColor));

                    e.getChannel().sendMessage(info.build()).complete().delete().queueAfter(5, TimeUnit.SECONDS);

                }

            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }
}
