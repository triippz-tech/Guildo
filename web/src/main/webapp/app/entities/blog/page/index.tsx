import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Page from './page';
import PageDetail from './page-detail';
import PageUpdate from './page-update';
import PageDeleteDialog from './page-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PageUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PageUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PageDetail} />
      <ErrorBoundaryRoute path={match.url} component={Page} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={PageDeleteDialog} />
  </>
);

export default Routes;
