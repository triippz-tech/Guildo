import { Moment } from 'moment';
import { IGuildServer } from 'app/shared/model/bot/guild-server.model';

export interface IGuildEvent {
  id?: number;
  eventName?: string;
  eventImageUrl?: string;
  eventMessage?: any;
  eventStart?: Moment;
  guildId?: number;
  eventGuild?: IGuildServer;
}

export const defaultValue: Readonly<IGuildEvent> = {};
