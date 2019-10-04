import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import UserStrike from './user-strike';
import UserStrikeDetail from './user-strike-detail';
import UserStrikeUpdate from './user-strike-update';
import UserStrikeDeleteDialog from './user-strike-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={UserStrikeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={UserStrikeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={UserStrikeDetail} />
      <ErrorBoundaryRoute path={match.url} component={UserStrike} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={UserStrikeDeleteDialog} />
  </>
);

export default Routes;
