import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AutoModIgnore from './auto-mod-ignore';
import AutoModIgnoreDetail from './auto-mod-ignore-detail';
import AutoModIgnoreUpdate from './auto-mod-ignore-update';
import AutoModIgnoreDeleteDialog from './auto-mod-ignore-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AutoModIgnoreUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AutoModIgnoreUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AutoModIgnoreDetail} />
      <ErrorBoundaryRoute path={match.url} component={AutoModIgnore} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={AutoModIgnoreDeleteDialog} />
  </>
);

export default Routes;
