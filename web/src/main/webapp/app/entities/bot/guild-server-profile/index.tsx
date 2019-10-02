import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import GuildServerProfile from './guild-server-profile';
import GuildServerProfileDetail from './guild-server-profile-detail';
import GuildServerProfileUpdate from './guild-server-profile-update';
import GuildServerProfileDeleteDialog from './guild-server-profile-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GuildServerProfileUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GuildServerProfileUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GuildServerProfileDetail} />
      <ErrorBoundaryRoute path={match.url} component={GuildServerProfile} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={GuildServerProfileDeleteDialog} />
  </>
);

export default Routes;
