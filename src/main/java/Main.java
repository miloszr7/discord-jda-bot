import admin.ShowEmoji;
import ai.CharacterCore;
import api.FiveM;
import api.GIPHY;
import api.YouTube;
import commands.*;
import config.DatabaseConnection;
import config.ServerTracker;
import config.Tokens;
import economy.AccountCreation;
import economy.BankingSystem;
import games.dice;
import games.eightBall;
import misc.LoadPrivateMessages;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import other.HostileTakeover;
import privateMessages.Respond;
import ratelimit.Cooldown;
import slash.*;
import viberp.*;


public class Main extends ListenerAdapter {

    final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {

        String tokenDEV = Tokens.DISCORD_DEV_TOKEN;
        String token = Tokens.DISCORD_TOKEN;
        
        JDABuilder JDA = JDABuilder.createDefault(tokenDEV);

        JDA.addEventListeners(new Main());
        JDA.addEventListeners(new YouTube());
        JDA.addEventListeners(new ClearChatMessages());
        JDA.addEventListeners(new Slowmode());
        JDA.addEventListeners(new ChangeNickname());
        JDA.addEventListeners(new HackBan());
        JDA.addEventListeners(new SearchForMember());
        JDA.addEventListeners(new ShowServerBans());
        JDA.addEventListeners(new CleanMessages());
        JDA.addEventListeners(new Report());
        JDA.addEventListeners(new CheckIPAddress());
        JDA.addEventListeners(new KickMember());
        JDA.addEventListeners(new BanMember());
        JDA.addEventListeners(new ChangeActivity());
        JDA.addEventListeners(new UnbanMember());
        JDA.addEventListeners(new MemberPermissions());
        JDA.addEventListeners(new ShowEmotes());
        JDA.addEventListeners(new ShowCommands());
        JDA.addEventListeners(new DisplayMemberAvatar());
        JDA.addEventListeners(new dice());
        JDA.addEventListeners(new eightBall());
        JDA.addEventListeners(new Ping());
        JDA.addEventListeners(new GIPHY());
        JDA.addEventListeners(new FlipCoin());
        JDA.addEventListeners(new AccountCreation());
        JDA.addEventListeners(new BankingSystem());
        JDA.addEventListeners(new Invitation());
        JDA.addEventListeners(new SendDiscordInvitation());
        JDA.addEventListeners(new MuteMember());
        JDA.addEventListeners(new Announce());
        JDA.addEventListeners(new FiveM());
        JDA.addEventListeners(new LoadPrivateMessages());
        JDA.addEventListeners(new Cooldown());
        JDA.addEventListeners(new Vote());
        JDA.addEventListeners(new ShowEmoji());
        JDA.addEventListeners(new ServerTracker());
        JDA.addEventListeners(new VendingMachine());

        JDA.addEventListeners(new HostileTakeover());

        JDA.addEventListeners(new Respond());
        JDA.addEventListeners(new CharacterCore());

        JDA.build();

    }


    @Override
    public void onReady(@NotNull ReadyEvent event) {

        System.out.println("=====================================");
        System.out.println("DISCORD 'GLaDOS' BOT");
        System.out.println("\u00A9 2021 All rights reserved.");
        System.out.println("Author: https://github.com/miloszr7");
        System.out.println("=====================================");

        DatabaseConnection.connect();

    }
}