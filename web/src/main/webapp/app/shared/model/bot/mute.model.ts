import { IDiscordUser } from 'app/shared/model/bot/discord-user.model';
import { IGuildServer } from 'app/shared/model/bot/guild-server.model';

export interface IMute {
  id?: number;
  reason?: string;
  endTime?: string;
  guildId?: number;
  userId?: number;
  mutedUser?: IDiscordUser;
  mutedGuildServer?: IGuildServer;
}

export const defaultValue: Readonly<IMute> = {};
