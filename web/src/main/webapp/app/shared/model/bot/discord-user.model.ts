import { IDiscordUserProfile } from 'app/shared/model/bot/discord-user-profile.model';
import { ITempBan } from 'app/shared/model/bot/temp-ban.model';
import { IMute } from 'app/shared/model/bot/mute.model';
import { IGuildApplication } from 'app/shared/model/bot/guild-application.model';
import { DiscordUserLevel } from 'app/shared/model/enumerations/discord-user-level.model';

export interface IDiscordUser {
  id?: number;
  userId?: number;
  userName?: string;
  icon?: string;
  commandsIssued?: number;
  blacklisted?: boolean;
  userLevel?: DiscordUserLevel;
  userProfile?: IDiscordUserProfile;
  userTempBans?: ITempBan[];
  userMutes?: IMute[];
  userApplications?: IGuildApplication[];
}

export const defaultValue: Readonly<IDiscordUser> = {
  blacklisted: false
};
