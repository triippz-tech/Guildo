package com.triippztech.guildo.commands.general;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.triippztech.guildo.commands.AbstractCommand;
import com.triippztech.guildo.service.ServiceManager;
import org.springframework.stereotype.Component;
import java.time.temporal.ChronoUnit;

@Component
public class PingCmd extends AbstractCommand {
    private final ServiceManager serviceManager;

    public PingCmd(ServiceManager serviceManager) {
        super(serviceManager);
        this.serviceManager = serviceManager;

        this.category = new Category("General");
        this.name = "ping";
        this.help = "Displays Guildo's latency";
        this.guildOnly = true;
        this.cooldown = 60;
        this.cooldownScope = CooldownScope.GUILD;
    }

    @Override
    public void doCommand(CommandEvent event) {
        event.reply("Ping: ...", m -> {
            long ping = event.getMessage().getTimeCreated().until(m.getTimeCreated(), ChronoUnit.MILLIS);
            m.editMessage("Ping: " + ping  + "ms | Websocket: " + event.getJDA().getGatewayPing() + "ms").queue();
        });
    }
}
