package admin;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContentVerification extends ListenerAdapter {

    private final String[] image_extensions = {
            "jpg", "png", "gif", "webp", "tiff",
            "psd", "raw", "bmp", "heif", "indd",
            "svg", "ai", "eps", "pdf"
    };

    private final String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";

    private void checkAttachment(@NotNull GuildMessageReceivedEvent event, TextChannel channel) {

        if (event.getMessage().getAttachments().size() > 0) {
            for (String ex : image_extensions) {
                if (event.getMessage().getAttachments().get(0).getFileExtension().equalsIgnoreCase(ex)) {

                    event.getGuild().getTextChannelById(channel.getId())
                            .sendMessage(event.getMessage().getAttachments().get(0).getUrl()).queue();

                    event.getMessage().delete().queue();

                }
            }
        }

    }

    private void checkMessageURL(@NotNull GuildMessageReceivedEvent event) throws Exception {

        Pattern pattern = Pattern.compile(URL_REGEX);
        Matcher matcher = pattern.matcher(event.getMessage().getContentRaw());

        if (matcher.find()) {

            URL url = new URL(event.getMessage().getContentRaw());

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("HEAD");

            System.out.println(connection.getContentType());

            connection.disconnect();

        }

    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        checkAttachment(event, event.getGuild().getTextChannelById("837722723950002208"));

        try {

            checkMessageURL(event);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }
}
