package com.triippztech.guildo.service;

import club.minnced.discord.webhook.WebhookClient;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.triippztech.guildo.domain.enumeration.BotStatus;
import com.triippztech.guildo.listeners.CommandExceptionListener;
import com.triippztech.guildo.listeners.EventListener;
import com.triippztech.guildo.service.scheduled.ScheduledJobs;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledExecutorService;

@Component
public class BotSystems {
    private ScheduledJobs scheduledJobs;
    private CommandExceptionListener commandExceptionListener;
    private EventListener eventListener;
    private EventWaiter waiter;
    private ScheduledExecutorService threadpool;
    private WebhookClient logwebhook;
    private ShardManager shardManager;
    private BotStatus botStatus = BotStatus.STOPPED;
    private CommandClient client;

    public ScheduledJobs getScheduledJobs() {
        return scheduledJobs;
    }

    public void setScheduledJobs(ScheduledJobs scheduledJobs) {
        this.scheduledJobs = scheduledJobs;
    }

    public CommandExceptionListener getCommandExceptionListener() {
        return commandExceptionListener;
    }

    public void setCommandExceptionListener(CommandExceptionListener commandExceptionListener) {
        this.commandExceptionListener = commandExceptionListener;
    }

    public EventListener getEventListener() {
        return eventListener;
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public EventWaiter getWaiter() {
        return waiter;
    }

    public void setWaiter(EventWaiter waiter) {
        this.waiter = waiter;
    }

    public ScheduledExecutorService getThreadpool() {
        return threadpool;
    }

    public void setThreadpool(ScheduledExecutorService threadpool) {
        this.threadpool = threadpool;
    }

    public WebhookClient getLogwebhook() {
        return logwebhook;
    }

    public void setLogwebhook(WebhookClient logwebhook) {
        this.logwebhook = logwebhook;
    }

    public ShardManager getShardManager() {
        return shardManager;
    }

    public void setShardManager(ShardManager shardManager) {
        this.shardManager = shardManager;
    }

    public BotStatus getBotStatus() {
        return botStatus;
    }

    public void setBotStatus(BotStatus botStatus) {
        this.botStatus = botStatus;
    }

    public CommandClient getClient() {
        return client;
    }

    public void setClient(CommandClient client) {
        this.client = client;
    }
}
