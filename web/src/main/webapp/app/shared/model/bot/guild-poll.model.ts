import { Moment } from 'moment';
import { IGuildPollItem } from 'app/shared/model/bot/guild-poll-item.model';
import { IGuildServer } from 'app/shared/model/bot/guild-server.model';

export interface IGuildPoll {
  id?: number;
  pollName?: string;
  description?: string;
  textChannelId?: number;
  finishTime?: Moment;
  completed?: boolean;
  guildId?: number;
  pollItems?: IGuildPollItem;
  pollServer?: IGuildServer;
}

export const defaultValue: Readonly<IGuildPoll> = {
  completed: false
};
