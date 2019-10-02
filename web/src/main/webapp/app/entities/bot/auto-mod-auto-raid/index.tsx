import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AutoModAutoRaid from './auto-mod-auto-raid';
import AutoModAutoRaidDetail from './auto-mod-auto-raid-detail';
import AutoModAutoRaidUpdate from './auto-mod-auto-raid-update';
import AutoModAutoRaidDeleteDialog from './auto-mod-auto-raid-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AutoModAutoRaidUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AutoModAutoRaidUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AutoModAutoRaidDetail} />
      <ErrorBoundaryRoute path={match.url} component={AutoModAutoRaid} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={AutoModAutoRaidDeleteDialog} />
  </>
);

export default Routes;
