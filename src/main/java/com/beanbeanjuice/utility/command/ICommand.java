package com.beanbeanjuice.utility.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * An interface used for {@link SlashCommandInteractionEvent}.
 *
 * @author beanbeanjuice
 * @since v1.0.0
 */
public interface ICommand {

    enum CommandCategory {

        GENERIC("Generic commands.", "https://cdn.beanbeanjuice.com/images/cafeBot/category_type/generic.png"),
        CAFE("Commands used for the cafe.", "https://cdn.beanbeanjuice.com/images/cafeBot/category_type/cafe.jpg"),
        FUN("Commands used for fun.", "https://cdn.beanbeanjuice.com/images/cafeBot/category_type/fun.jpg"),
        GAMES("Commands used for small games.", "https://cdn.beanbeanjuice.com/images/cafeBot/category_type/games.png"),
        SOCIAL("Commands used for social media stuff", "https://cdn.beanbeanjuice.com/images/cafeBot/category_type/social.gif"),
        INTERACTION("Commands used for user interactions.", "https://cdn.beanbeanjuice.com/images/cafeBot/category_type/interaction.png"),
        TWITCH("Commands used for twitch.", "https://cdn.beanbeanjuice.com/images/cafeBot/category_type/twitch.jpg"),
        MODERATION("Commands used for moderation.", "https://cdn.beanbeanjuice.com/images/cafeBot/category_type/moderation.png"),
        SETTINGS("Commands used for bot settings.", "https://cdn.beanbeanjuice.com/images/cafeBot/category_type/settings.gif");

        private final String message;
        private final String link;

        CommandCategory(@NotNull String message, @NotNull String link) {
            this.message = message;
            this.link = link;
        }

        public String getMessage() {
            return message;
        }

        public String getLink() {
            return link;
        }

    }

    /**
     * The main method for running the command.
     * @param event The {@link SlashCommandInteractionEvent}.
     */
    void handle(@NotNull SlashCommandInteractionEvent event);

    /**
     * @return The description for the {@link ICommand}.
     */
    @NotNull
    String getDescription();

    /**
     * @return An example of how to use the {@link ICommand}.
     */
    @NotNull
    String exampleUsage();

    /**
     * @return The various options available for the {@link ICommand}.
     */
    @NotNull
    default ArrayList<OptionData> getOptions() {
        return new ArrayList<>();
    }

    /**
     * @return The {@link CommandCategory} for the {@link ICommand}.
     */
    @NotNull
    CommandCategory getCategoryType();

    /**
     * @return The {@link ArrayList<ISubCommand>} for the specified {@link ICommand}.
     */
    @NotNull
    default ArrayList<ISubCommand> getSubCommands() {
        return new ArrayList<>();
    }

    /**
     * Runs the {@link ISubCommand} for the specified {@link ICommand}.
     * @param subCommandName The {@link String name} of the {@link ISubCommand}.
     * @param event The {@link SlashCommandInteractionEvent event} that triggered the {@link ICommand}.
     */
    default void runSubCommand(@NotNull String subCommandName, @NotNull SlashCommandInteractionEvent event) {
        for (ISubCommand subCommand : getSubCommands()) {
            if (subCommand.getName().equals(subCommandName)) {
                subCommand.handle(event);
            }
        }
    }

    /**
     * @return True, if this command is allowed to be run in a DM.
     */
    @NotNull
    default Boolean allowDM() {
        return false;
    }

    /**
     * @return True, if this command should be hidden from others.
     */
    @NotNull
    default Boolean isHidden() {
        return false;
    }

    /**
     * @return The permissions members need to use this.
     */
    @Nullable
    default ArrayList<Permission> getPermissions() {
        return null;
    }

    /**
     * @return The {@link HashMap <String, ArrayList<String>>}.
     */
    @Nullable
    default HashMap<String, ArrayList<String>> getAutoComplete() {
        return null;
    }

}
