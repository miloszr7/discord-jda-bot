package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

// shows 10 longest members on the server
public class MemberStats extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        String[] command = event.getMessage().getContentRaw().split(" ");

        if (command[0].equalsIgnoreCase("$stats")) {

            if (event.getGuild().getMemberCount() < 10) {
                event.getChannel().sendMessage("There has to be at least `10` members in the server to display statistics.").queue();
            } else {

                List<Member> memberList = event.getGuild().getMembers();

                System.out.println(memberList);

                HashMap<String, String> users = new HashMap<>();

                for (Member member : memberList) {
                    users.put(member.getEffectiveName(), member.getTimeJoined().format(DateTimeFormatter.ofPattern("MM/dd/YYYY")));
                    System.out.println(member.getUser().getAsTag());
                }

                System.out.println(users);

                TreeMap<String, String> sort = new TreeMap<>(Collections.reverseOrder());

                sort.putAll(users);

                StringBuilder topUsers = new StringBuilder();

                List<String> sortedUsers = new ArrayList<>();

                for (Map.Entry<String, String> entry : sort.entrySet()) {
                    sortedUsers.add(entry.getKey() + " - " + entry.getValue());
                }

                int count = 1;

                for (int i = 0; i < 10; i++) {
                    topUsers.append("**").append(count++).append("**").append(". ").append(sortedUsers.get(i)).append("\n");
                }

                event.getChannel().sendMessage(
                        new EmbedBuilder()
                                .setColor(Color.CYAN)
                                .setTitle("**TOP 10 MEMBERS**")
                                .setDescription(topUsers)
                                .build()
                ).queue();

            }

        }

    }
}
