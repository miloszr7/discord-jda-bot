package inventory;

import config.DatabaseConnection;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class InventoryAdmin extends ListenerAdapter {

    private final String admin = "";

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        try {

            String[] command = event.getMessage().getContentRaw().split(" ");

            // *giveitem @mention ITEM_NAME

            if (command.length == 3 && command[0].equalsIgnoreCase("*item")
                    && event.getAuthor().getId().equals(admin)) {

                Member member = event.getMessage().getMentionedMembers().get(0);

                DatabaseConnection connection = new DatabaseConnection();

                connection.query("INSERT INTO inventory VALUES(null, "+member.getId()+", "+member.getUser().getAsTag()+", "+command[2]+")");

                event.getChannel().sendMessage(
                        new EmbedBuilder()
                        .setDescription("I've added `"+command[2]+ "` for " + member.getAsMention())
                        .build()
                ).queue();

            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }


    }
}
