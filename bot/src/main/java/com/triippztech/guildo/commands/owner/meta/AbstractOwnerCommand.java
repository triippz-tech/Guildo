package com.triippztech.guildo.commands.owner.meta;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.triippztech.guildo.commands.AbstractCommand;
import com.triippztech.guildo.config.ApplicationProperties;
import com.triippztech.guildo.service.DiscordUserService;
import com.triippztech.guildo.service.ServiceManager;
import net.dv8tion.jda.api.Permission;

public abstract class AbstractOwnerCommand extends AbstractCommand {

    public AbstractOwnerCommand(ServiceManager serviceManager,
                                Permission... altPerms)
    {
        super(serviceManager);
        this.ownerCommand = true;
        this.guildOnly = false;
        this.hidden = true;
        this.category = new Category("Moderation", event ->
        {
            if(event.getGuild()==null)
            {
                event.replyError("This command is not available in Direct Messages!");
                return false;
            }

            boolean missingPerms = false;
            for(Permission altPerm: altPerms)
            {
                if(altPerm.isText())
                {
                    if(!event.getMember().hasPermission(event.getTextChannel(), altPerm))
                        missingPerms = true;
                }
                else
                {
                    if(!event.getMember().hasPermission(altPerm))
                        missingPerms = true;
                }
            }
            if(!missingPerms)
                return true;
            if(event.getMember().getRoles().isEmpty())
                event.replyError("You must have the following permissions to use that: "+listPerms(altPerms));
            else
                event.replyError("You must have the following permissions to use that: "+listPerms(altPerms));
            return false;
        });
    }

    public abstract void doCommand(CommandEvent event);

    private static String listPerms(Permission... perms)
    {
        if(perms.length==0)
            return "";
        StringBuilder sb = new StringBuilder(perms[0].getName());
        for(int i=1; i<perms.length; i++)
            sb.append(", ").append(perms[i].getName());
        return sb.toString();
    }
}
