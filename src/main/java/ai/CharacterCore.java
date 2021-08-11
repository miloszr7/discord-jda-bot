package ai;

import config.Tokens;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.net.URL;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class CharacterCore extends ListenerAdapter {

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        if (event.getMessage().getContentRaw().startsWith("& ")
                && Settings.isWhitelisted(event.getGuild().getId())) {

            try {

                OkHttpClient client = new OkHttpClient();

                URL website = new URL("https://api-inference.huggingface.co/models/manav/dialogpt-large-kanye-reddit");

                String json = "{\"generated_text\": \""+event.getMessage().getContentRaw()+"\";}";

                RequestBody body = RequestBody.create(JSON, json);

                Request request = new Request.Builder()
                        .url(website)
                        .header("Authorization", "Bearer " + Tokens.HUGGING_FACE_TOKEN)
                        .post(body)
                        .build();

                String content ;

                JSONObject object ;

                try (Response response = client.newCall(request).execute()) {

                    content = Objects.requireNonNull(response.body()).string();

                    object = new JSONObject(content);

                }

                String message = object.getString("generated_text");

                event.getChannel().sendTyping().queue();

                event.getChannel().sendMessage(message).queueAfter(1, TimeUnit.SECONDS);

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

        }

    }
}
