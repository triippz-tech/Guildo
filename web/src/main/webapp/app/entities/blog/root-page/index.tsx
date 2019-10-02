import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RootPage from './root-page';
import RootPageDetail from './root-page-detail';
import RootPageUpdate from './root-page-update';
import RootPageDeleteDialog from './root-page-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RootPageUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RootPageUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RootPageDetail} />
      <ErrorBoundaryRoute path={match.url} component={RootPage} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={RootPageDeleteDialog} />
  </>
);

export default Routes;
