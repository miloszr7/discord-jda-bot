package music;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class Premium {

    private final long[] premiumServers = {
        3333333333333L
    };

    private final String[] premiumMembers = {
            "3333333333333"
    };

    private final String url = "jdbc:sqlite:database.db";

    public boolean isPremium(String userID) {

        for (String s : premiumMembers) {
            if (s.contains(userID)) {
                return true;
            }
        }

        return false;
    }

    public boolean isPremium(@NotNull GuildMessageReceivedEvent event, long serverID) {

        for (long ID : premiumServers) {
              if (serverID == ID) {
                  return true;
              }
        }

        //event.getChannel().sendMessage("Your server does not have a premium features.").queue();
        return false;
    }

}
