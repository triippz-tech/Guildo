package com.triippztech.guildo.service;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.triippztech.guildo.commands.CommandManager;
import com.triippztech.guildo.config.ApplicationProperties;
import com.triippztech.guildo.domain.enumeration.BotStatus;
import com.triippztech.guildo.listeners.CommandExceptionListener;
import com.triippztech.guildo.listeners.EventListener;
import com.triippztech.guildo.service.scheduled.ScheduledJobs;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class GuildoBotService {
    private final Logger log = LoggerFactory.getLogger(GuildoBotService.class);
    private final ServiceManager serviceManager;
    private final CommandManager commandManager;
    private ScheduledJobs scheduledJobs;
    private CommandExceptionListener commandExceptionListener;
    private EventListener eventListener;
    private EventWaiter waiter;
    private ScheduledExecutorService threadpool;
    private WebhookClient logwebhook;
    private ShardManager shardManager;
    private BotStatus botStatus = BotStatus.STOPPED;
    private CommandClient client;

    public GuildoBotService(ServiceManager serviceManager, CommandManager commandManager) throws LoginException {
        this.serviceManager = serviceManager;
        this.commandManager = commandManager;

        this.startBot();
    }

    private void startBot() throws LoginException {
        this.botStatus = BotStatus.STARTING;
        this.waiter = new EventWaiter(Executors.newSingleThreadScheduledExecutor(), false);
        this.threadpool = Executors.newScheduledThreadPool(50);
        if ( !this.serviceManager.getApplicationProperties().getDiscord().getLogWebookUrl().isEmpty() )
            this.logwebhook = new WebhookClientBuilder(this.serviceManager.getApplicationProperties().getDiscord().getLogWebookUrl()).build();

        this.initializeScheduleEvents();
        this.initializeListeners();
        this.buildCommandClient();
        this.buildShardManager();
        this.buildThreadPools();

        log.info("Guildo Bot started");
    }

    private void stopBot() {

    }

    private BotStatus restartBot() throws LoginException {
        this.botStatus = BotStatus.RESTARTING;
        this.stopBot();
        this.startBot();
        return this.botStatus;
    }

    private void initializeScheduleEvents() {
        log.info("Preparing scheduled events");
        this.scheduledJobs = new ScheduledJobs(this);
    }

    private void initializeListeners() {
        log.info("Initializing Event and Exception listeners for bot.");
        this.eventListener = new EventListener(this);
        this.commandExceptionListener = new CommandExceptionListener();
    }

    private void buildCommandClient() {
        log.info("Creating Command Client for Guildo Bot");
        this.client =  new CommandClientBuilder()
            .setPrefix( this.serviceManager.getApplicationProperties().getDiscord().getPrefix() )
            .setOwnerId( this.serviceManager.getApplicationProperties().getDiscord().getAuthorId() )
            .setServerInvite( this.serviceManager.getApplicationProperties().getDiscord().getServerInvite() )
            .setEmojis( this.serviceManager.getApplicationProperties().getDiscord().getEmojis().getSuccess(),
                this.serviceManager.getApplicationProperties().getDiscord().getEmojis().getWarning(),
                this.serviceManager.getApplicationProperties().getDiscord().getEmojis().getError())
            .setLinkedCacheSize(0)
            .setListener(this.commandExceptionListener)
            .setScheduleExecutor(threadpool)
            // Help consumer?
            .setDiscordBotsKey(this.serviceManager.getApplicationProperties().getDiscord().getToken())
            .addCommands(this.commandManager.getCommands())
            .build();
    }

    private void buildShardManager() throws LoginException {
        log.info("Building Shards for Guildo Bot");
        this.shardManager = new DefaultShardManagerBuilder()
            .setShardsTotal(Integer.parseInt(this.serviceManager.getApplicationProperties().getDiscord().getShards()))
            .setToken(this.serviceManager.getApplicationProperties().getDiscord().getToken())
            .addEventListeners(this.eventListener, this.client, waiter)
            .setStatus(OnlineStatus.DO_NOT_DISTURB)
            .setActivity(Activity.playing("loading..."))
            .setBulkDeleteSplittingEnabled(false)
            .setRequestTimeoutRetry(true)
            .setDisabledCacheFlags(EnumSet.of(CacheFlag.EMOTE, CacheFlag.ACTIVITY)) //TODO: dont disable GAME
            .build();
    }

    private void buildThreadPools() {
        this.threadpool.scheduleWithFixedDelay(System::gc, 12, 6, TimeUnit.HOURS);
    }

    public BotStatus getBotStatus() {
        return botStatus;
    }

    public EventWaiter getWaiter() {
        return waiter;
    }

    public ScheduledExecutorService getThreadpool() {
        return threadpool;
    }

    public WebhookClient getLogwebhook() {
        return logwebhook;
    }

    public ShardManager getShardManager() {
        return shardManager;
    }

    public JDA getJDA(Long guildId) {
        return shardManager.getGuildById(guildId).getJDA();
    }

    public ApplicationProperties getApplicationProperties() {
        return serviceManager.getApplicationProperties();
    }

    public ScheduledJobs getScheduledJobs() {
        return scheduledJobs;
    }

    public EventListener getEventListener() {
        return eventListener;
    }

    public CommandClient getClient() {
        return client;
    }

    public ServiceManager getServiceManager() {
        return serviceManager;
    }
}
