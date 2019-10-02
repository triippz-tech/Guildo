import { Moment } from 'moment';
import { IGuildServer } from 'app/shared/model/bot/guild-server.model';
import { Activity } from 'app/shared/model/enumerations/activity.model';

export interface IServerLogItem {
  id?: number;
  activity?: Activity;
  channelId?: number;
  channelName?: string;
  time?: Moment;
  userId?: number;
  userName?: string;
  guildId?: number;
  serverItemGuildServer?: IGuildServer;
}

export const defaultValue: Readonly<IServerLogItem> = {};
