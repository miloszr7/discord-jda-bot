package privateMessages;

import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Respond extends ListenerAdapter {

    private final List<String> members = new ArrayList<>();

    private boolean getMembers(String id) {

        for (String u : members) {
            if (u.equals(id)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {

        if (!event.getAuthor().isBot()) {
            if (!getMembers(event.getAuthor().getId())) {
                members.add(event.getAuthor().getId());
                event.getChannel().sendMessage(event.getAuthor().getAsMention() +
                        " I dont respond to private messages," +
                        " you need to add me to your server and then use commands."
                ).queue();
            }
        }

    }
}
