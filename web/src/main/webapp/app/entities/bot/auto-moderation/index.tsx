import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AutoModeration from './auto-moderation';
import AutoModerationDetail from './auto-moderation-detail';
import AutoModerationUpdate from './auto-moderation-update';
import AutoModerationDeleteDialog from './auto-moderation-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AutoModerationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AutoModerationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AutoModerationDetail} />
      <ErrorBoundaryRoute path={match.url} component={AutoModeration} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={AutoModerationDeleteDialog} />
  </>
);

export default Routes;
