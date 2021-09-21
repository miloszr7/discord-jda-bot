package inventory;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// DISPLAY PLAYER'S INVENTORY + COUNT DUPLICATED ITEMS
public class InventoryCore extends ListenerAdapter {
    
    private DatabaseConnection connection = null;

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        try {

            if (event.getMessage().getContentRaw().equalsIgnoreCase("$inventory")) {

                String query = "SELECT * FROM inventory WHERE userID = " + event.getAuthor().getIdLong() + " ";

                ResultSet resultSet = connection.resultSet(query);

                StringBuilder builder = new StringBuilder();

                EmbedBuilder message = new EmbedBuilder();

                message.setColor(Color.CYAN);
                message.setFooter(event.getAuthor().getAsTag());

                ArrayList<String> itemList = new ArrayList<>();

                while (resultSet.next()) {
                    itemList.add(resultSet.getString("itemName"));
                }

                // close connection and result
                resultSet.close();
                connection.getConnection().close();

                // Place our items inside HashMap, add number to each one
                HashMap<String, Integer> nameAndCount = new HashMap<>();

                for (String name : itemList) {
                    Integer count = nameAndCount.get(name);
                    if (count == null) {
                        nameAndCount.put(name, 1);
                    } else {
                        nameAndCount.put(name, ++count);
                    }
                }

                // Retrieve values and place them inside StringBuilder
                Set<Map.Entry<String, Integer>> entrySet = nameAndCount.entrySet();

                for (Map.Entry<String, Integer> entry : entrySet) {
                    builder.append("**").append(entry.getValue()).append("**x ").append(entry.getKey()).append("\n");
                }

                message.setTitle("**Inventory**");

                message.setThumbnail("https://i.imgur.com/LQ3PZja.png");

                if (itemList.isEmpty() || itemList == null) {

                    message.setDescription("`You dont have any items`");

                } else {

                    message.setDescription(builder);

                }

                event.getChannel().sendMessage(message.build()).queue();

            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }
}
