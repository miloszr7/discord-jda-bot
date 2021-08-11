package admin;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Statistics extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        String[] command = event.getMessage().getContentRaw().split(" ");

        if (command[0].equalsIgnoreCase("*stats")
                && event.getAuthor().getId().equals(Entity.DEVELOPER)) {

            // get servers
            List<Guild> allServers = event.getJDA().getGuilds();

            // from server list get guild name and member count
            HashMap<Integer, String> info = new HashMap<>();

            for (Guild guild : allServers) {
                info.put(guild.getMemberCount(), guild.getName());
            }


            TreeMap<Integer, String> sorted = new TreeMap<>(Collections.reverseOrder());

            sorted.putAll(info);

            StringBuilder topServers = new StringBuilder();

            List<String> serverList = new ArrayList<>();

            for (Map.Entry<Integer, String> entry : sorted.entrySet()) {
                serverList.add(entry.getValue() + " - " + entry.getKey());
            }

            for (int i = 0; i < 10; i++) {
                topServers.append(serverList.get(i)).append("\n");
            }

            event.getChannel().sendMessage(
                    new EmbedBuilder()
                            .setColor(Color.CYAN)
                            .setTitle("Top 10 Servers")
                            .setDescription(topServers)
                    .build()
            ).queue();


        }

    }
}
