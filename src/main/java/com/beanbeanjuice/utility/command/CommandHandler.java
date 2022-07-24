package com.beanbeanjuice.utility.command;

import com.beanbeanjuice.Bot;
import com.beanbeanjuice.command.generic.PingCommand;
import com.beanbeanjuice.utility.logging.LogLevel;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * A {@link ListenerAdapter} used to listen for {@link SlashCommandInteractionEvent}.
 *
 * @author beanbeanjuice
 * @since v1.0.0
 */
public class CommandHandler extends ListenerAdapter {

    private final TreeMap<String, ICommand> commands;

    /**
     * Creates a new {@link CommandHandler} object.
     * @param jda The current {@link JDA}.
     */
    public CommandHandler(@NotNull JDA jda) {
        commands = new TreeMap<>();
        List<SlashCommandData> slashCommands = new ArrayList<>();

        // ========================
        //     COMMANDS GO HERE
        // ========================

        // Cafe

        // Fun

        // Games

        // Generic
        commands.put("ping", new PingCommand());

        // Interaction

        // Moderation

        // Settings

        // Social

        // Twitch

        // =======================
        //     END OF COMMANDS
        // =======================

        commands.forEach((commandName, command) -> {
            SlashCommandData slashCommandData = Commands.slash(commandName, command.getDescription());
            slashCommandData.setGuildOnly(!command.allowDM());
            slashCommandData.addOptions(command.getOptions());

            // Setting the permissions for commands.
            if (command.getPermissions() != null)
                slashCommandData.setDefaultPermissions(DefaultMemberPermissions.enabledFor(command.getPermissions()));

            List<SubcommandData> subCommands = new ArrayList<>();

            for (ISubCommand subCommand : command.getSubCommands()) {
                SubcommandData subCommandData = new SubcommandData(subCommand.getName(), subCommand.getDescription());
                subCommandData.addOptions(subCommand.getOptions());
                subCommands.add(subCommandData);
            }

            // Adding sub commands if applicable.
            slashCommandData.addSubcommands(subCommands);
            slashCommands.add(slashCommandData);
        });

        jda.updateCommands().addCommands(slashCommands).queue((e) -> {
            Bot.getLogger().log(this.getClass(), LogLevel.INFO, "Waiting for slash commands to propagate.", false, false);
        });
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        super.onSlashCommandInteraction(event);

        // Checking if the commands is something that should be run.
        if (commands.containsKey(event.getName())) {

            // Log the command.
            logCommand(event);

            // Checks if the reply should be hidden or not.
            if (commands.get(event.getName()).isHidden())
                event.deferReply(true).queue();
            else
                event.deferReply().queue();

            // Checks if it IS a sub command.
            if (event.getSubcommandName() != null)
                commands.get(event.getName()).runSubCommand(event.getSubcommandName(), event);
            else
                commands.get(event.getName()).handle(event);

            // Increment commands run for this bot.
            Bot.commandsRun++;
        }
    }

    private void logCommand(@NotNull SlashCommandInteractionEvent event) {
        String commandName = event.getName();
        StringBuilder commandString = new StringBuilder(commandName);

        if (event.getSubcommandName() != null)
            commandString.append(" ").append(event.getSubcommandName());

        for (int i = 0; i < event.getOptions().size(); i++) {
            OptionMapping optionMapping = event.getOptions().get(i);

            // Check if the mapping is null
            if (optionMapping != null) {
                String type = optionMapping.getType().toString();
                String value = optionMapping.getAsString();
                commandString.append(" <").append(type).append(":").append(value).append(">");
            }
        }

        Bot.getLogger().log(commands.get(event.getName()).getClass(), LogLevel.DEBUG, commandString.toString(), false, false);
    }

    @NotNull
    public TreeMap<String, ICommand> getCommands() {
        return commands;
    }

}
