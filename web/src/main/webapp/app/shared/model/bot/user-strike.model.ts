import { IDiscordUser } from 'app/shared/model/bot/discord-user.model';

export interface IUserStrike {
  id?: number;
  count?: number;
  userId?: number;
  guildId?: number;
  discordUser?: IDiscordUser;
}

export const defaultValue: Readonly<IUserStrike> = {};
