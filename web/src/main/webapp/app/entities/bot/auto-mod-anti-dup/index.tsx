import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AutoModAntiDup from './auto-mod-anti-dup';
import AutoModAntiDupDetail from './auto-mod-anti-dup-detail';
import AutoModAntiDupUpdate from './auto-mod-anti-dup-update';
import AutoModAntiDupDeleteDialog from './auto-mod-anti-dup-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AutoModAntiDupUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AutoModAntiDupUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AutoModAntiDupDetail} />
      <ErrorBoundaryRoute path={match.url} component={AutoModAntiDup} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={AutoModAntiDupDeleteDialog} />
  </>
);

export default Routes;
