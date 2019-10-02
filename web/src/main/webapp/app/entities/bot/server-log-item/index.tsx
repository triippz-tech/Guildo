import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ServerLogItem from './server-log-item';
import ServerLogItemDetail from './server-log-item-detail';
import ServerLogItemUpdate from './server-log-item-update';
import ServerLogItemDeleteDialog from './server-log-item-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ServerLogItemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ServerLogItemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ServerLogItemDetail} />
      <ErrorBoundaryRoute path={match.url} component={ServerLogItem} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ServerLogItemDeleteDialog} />
  </>
);

export default Routes;
