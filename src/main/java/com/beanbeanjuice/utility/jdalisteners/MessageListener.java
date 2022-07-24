package com.beanbeanjuice.utility.jdalisteners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link ListenerAdapter} used to listen for messages.
 *
 * @author beanbeanjuice
 * @since v1.0.0
 */
public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        // Ignore message if it is a bot.
        if (event.getAuthor().isBot())
            return;
    }

}
