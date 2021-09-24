package slash;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class DiceGame extends ListenerAdapter {

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {

        if (event.getName().equals("dice")) {
            diceRoll(event, 5);
        }

    }

    private void diceRoll(SlashCommandEvent event, int max) {

        int count = (int) event.getOption("count").getAsLong();

        if (count > max) {
            event.getInteraction().reply("You can roll a dice max **"+max+"** times.").setEphemeral(true).queue();
            return;
        }

        if (count <= max && count > 0) {

            StringBuilder desc = new StringBuilder();

            int sum = 0;

            for (int i = 0; i < count; i++) {

                int roll = (int) (Math.random() * 6) + 1;

                sum += roll;

                desc.append(":game_die: ").append("**").append(roll).append("**").append("\n");

            }

            event.getInteraction().replyEmbeds(
                    new EmbedBuilder()
                            .setTitle("A Dice Game")
                            .setDescription(desc + "\n" + "Result :: **" + sum + "**")
                            .setFooter(event.getMember().getUser().getAsTag())
                            .build()
            ).queue();

        } else {
            event.getInteraction().reply("Something went wrong - make sure to use command properly.")
                    .setEphemeral(true).queue();
        }

    }

}
