package ratelimit;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class Cooldown extends ListenerAdapter {

    private final Set<String> warned_users = new HashSet<>();


    public boolean hasCooldown(Member member) {

        return warned_users.contains(member.getId());

    }

    public void activate(@NotNull GuildMessageReceivedEvent event, String command) {

        if (event.getMessage().getContentRaw().startsWith(command) && !event.getAuthor().isBot()) {

            List<Message> history = event.getChannel().getIterableHistory().complete().stream().limit(10)
                    .filter(msg -> !msg.equals(event.getMessage())).collect(Collectors.toList());

            int spam = (int) history.stream().filter(message -> message.getAuthor().equals(event.getAuthor()) && !message.getAuthor().isBot())
                    .filter(msg -> (event.getMessage().getTimeCreated().toEpochSecond() - msg.getTimeCreated().toEpochSecond()) < 6).count();

            if (spam > 1) {

                warned_users.add(event.getAuthor().getId());

            }

            if (!event.getAuthor().isBot() && warned_users.contains(event.getAuthor().getId())) {

                event.getChannel().sendMessage(
                        new EmbedBuilder()
                        .setColor(Color.CYAN)
                                .setTitle("You are sending messages too fast")
                                .setDescription("You have to wait `5 seconds` before sending another message.\n And please slow down, thanks.")
                        .build()
                ).queue();

                if (!warned_users.isEmpty()) {

                    TimerTask task = new TimerTask() {
                        public void run() {
                            warned_users.remove(event.getAuthor().getId());
                        }
                    };

                    Timer timer = new Timer("Timer");

                    long delay = 5000L;
                    timer.schedule(task, delay);

                }

            }

        }

    }

}
