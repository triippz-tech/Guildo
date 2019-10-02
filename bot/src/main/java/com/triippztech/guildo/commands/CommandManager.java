package com.triippztech.guildo.commands;

import com.jagrosh.jdautilities.command.Command;
import com.triippztech.guildo.commands.general.*;
import com.triippztech.guildo.commands.moderation.BanCmd;
import com.triippztech.guildo.commands.moderation.ModLogCmd;
import com.triippztech.guildo.commands.moderation.UnBanCmd;
import com.triippztech.guildo.commands.owner.BlacklistCmd;
import com.triippztech.guildo.commands.owner.BotStatusCmd;
import org.springframework.stereotype.Component;

@Component
public class CommandManager {
    private final PingCmd pingCmd;
    private final LookupCmd lookupCmd;
    private final InviteCmd inviteCmd;
    private final ServerInfoCmd serverInfoCmd;
    private final AboutCmd aboutCmd;
    private final BlacklistCmd blacklistCmd;
    private final BotStatusCmd botStatusCmd;
    private final BanCmd banCmd;
    private final UnBanCmd unBanCmd;
    private final ModLogCmd modLogCmd;

    public CommandManager(PingCmd pingCmd, LookupCmd lookupCmd, InviteCmd inviteCmd,
                          ServerInfoCmd serverInfoCmd, AboutCmd aboutCmd,
                          BlacklistCmd blacklistCmd, BotStatusCmd botStatusCmd,
                          BanCmd banCmd, UnBanCmd unBanCmd, ModLogCmd modLogCmd) {
        this.pingCmd = pingCmd;
        this.lookupCmd = lookupCmd;
        this.inviteCmd = inviteCmd;
        this.serverInfoCmd = serverInfoCmd;
        this.aboutCmd = aboutCmd;
        this.blacklistCmd = blacklistCmd;
        this.botStatusCmd = botStatusCmd;
        this.banCmd = banCmd;
        this.unBanCmd = unBanCmd;
        this.modLogCmd = modLogCmd;
    }

    public Command[] getCommands() {
        return new Command[] {
            this.pingCmd,
            this.lookupCmd,
            this.aboutCmd,
            this.inviteCmd,
            this.serverInfoCmd,
            this.blacklistCmd,
            this.botStatusCmd,
            this.banCmd,
            this.unBanCmd,
            this.modLogCmd
        };
    }
}
