package com.triippztech.guildo.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.triippztech.guildo.config.ApplicationProperties;
import com.triippztech.guildo.service.DiscordUserService;
import com.triippztech.guildo.service.ServiceManager;

/**
 * This is our base command class. All commands and subsquent
 * abstract command classes should derive from this one.
 * This abstract is used to check details such as (blacklisted users)
 * prior to executing any command. This prevents reuse of code.
 *
 * The Abstract command is not a service, because it is not component
 * scanned, since it can't be instantiated with out a concrete subclass
 * think PongCommand extends AbstractCommand
 *
 * The subclass will dictate how this (The Parent Class) will get
 * its dependencies, such as the ApplicationProperties via constructor
 * parameters.
 *
 * @author Mark Tripoli
 */
public abstract class AbstractCommand extends Command {
    private final DiscordUserService discordUserService;
    private final ApplicationProperties applicationProperties;

    public AbstractCommand(ServiceManager serviceManager) {
        this.discordUserService = serviceManager.getDiscordUserService();
        this.applicationProperties = serviceManager.getApplicationProperties();
    }

    @Override
    protected void execute(CommandEvent event) {
        // Always ignore bots, the last thing we want is a loop of bots talking... SKYNET!
        if (event.getAuthor().isBot()) return;
        // Ensure user is not blacklisted
        if (discordUserService.isBlacklisted(event.getAuthor())) {
            event.replyInDm("You have been blacklist from using Guildo. If you feel this is a" +
                " mistake, please contact the creators via Discord" +
                applicationProperties.getDiscord().getServerInvite());
            return;
        }
        discordUserService.addCommand(event.getAuthor());
        doCommand(event);
    }

    public abstract void doCommand(CommandEvent event);
}
