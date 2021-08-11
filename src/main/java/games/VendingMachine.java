package games;

import economy.BankHandler;
import economy.MoneyFormat;
import ratelimit.Cooldown;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class VendingMachine extends ListenerAdapter {

    private final MoneyFormat format = new MoneyFormat();
    private final Cooldown cooldown = new Cooldown();
    private final BankHandler bankHandler = new BankHandler();

    private final String[] slots = {
            ":green_apple:", ":apple:", ":pear:",
            ":tangerine:", ":watermelon:", ":grapes:",
            ":blueberries:", ":cherries:", ":strawberry:"
    };

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

            String[] command = event.getMessage().getContentRaw().split(" ");

            if (command.length == 2 && command[0].equalsIgnoreCase("$vm") && !cooldown.hasCooldown(event.getMember())) {

                cooldown.activate(event, "$vm");

                int amount = Integer.parseInt(command[1]);

                if (amount < 1000) {

                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + " minimum amount you can bet is **$1000**.").queue();

                } else {

                    try {

                        if (bankHandler.verify(amount, event.getAuthor().getIdLong())) {

                            event.getChannel().sendMessage("**Launching vending machine**...").queue(edit -> {

                                int rOne = (int) (Math.random() * slots.length);
                                int rTwo = (int) (Math.random() * slots.length);
                                int rThree = (int) (Math.random() * slots.length);

                                String[] finalResult = new String[]{slots[rOne], slots[rTwo], slots[rThree]};

                                boolean loose = !finalResult[0].equals(finalResult[1]) && !finalResult[1].equals(finalResult[2]);
                                boolean WinOne = finalResult[0].equals(finalResult[1]);
                                boolean WinTwo = finalResult[1].equals(finalResult[2]);
                                boolean WinThree = finalResult[0].equals(finalResult[1]) && finalResult[1].equals(finalResult[2]);

                                String winTemplate = " " + finalResult[0] + " " + finalResult[1] + " " + finalResult[2] + "\n\n";
                                String footerTemplate = event.getAuthor().getAsTag();

                                if (WinOne || WinTwo) {

                                    double winAmount = amount * 6.5;

                                    edit.editMessage(winTemplate).queueAfter(3, TimeUnit.SECONDS);
                                    edit.editMessage(
                                            new EmbedBuilder()
                                                    .setTitle("**Vending Machine**")
                                                    .setDescription(
                                                             "\n :tada: You win **$" + format.comaFormat((int) winAmount) + "**"
                                                    )
                                                    .setColor(Color.CYAN)
                                                    .setFooter(footerTemplate)
                                                    .build()
                                    ).queueAfter(3, TimeUnit.SECONDS);

                                    try {
                                        bankHandler.addMoney(winAmount, event.getAuthor().getIdLong());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                                if (WinThree) {

                                    double winAmount = amount * 20.5;

                                    edit.editMessage(winTemplate).queueAfter(3, TimeUnit.SECONDS);
                                    edit.editMessage(
                                            new EmbedBuilder()
                                                    .setTitle("**Vending Machine**")
                                                    .setDescription(
                                                            ":tada: YOU WIN **$" + format.comaFormat((int) winAmount) + "**"
                                                    )
                                                    .setColor(Color.CYAN)
                                                    .setFooter(footerTemplate)
                                                    .build()
                                    ).queueAfter(3, TimeUnit.SECONDS);

                                    try {

                                        bankHandler.addMoney(winAmount, event.getAuthor().getIdLong());

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                                if (loose) {

                                    edit.editMessage(winTemplate).queueAfter(3, TimeUnit.SECONDS);
                                    edit.editMessage(
                                            new EmbedBuilder()
                                                    .setTitle("**Vending Machine**")
                                                    .setDescription(
                                                            "You lose **$" + format.comaFormat(amount) + "**"
                                                    )
                                                    .setColor(Color.CYAN)
                                                    .setFooter(footerTemplate)
                                                    .build()
                                    ).queueAfter(3, TimeUnit.SECONDS);
                                }

                            });

                        } else {
                            event.getChannel().sendMessage("**You do not have enough money to play**.").queue();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

    }
}
