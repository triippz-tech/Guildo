import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TempBan from './temp-ban';
import TempBanDetail from './temp-ban-detail';
import TempBanUpdate from './temp-ban-update';
import TempBanDeleteDialog from './temp-ban-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TempBanUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TempBanUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TempBanDetail} />
      <ErrorBoundaryRoute path={match.url} component={TempBan} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={TempBanDeleteDialog} />
  </>
);

export default Routes;
