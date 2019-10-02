import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import GuildServer from './guild-server';
import GuildServerDetail from './guild-server-detail';
import GuildServerUpdate from './guild-server-update';
import GuildServerDeleteDialog from './guild-server-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GuildServerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GuildServerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GuildServerDetail} />
      <ErrorBoundaryRoute path={match.url} component={GuildServer} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={GuildServerDeleteDialog} />
  </>
);

export default Routes;
