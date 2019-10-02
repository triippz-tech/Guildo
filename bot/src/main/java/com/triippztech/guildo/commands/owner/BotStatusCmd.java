package com.triippztech.guildo.commands.owner;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.triippztech.guildo.commands.owner.meta.AbstractOwnerCommand;
import com.triippztech.guildo.service.ServiceManager;
import net.dv8tion.jda.api.OnlineStatus;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class BotStatusCmd extends AbstractOwnerCommand {
    private final ServiceManager serviceManager;
    public BotStatusCmd(ServiceManager serviceManager) {
        super(serviceManager);
        this.serviceManager = serviceManager;

        this.name = "setstatus";
        this.help = "sets the status the bot displays";
        this.arguments = "<status>";
        this.guildOnly = false;
    }

    @Override
    public void doCommand(CommandEvent event) {
        try {
            OnlineStatus status = OnlineStatus.fromKey(event.getArgs());
            if(status==OnlineStatus.UNKNOWN)
            {
                event.replyError("Please include one of the following statuses: `ONLINE`, `IDLE`, `DND`, `INVISIBLE`");
            }
            else
            {
                Objects.requireNonNull(event.getJDA().getShardManager()).setStatus(status);
                event.replySuccess("Set the status to `"+status.getKey().toUpperCase()+"`");
            }
        } catch(Exception e) {
            event.reply(event.getClient().getError()+" The status could not be set!");
        }
    }
}
