package data;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class StoreServerInfo extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        Date date = new Date(System.currentTimeMillis());

        String pattern = "MM-dd-yyyy";
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(pattern);
        String updated = simpleDateFormat1.format(new Date());


        try {
            String fileName = event.getGuild().getId();

            String guildName = event.getGuild().getName();
            String guildOwner = event.getGuild().getOwner().getUser().getName();
            String guildOwnerID = event.getGuild().getOwnerId();
            String guildRegion = event.getGuild().getRegion().getName();
            String VerificationLVL = event.getGuild().getVerificationLevel().toString();

            long guildID = event.getGuild().getIdLong();

            int MaxEmotes = event.getGuild().getMaxEmotes();
            int MaxMembers = event.getGuild().getMaxMembers();
            int MaxBitrate = event.getGuild().getMaxBitrate();
            int boosters = event.getGuild().getBoostCount();
            int boostTier = event.getGuild().getBoostTier().getKey();
            int guildMembers = event.getGuild().getMembers().size();
            int guildTextChannels = event.getGuild().getTextChannels().size();
            int guildVoiceChannels = event.getGuild().getVoiceChannels().size();
            int guildEmotes = event.getGuild().getEmotes().size();
            int guildCategories = event.getGuild().getCategories().size();
            int guildRoles = event.getGuild().getRoles().size();

            int totalServers = event.getJDA().getGuilds().size();
            int totalChannels = event.getJDA().getTextChannels().size();
            int totalMembers = 0;

            List<Guild> guildList = event.getJDA().getGuilds();

            for (Guild server : guildList) {
                totalMembers += server.getMemberCount();
            }

            PrintWriter printWriter1 = new PrintWriter(new FileWriter(fileName + ".html", false));

            StringBuilder G = new StringBuilder();

            G.append("<!DOCTYPE html>");
            G.append("<head>");
            G.append("<meta charset='utf-8'>");
            //G.append("<link rel='stylesheet' type='text/css' href='style.css'>");
            G.append("<link href='https://fonts.googleapis.com/css?family=Darker+Grotesque&display=swap' rel='stylesheet'>");
            G.append("<link href='https://fonts.googleapis.com/css?family=Source+Sans+Pro&display=swap' rel='stylesheet'>");
            G.append("<link href='https://fonts.googleapis.com/css?family=Roboto+Condensed&display=swap' rel='stylesheet'>");
            G.append("<style> a {\ttext-decoration: none;\tcolor: white;}body {\tbackground-color: #2C2F33;\tfont-family: 'Darker Grotesque', sans-serif;}.data {\tmargin: auto;\twidth: 50%;}.title {\tcolor: white;\ttext-align: center;\tfont-size: 50px;}::-webkit-scrollbar-track{    -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.3);    border-radius: 10px;    background-color: #2C2F33;}::-webkit-scrollbar{    width: 12px;    background-color: #2C2F33;}::-webkit-scrollbar-thumb{    border-radius: 10px;    -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,.3);    background-color: #555;}pre {\tcolor: #00E4FF;\tborder: 2px solid black;\tfont-family: 'Roboto Condensed', sans-serif;\tfont-size: 18px;\tpadding: 15px;\theight: 600px;\tmargin: auto;\twidth: 50%;\tbackground-color: #23272A;\toverflow: auto;}.footer {\ttext-align: center;\tfont-family: 'Roboto Condensed', sans-serif;\tcolor: white;\tfont-size: 24px;\tmargin-top: 1%;\t}.test {\t}footer {\ttext-align: center;\tfont-family: 'Roboto Condensed', sans-serif;\tcolor: white;\tfont-size: 20px;\tmargin-top: 1%;} </style>");
            G.append("</head>");
            G.append("<body>");
            G.append("<div class='title'><a href='" + guildID + ".txt' alt='Message Logs'>" + guildName + "</a></div>");
            G.append("<br>");
            G.append("<pre>");
            G.append("<ul>");
            G.append("<li> <b>Server Name</b>: <font color='#E0E0E0'>" + guildName + "</div>" + " (" + guildID + ")</font></li> \n");
            G.append("<li> <b>Created</b>: <font color='#E0E0E0'>" + event.getGuild().getTimeCreated().getMonthValue() + "/" + event.getGuild().getTimeCreated().getDayOfMonth() + "/" + event.getGuild().getTimeCreated().getYear() + "</font></li> \n");
            G.append("<li> <b>Leader</b>: <font color='#E0E0E0'>" + guildOwner + " (" + guildOwnerID + ")</font></li> \n");
            G.append("<li> <b>Region</b>: <font color='#E0E0E0'>" + guildRegion + "</font></li> \n");
            G.append("<li> <b>Verification</b>: <font color='#E0E0E0'>" + VerificationLVL + "</font></li> \n");
            G.append("<li> <b>Boost Tier</b>: <font color='#E0E0E0'>" + boostTier + "/3 </font></li> \n");
            G.append("<li> <b>Nitro Boosters</b>: <font color='#E0E0E0'>" + boosters + "</font></li> \n");
            G.append("<li> <b>Maximum Bitrate</b>: <font color='#E0E0E0'>" + MaxBitrate + "</font></li> \n");
            G.append("<li> <b>Maximum Members</b>: <font color='#E0E0E0'>" + MaxMembers + "</font></li> \n");
            G.append("<li> <b>Members</b>: <font color='#E0E0E0'>" + guildMembers + "</font></li> \n");
            G.append("<li> <b>Roles</b>: <font color='#E0E0E0'>" + guildRoles + "</font></li> \n");
            G.append("<li> <b>Categories</b>: <font color='#E0E0E0'>" + guildCategories + "</font></li> \n");
            G.append("<li> <b>Text Channels</b>: <font color='#E0E0E0'>" + guildTextChannels + "</font></li> \n");
            G.append("<li> <b>Voice Channels</b>: <font color='#E0E0E0'>" + guildVoiceChannels + "</font></li> \n");
            G.append("<li> <b>Maximum Emotes</b>: <font color='#E0E0E0'>" + MaxEmotes + "</font></li> \n");
            G.append("<li> <b>Emotes</b>: <font color='#E0E0E0'>" + guildEmotes + "</font></li> \n");
            G.append("</ul>");
            G.append("</pre>");
            G.append("<div class='footer'><b><br><font color='#00DCFF'>" + updated + "</font><br><font color='#009FB9'>" + simpleDateFormat.format(date) + "</font></b></div>");
            G.append("<br>");
            G.append("<footer><b> © 2020 GLaDOS </b></footer>");
            G.append("</body>");
            G.append("</html>");

            String guild = G.toString();

            printWriter1.println(guild);

            printWriter1.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
