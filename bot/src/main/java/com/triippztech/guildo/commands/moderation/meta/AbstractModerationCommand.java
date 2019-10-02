package com.triippztech.guildo.commands.moderation.meta;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.triippztech.guildo.commands.AbstractCommand;
import com.triippztech.guildo.config.ApplicationProperties;
import com.triippztech.guildo.service.DiscordUserService;
import com.triippztech.guildo.service.ServiceManager;
import net.dv8tion.jda.api.Permission;

public abstract class AbstractModerationCommand extends AbstractCommand {

    public AbstractModerationCommand(ServiceManager serviceManager, Permission[] botPermissions) {
        super(serviceManager);

        this.category = new Category("Moderation");
        this.ownerCommand = false;
        this.guildOnly = true;
        this.hidden = false;
        this.botPermissions = botPermissions;
    }

    public abstract void doCommand(CommandEvent event);

}
