import { GuildType } from 'app/shared/model/enumerations/guild-type.model';
import { GuildPlayStyle } from 'app/shared/model/enumerations/guild-play-style.model';

export interface IGuildServerProfile {
  id?: number;
  guildType?: GuildType;
  playStyle?: GuildPlayStyle;
  description?: string;
  website?: string;
  discordUrl?: string;
}

export const defaultValue: Readonly<IGuildServerProfile> = {};
