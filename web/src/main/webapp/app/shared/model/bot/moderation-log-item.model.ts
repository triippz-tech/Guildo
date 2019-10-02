import { Moment } from 'moment';
import { IGuildServer } from 'app/shared/model/bot/guild-server.model';
import { PunishmentType } from 'app/shared/model/enumerations/punishment-type.model';

export interface IModerationLogItem {
  id?: number;
  channelId?: number;
  channelName?: string;
  issuedById?: number;
  issuedByName?: string;
  issuedToId?: number;
  issuedToName?: string;
  reason?: string;
  time?: Moment;
  moderationAction?: PunishmentType;
  guildId?: number;
  modItemGuildServer?: IGuildServer;
}

export const defaultValue: Readonly<IModerationLogItem> = {};
