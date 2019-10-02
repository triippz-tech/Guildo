import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import GuildServerSettings from './guild-server-settings';
import GuildServerSettingsDetail from './guild-server-settings-detail';
import GuildServerSettingsUpdate from './guild-server-settings-update';
import GuildServerSettingsDeleteDialog from './guild-server-settings-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GuildServerSettingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GuildServerSettingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GuildServerSettingsDetail} />
      <ErrorBoundaryRoute path={match.url} component={GuildServerSettings} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={GuildServerSettingsDeleteDialog} />
  </>
);

export default Routes;
