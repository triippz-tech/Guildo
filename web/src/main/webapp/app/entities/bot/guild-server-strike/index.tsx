import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import GuildServerStrike from './guild-server-strike';
import GuildServerStrikeDetail from './guild-server-strike-detail';
import GuildServerStrikeUpdate from './guild-server-strike-update';
import GuildServerStrikeDeleteDialog from './guild-server-strike-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GuildServerStrikeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GuildServerStrikeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GuildServerStrikeDetail} />
      <ErrorBoundaryRoute path={match.url} component={GuildServerStrike} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={GuildServerStrikeDeleteDialog} />
  </>
);

export default Routes;
