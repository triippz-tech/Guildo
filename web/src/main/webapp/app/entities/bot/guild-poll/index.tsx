import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import GuildPoll from './guild-poll';
import GuildPollDetail from './guild-poll-detail';
import GuildPollUpdate from './guild-poll-update';
import GuildPollDeleteDialog from './guild-poll-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GuildPollUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GuildPollUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GuildPollDetail} />
      <ErrorBoundaryRoute path={match.url} component={GuildPoll} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={GuildPollDeleteDialog} />
  </>
);

export default Routes;
