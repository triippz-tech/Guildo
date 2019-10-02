import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale, { LocaleState } from './locale';
import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from './user-management';
// prettier-ignore
import autoModAntiDup, {
  AutoModAntiDupState
} from 'app/entities/bot/auto-mod-anti-dup/auto-mod-anti-dup.reducer';
// prettier-ignore
import autoModAutoRaid, {
  AutoModAutoRaidState
} from 'app/entities/bot/auto-mod-auto-raid/auto-mod-auto-raid.reducer';
// prettier-ignore
import autoModIgnore, {
  AutoModIgnoreState
} from 'app/entities/bot/auto-mod-ignore/auto-mod-ignore.reducer';
// prettier-ignore
import autoModMentions, {
  AutoModMentionsState
} from 'app/entities/bot/auto-mod-mentions/auto-mod-mentions.reducer';
// prettier-ignore
import autoModeration, {
  AutoModerationState
} from 'app/entities/bot/auto-moderation/auto-moderation.reducer';
// prettier-ignore
import discordUser, {
  DiscordUserState
} from 'app/entities/bot/discord-user/discord-user.reducer';
// prettier-ignore
import discordUserProfile, {
  DiscordUserProfileState
} from 'app/entities/bot/discord-user-profile/discord-user-profile.reducer';
// prettier-ignore
import giveAway, {
  GiveAwayState
} from 'app/entities/bot/give-away/give-away.reducer';
// prettier-ignore
import guildApplication, {
  GuildApplicationState
} from 'app/entities/bot/guild-application/guild-application.reducer';
// prettier-ignore
import guildApplicationForm, {
  GuildApplicationFormState
} from 'app/entities/bot/guild-application-form/guild-application-form.reducer';
// prettier-ignore
import guildEvent, {
  GuildEventState
} from 'app/entities/bot/guild-event/guild-event.reducer';
// prettier-ignore
import guildPoll, {
  GuildPollState
} from 'app/entities/bot/guild-poll/guild-poll.reducer';
// prettier-ignore
import guildPollItem, {
  GuildPollItemState
} from 'app/entities/bot/guild-poll-item/guild-poll-item.reducer';
// prettier-ignore
import guildServer, {
  GuildServerState
} from 'app/entities/bot/guild-server/guild-server.reducer';
// prettier-ignore
import guildServerProfile, {
  GuildServerProfileState
} from 'app/entities/bot/guild-server-profile/guild-server-profile.reducer';
// prettier-ignore
import guildServerSettings, {
  GuildServerSettingsState
} from 'app/entities/bot/guild-server-settings/guild-server-settings.reducer';
// prettier-ignore
import moderationLogItem, {
  ModerationLogItemState
} from 'app/entities/bot/moderation-log-item/moderation-log-item.reducer';
// prettier-ignore
import mute, {
  MuteState
} from 'app/entities/bot/mute/mute.reducer';
// prettier-ignore
import punishment, {
  PunishmentState
} from 'app/entities/bot/punishment/punishment.reducer';
// prettier-ignore
import scheduledAnnouncement, {
  ScheduledAnnouncementState
} from 'app/entities/bot/scheduled-announcement/scheduled-announcement.reducer';
// prettier-ignore
import serverLogItem, {
  ServerLogItemState
} from 'app/entities/bot/server-log-item/server-log-item.reducer';
// prettier-ignore
import tempBan, {
  TempBanState
} from 'app/entities/bot/temp-ban/temp-ban.reducer';
// prettier-ignore
import welcomeMessage, {
  WelcomeMessageState
} from 'app/entities/bot/welcome-message/welcome-message.reducer';
// prettier-ignore
import page, {
  PageState
} from 'app/entities/blog/page/page.reducer';
// prettier-ignore
import rootPage, {
  RootPageState
} from 'app/entities/blog/root-page/root-page.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly locale: LocaleState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly autoModAntiDup: AutoModAntiDupState;
  readonly autoModAutoRaid: AutoModAutoRaidState;
  readonly autoModIgnore: AutoModIgnoreState;
  readonly autoModMentions: AutoModMentionsState;
  readonly autoModeration: AutoModerationState;
  readonly discordUser: DiscordUserState;
  readonly discordUserProfile: DiscordUserProfileState;
  readonly giveAway: GiveAwayState;
  readonly guildApplication: GuildApplicationState;
  readonly guildApplicationForm: GuildApplicationFormState;
  readonly guildEvent: GuildEventState;
  readonly guildPoll: GuildPollState;
  readonly guildPollItem: GuildPollItemState;
  readonly guildServer: GuildServerState;
  readonly guildServerProfile: GuildServerProfileState;
  readonly guildServerSettings: GuildServerSettingsState;
  readonly moderationLogItem: ModerationLogItemState;
  readonly mute: MuteState;
  readonly punishment: PunishmentState;
  readonly scheduledAnnouncement: ScheduledAnnouncementState;
  readonly serverLogItem: ServerLogItemState;
  readonly tempBan: TempBanState;
  readonly welcomeMessage: WelcomeMessageState;
  readonly page: PageState;
  readonly rootPage: RootPageState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  autoModAntiDup,
  autoModAutoRaid,
  autoModIgnore,
  autoModMentions,
  autoModeration,
  discordUser,
  discordUserProfile,
  giveAway,
  guildApplication,
  guildApplicationForm,
  guildEvent,
  guildPoll,
  guildPollItem,
  guildServer,
  guildServerProfile,
  guildServerSettings,
  moderationLogItem,
  mute,
  punishment,
  scheduledAnnouncement,
  serverLogItem,
  tempBan,
  welcomeMessage,
  page,
  rootPage,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar
});

export default rootReducer;
