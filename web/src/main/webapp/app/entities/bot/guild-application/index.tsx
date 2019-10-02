import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import GuildApplication from './guild-application';
import GuildApplicationDetail from './guild-application-detail';
import GuildApplicationUpdate from './guild-application-update';
import GuildApplicationDeleteDialog from './guild-application-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GuildApplicationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GuildApplicationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GuildApplicationDetail} />
      <ErrorBoundaryRoute path={match.url} component={GuildApplication} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={GuildApplicationDeleteDialog} />
  </>
);

export default Routes;
