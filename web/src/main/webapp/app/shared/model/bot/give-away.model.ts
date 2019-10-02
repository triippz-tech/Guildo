import { Moment } from 'moment';
import { IDiscordUser } from 'app/shared/model/bot/discord-user.model';
import { IGuildServer } from 'app/shared/model/bot/guild-server.model';

export interface IGiveAway {
  id?: number;
  name?: string;
  image?: string;
  message?: any;
  messageId?: number;
  textChannelId?: number;
  finish?: Moment;
  expired?: boolean;
  guildId?: number;
  winner?: IDiscordUser;
  guildGiveAway?: IGuildServer;
}

export const defaultValue: Readonly<IGiveAway> = {
  expired: false
};
