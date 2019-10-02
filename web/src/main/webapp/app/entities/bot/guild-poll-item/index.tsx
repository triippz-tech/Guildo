import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import GuildPollItem from './guild-poll-item';
import GuildPollItemDetail from './guild-poll-item-detail';
import GuildPollItemUpdate from './guild-poll-item-update';
import GuildPollItemDeleteDialog from './guild-poll-item-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GuildPollItemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GuildPollItemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GuildPollItemDetail} />
      <ErrorBoundaryRoute path={match.url} component={GuildPollItem} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={GuildPollItemDeleteDialog} />
  </>
);

export default Routes;
