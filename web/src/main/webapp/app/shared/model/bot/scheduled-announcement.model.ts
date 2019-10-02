import { Moment } from 'moment';
import { IGuildServer } from 'app/shared/model/bot/guild-server.model';

export interface IScheduledAnnouncement {
  id?: number;
  annoucementTitle?: string;
  annoucementImgUrl?: string;
  annoucementMessage?: any;
  annoucementFire?: Moment;
  guildId?: number;
  annouceGuild?: IGuildServer;
}

export const defaultValue: Readonly<IScheduledAnnouncement> = {};
