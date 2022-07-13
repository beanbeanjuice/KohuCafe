package com.beanbeanjuice;

import com.beanbeanjuice.utility.logging.LogLevel;
import com.beanbeanjuice.utility.logging.LogManager;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;

/**
 * The main {@link JDA bot} class.
 *
 * @author beanbeanjuice
 * @since v1.0.0
 */
public class Bot {

    private static final Dotenv DOT_ENV = Dotenv.configure().load();

    public static final String BOT_VERSION = DOT_ENV.get("BOT_VERSION");
    public static final String BOT_USER_AGENT = "java:com.beanbeanjuice.KohuCafe:" + BOT_VERSION;

    private static final String BOT_TOKEN = DOT_ENV.get("BOT_TOKEN");
    private static final String GUILD_ID = DOT_ENV.get("GUILD_ID");
    private static final String LOG_CHANNEL_ID = DOT_ENV.get("LOG_CHANNEL_ID");
    private static final String WEBHOOK_URL = DOT_ENV.get("LOG_WEBHOOK_URL");
    // TODO: Add Twitch Access Token
    private static LogManager logger;
    private static JDA bot;
    private static Guild homeGuild;
    private static TextChannel homeGuildLogChannel;

    public static int commandsRun = 0;
    public static final String DISCORD_AVATAR_URL = "https://cdn.beanbeanjuice.com/images/cafeBot/cafeBot.gif";  // TODO: Change this later

    public static void main(String[] args) throws LoginException, InterruptedException {
        logger = new LogManager("cafeBot Logging System", homeGuildLogChannel, "logs/");
        logger.addWebhookURL(WEBHOOK_URL);
        logger.log(Bot.class, LogLevel.OKAY, "Starting bot!", true, false);

        bot = JDABuilder.createDefault(BOT_TOKEN)
                .setActivity(Activity.playing("The Cafe is starting..."))
                .setStatus(OnlineStatus.IDLE)
                .enableIntents(
                        GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.DIRECT_MESSAGES
                )
                .enableCache(
                        CacheFlag.EMOJI
                )
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setChunkingFilter(ChunkingFilter.ALL)
                .build()
                .awaitReady();

        homeGuild = bot.getGuildById(GUILD_ID);
        homeGuildLogChannel = homeGuild.getTextChannelById(LOG_CHANNEL_ID);

        // Log "Adding Commands"
        // Start new command handler
        // Add event listeners

        logger.setLogChannel(homeGuildLogChannel);
        logger.log(Bot.class, LogLevel.INFO, "Enabled Discord Logging...", true, true);

        // Start handlers

        bot.getPresence().setStatus(OnlineStatus.ONLINE);
        updateGuildPresence();
        logger.log(Bot.class, LogLevel.OKAY, "The bot is online!");
    }

    /**
     * @return The current {@link JDA bot}.
     */
    @NotNull
    public static JDA getBot() {
        return bot;
    }

    /**
     * @return The current {@link LogManager}.
     */
    @NotNull
    public static LogManager getLogger() {
        return logger;
    }

    /**
     * Updates the presence for the {@link JDA}.
     */
    public static void updateGuildPresence() {
        bot.getPresence().setActivity(Activity.playing("KohuCafe " + BOT_VERSION + " - Currently in " + bot.getGuilds().size() + " servers!"));
    }

}
