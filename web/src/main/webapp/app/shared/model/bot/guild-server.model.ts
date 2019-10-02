import { IGuildServerProfile } from 'app/shared/model/bot/guild-server-profile.model';
import { IGuildApplicationForm } from 'app/shared/model/bot/guild-application-form.model';
import { IGuildServerSettings } from 'app/shared/model/bot/guild-server-settings.model';
import { IWelcomeMessage } from 'app/shared/model/bot/welcome-message.model';
import { IGuildPoll } from 'app/shared/model/bot/guild-poll.model';
import { IScheduledAnnouncement } from 'app/shared/model/bot/scheduled-announcement.model';
import { IGuildEvent } from 'app/shared/model/bot/guild-event.model';
import { IGiveAway } from 'app/shared/model/bot/give-away.model';
import { IModerationLogItem } from 'app/shared/model/bot/moderation-log-item.model';
import { IServerLogItem } from 'app/shared/model/bot/server-log-item.model';
import { ITempBan } from 'app/shared/model/bot/temp-ban.model';
import { IMute } from 'app/shared/model/bot/mute.model';
import { IGuildApplication } from 'app/shared/model/bot/guild-application.model';
import { GuildServerLevel } from 'app/shared/model/enumerations/guild-server-level.model';

export interface IGuildServer {
  id?: number;
  guildId?: number;
  guildName?: string;
  icon?: string;
  owner?: number;
  serverLevel?: GuildServerLevel;
  guildProfile?: IGuildServerProfile;
  applicationForm?: IGuildApplicationForm;
  guildSettings?: IGuildServerSettings;
  welcomeMessage?: IWelcomeMessage;
  serverPolls?: IGuildPoll[];
  guildAnnoucements?: IScheduledAnnouncement[];
  guildEvents?: IGuildEvent[];
  giveAways?: IGiveAway[];
  modLogItems?: IModerationLogItem[];
  serverLogItems?: IServerLogItem[];
  guildTempBans?: ITempBan[];
  mutedUsers?: IMute[];
  guildApplications?: IGuildApplication[];
}

export const defaultValue: Readonly<IGuildServer> = {};
