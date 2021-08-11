package data;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StoreMessages extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {

        try {

            Date date = new Date();
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("hh:mm:ss a");

            String pattern = "MM/dd/yyyy";
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(pattern);
            String updated = simpleDateFormat2.format(new Date());

            String title = event.getGuild().getId();

            FileWriter fileWriter = new FileWriter(title + ".txt", true);

            BufferedWriter content = new BufferedWriter(fileWriter);

            String channel = event.getChannel().getName();


            content.write(
                    "\n" + "[" + updated + "] [" + simpleDateFormat1.format(date) + "] (" + channel + ") " +
                            event.getAuthor().getName() + ": " + event.getMessage().getContentRaw()
            );

            content.close();

            fileWriter.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
