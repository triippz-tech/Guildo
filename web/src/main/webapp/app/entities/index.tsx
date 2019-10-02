import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AutoModAntiDup from './bot/auto-mod-anti-dup';
import AutoModAutoRaid from './bot/auto-mod-auto-raid';
import AutoModIgnore from './bot/auto-mod-ignore';
import AutoModMentions from './bot/auto-mod-mentions';
import AutoModeration from './bot/auto-moderation';
import DiscordUser from './bot/discord-user';
import DiscordUserProfile from './bot/discord-user-profile';
import GiveAway from './bot/give-away';
import GuildApplication from './bot/guild-application';
import GuildApplicationForm from './bot/guild-application-form';
import GuildEvent from './bot/guild-event';
import GuildPoll from './bot/guild-poll';
import GuildPollItem from './bot/guild-poll-item';
import GuildServer from './bot/guild-server';
import GuildServerProfile from './bot/guild-server-profile';
import GuildServerSettings from './bot/guild-server-settings';
import ModerationLogItem from './bot/moderation-log-item';
import Mute from './bot/mute';
import Punishment from './bot/punishment';
import ScheduledAnnouncement from './bot/scheduled-announcement';
import ServerLogItem from './bot/server-log-item';
import TempBan from './bot/temp-ban';
import WelcomeMessage from './bot/welcome-message';
import Page from './blog/page';
import RootPage from './blog/root-page';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/auto-mod-anti-dup`} component={AutoModAntiDup} />
      <ErrorBoundaryRoute path={`${match.url}/auto-mod-auto-raid`} component={AutoModAutoRaid} />
      <ErrorBoundaryRoute path={`${match.url}/auto-mod-ignore`} component={AutoModIgnore} />
      <ErrorBoundaryRoute path={`${match.url}/auto-mod-mentions`} component={AutoModMentions} />
      <ErrorBoundaryRoute path={`${match.url}/auto-moderation`} component={AutoModeration} />
      <ErrorBoundaryRoute path={`${match.url}/discord-user`} component={DiscordUser} />
      <ErrorBoundaryRoute path={`${match.url}/discord-user-profile`} component={DiscordUserProfile} />
      <ErrorBoundaryRoute path={`${match.url}/give-away`} component={GiveAway} />
      <ErrorBoundaryRoute path={`${match.url}/guild-application`} component={GuildApplication} />
      <ErrorBoundaryRoute path={`${match.url}/guild-application-form`} component={GuildApplicationForm} />
      <ErrorBoundaryRoute path={`${match.url}/guild-event`} component={GuildEvent} />
      <ErrorBoundaryRoute path={`${match.url}/guild-poll`} component={GuildPoll} />
      <ErrorBoundaryRoute path={`${match.url}/guild-poll-item`} component={GuildPollItem} />
      <ErrorBoundaryRoute path={`${match.url}/guild-server`} component={GuildServer} />
      <ErrorBoundaryRoute path={`${match.url}/guild-server-profile`} component={GuildServerProfile} />
      <ErrorBoundaryRoute path={`${match.url}/guild-server-settings`} component={GuildServerSettings} />
      <ErrorBoundaryRoute path={`${match.url}/moderation-log-item`} component={ModerationLogItem} />
      <ErrorBoundaryRoute path={`${match.url}/mute`} component={Mute} />
      <ErrorBoundaryRoute path={`${match.url}/punishment`} component={Punishment} />
      <ErrorBoundaryRoute path={`${match.url}/scheduled-announcement`} component={ScheduledAnnouncement} />
      <ErrorBoundaryRoute path={`${match.url}/server-log-item`} component={ServerLogItem} />
      <ErrorBoundaryRoute path={`${match.url}/temp-ban`} component={TempBan} />
      <ErrorBoundaryRoute path={`${match.url}/welcome-message`} component={WelcomeMessage} />
      <ErrorBoundaryRoute path={`${match.url}/page`} component={Page} />
      <ErrorBoundaryRoute path={`${match.url}/root-page`} component={RootPage} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
