package com.beanbeanjuice.utility.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * An interface used for sub {@link SlashCommandInteractionEvent}.
 * This extends off of {@link ICommand}.
 *
 * @author beanbeanjuice
 * @since v1.0.0
 */
public interface ISubCommand extends ICommand {

    /**
     * @return The {@link String name} of the {@link ISubCommand}.
     */
    @NotNull
    String getName();

}
