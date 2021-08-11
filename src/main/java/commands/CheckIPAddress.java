package commands;

import ratelimit.Cooldown;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.net.InetAddress;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class CheckIPAddress extends ListenerAdapter {

    private final OkHttpClient client = new OkHttpClient();
    public Cooldown cooldown = new Cooldown();

    private final String ownerEmail = "";
    private final double Threshold = 0.99;
    private final String flags = "m";


    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent e) {

        try {

            String[] command = e.getMessage().getContentRaw().split(" ");

            if (command.length == 2 && command[0].equalsIgnoreCase("$checkIP") && !cooldown.hasCooldown(e.getMember())) {

                cooldown.activate(e, "$checkIP");

                e.getMessage().delete().queue();

                URL website = new URL("http://check.getipintel.net/check.php?ip=" + command[1] + "&contact=" + ownerEmail + "&flags=" + flags);

                Request request = new Request.Builder()
                        .url(website)
                        .get()
                        .build();

                String content = "";

                try (Response response = client.newCall(request).execute()) {

                    content = response.body().string();

                }

                    double output = Double.parseDouble(content);

                    EmbedBuilder message = new EmbedBuilder();

                    if (content.equals("-2")) {

                        message.setColor(Color.GRAY);
                        message.setDescription("Invalid IP address.");

                        e.getChannel().sendMessage(message.build()).queue();

                    } else if (content.equals("-3")) {

                        message.setColor(Color.GRAY);
                        message.setDescription("IP address is unroutable or private.");

                        e.getChannel().sendMessage(message.build()).queue();

                    } else if (content.equals("-4")) {

                        message.setColor(Color.GRAY);
                        message.setDescription("Unable to reach database at this moment, most likely database is being updated.");

                        e.getChannel().sendMessage(message.build()).queue();

                    } else {

                        if (output >= Threshold) {

                            message.setColor(Color.decode("#FF5C5C"));
                            message.setDescription("IP address is using VPN or Proxy.");

                            e.getChannel().sendMessage(message.build()).queue();

                        } else if (output < Threshold) {

                            message.setColor(Color.decode("#3CFFA4"));
                            message.setDescription("IP address is not using any VPN or Proxy.");

                            e.getChannel().sendMessage(message.build()).queue();

                        }

                    }


            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        try {

            String[] command = e.getMessage().getContentRaw().split(" ");

            if (command.length == 1 && command[0].equalsIgnoreCase("$ip")) {
                e.getMessage().delete().queue();
            } else if (command.length == 2 && command[0].equalsIgnoreCase("$ip")) {

                String url = command[1];

                if (url.contains("http:")) {

                    String replaced = url.replace("http://", "");

                    EmbedBuilder host = new EmbedBuilder();
                    host.setColor(Color.decode(CleanMessages.EmbedColor));
                    host.setTitle("**IP**: " + InetAddress.getByName(replaced).getHostAddress());
                    host.setDescription(
                            "**Local**: " + InetAddress.getByName(replaced).isAnyLocalAddress() + "\n" +
                                    "**Loopback**: " + InetAddress.getByName(replaced).isLoopbackAddress() + "\n" +
                                    "**Multicast**: " + InetAddress.getByName(replaced).isMulticastAddress() + "\n" +
                                    "**SiteLocal**: " + InetAddress.getByName(replaced).isSiteLocalAddress()
                    );
                    e.getChannel().sendMessage(host.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);


                } else if (url.contains("https:")) {

                    String replaced = url.replace("https://", "");

                    EmbedBuilder host = new EmbedBuilder();
                    host.setColor(Color.decode(CleanMessages.EmbedColor));
                    host.setTitle("**IP**: " + InetAddress.getByName(replaced).getHostAddress());
                    host.setDescription(
                            "**Local**: " + InetAddress.getByName(replaced).isAnyLocalAddress() + "\n" +
                                    "**Loopback**: " + InetAddress.getByName(replaced).isLoopbackAddress() + "\n" +
                                    "**Multicast**: " + InetAddress.getByName(replaced).isMulticastAddress() + "\n" +
                                    "**SiteLocal**: " + InetAddress.getByName(replaced).isSiteLocalAddress()
                    );
                    e.getChannel().sendMessage(host.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);

                } else {

                    EmbedBuilder host = new EmbedBuilder();
                    host.setColor(Color.decode(CleanMessages.EmbedColor));
                    host.setTitle("**IP**: " + InetAddress.getByName(url).getHostAddress());
                    host.setDescription(
                            "**Local**: " + InetAddress.getByName(url).isAnyLocalAddress() + "\n" +
                                    "**Loopback**: " + InetAddress.getByName(url).isLoopbackAddress() + "\n" +
                                    "**Multicast**: " + InetAddress.getByName(url).isMulticastAddress() + "\n" +
                                    "**SiteLocal**: " + InetAddress.getByName(url).isSiteLocalAddress()
                    );
                    e.getChannel().sendMessage(host.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);

                }

            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }
}
