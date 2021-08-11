package games;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class dice extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

            String[] command = event.getMessage().getContentRaw().split(" ");

            if (command.length == 2 && command[0].equalsIgnoreCase("$roll")) {

                int roll_times = Integer.parseInt(command[1]);

                event.getChannel().sendMessage(event.getAuthor().getAsMention()).complete().delete().queueAfter(9, TimeUnit.SECONDS);

                if (roll_times <= 3) {

                    int sum = 0;

                    for (int i = 0; i < roll_times; i++) {

                        int roll = (int) (Math.random() * 6) + 1;

                        sum += roll;

                        event.getChannel().sendTyping().complete();

                        event.getChannel().sendMessage("Your dice landed on " + "`" + roll + "`" + " :game_die:").complete().delete().queueAfter(8, TimeUnit.SECONDS);

                    }

                    event.getChannel().sendMessage("**TOTAL**: " + sum).complete().delete().queueAfter(7, TimeUnit.SECONDS);


                } else if (roll_times <= 0) {

                    event.getChannel().sendMessage("**You can't roll your dice less than once**.").complete().delete().queueAfter(7, TimeUnit.SECONDS);

                } else if (roll_times > 3) {

                    event.getChannel().sendMessage("**You can't roll your dice more than 3 times**.").complete().delete().queueAfter(7, TimeUnit.SECONDS);

                }


            }


    }
}
