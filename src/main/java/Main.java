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

        // add listeners to enable commands
        JDA.addEventListeners(new Main());

        JDA.build();

    }


    @Override
    public void onReady(@NotNull ReadyEvent event) {

        System.out.println("=====================================");
        System.out.println("DISCORD BOT");
        System.out.println("Author: https://github.com/miloszr7");
        System.out.println("=====================================");

    }
}
