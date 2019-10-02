import { Moment } from 'moment';
import { IDiscordUser } from 'app/shared/model/bot/discord-user.model';
import { IGuildServer } from 'app/shared/model/bot/guild-server.model';

export interface ITempBan {
  id?: number;
  reason?: string;
  endTime?: Moment;
  guildId?: number;
  userId?: number;
  bannedUser?: IDiscordUser;
  tempBanGuildServer?: IGuildServer;
}

export const defaultValue: Readonly<ITempBan> = {};
