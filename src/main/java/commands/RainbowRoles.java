package commands;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class RainbowRoles extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {

        String[] rainbowThis = event.getMessage().getContentRaw().split(" ");

        boolean rainbowMaster = event.getMember().isOwner();

        if (rainbowThis.length == 3 && rainbowThis[0].equalsIgnoreCase("$rainbow") && rainbowMaster) {
            event.getMessage().delete().queue();

            // $rainbow [@rank] [int]

            Role rainbowRole = event.getMessage().getMentionedRoles().get(0);

            Color originalRankColor = rainbowRole.getColor();

            int length = Integer.parseInt(rainbowThis[2]); // [int]

            int x = 0;

            for (int i = 0; i < length; i++) {

                rainbowRole.getManager().setColor(Color.decode("#FF0000")).queue();
                rainbowRole.getManager().setColor(Color.decode("#FF7F00")).queue();
                rainbowRole.getManager().setColor(Color.decode("#FFFF00")).queue();
                rainbowRole.getManager().setColor(Color.decode("#00FF00")).queue();
                rainbowRole.getManager().setColor(Color.decode("#0000FF")).queue();
                rainbowRole.getManager().setColor(Color.decode("#4B0082")).queue();
                rainbowRole.getManager().setColor(Color.decode("#9400D3")).queue();

                if (x == length) {
                    rainbowRole.getManager().setColor(originalRankColor).queueAfter(20, TimeUnit.SECONDS);
                }

            }

        }

    }
}
