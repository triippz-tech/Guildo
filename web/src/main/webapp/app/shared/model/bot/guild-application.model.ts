import { IDiscordUser } from 'app/shared/model/bot/discord-user.model';
import { IGuildServer } from 'app/shared/model/bot/guild-server.model';
import { ApplicationStatus } from 'app/shared/model/enumerations/application-status.model';

export interface IGuildApplication {
  id?: number;
  characterName?: string;
  characterType?: string;
  applicationFileContentType?: string;
  applicationFile?: any;
  status?: ApplicationStatus;
  acceptedBy?: IDiscordUser;
  appliedUser?: IDiscordUser;
  guildServer?: IGuildServer;
}

export const defaultValue: Readonly<IGuildApplication> = {};
