import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ModerationLogItem from './moderation-log-item';
import ModerationLogItemDetail from './moderation-log-item-detail';
import ModerationLogItemUpdate from './moderation-log-item-update';
import ModerationLogItemDeleteDialog from './moderation-log-item-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ModerationLogItemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ModerationLogItemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ModerationLogItemDetail} />
      <ErrorBoundaryRoute path={match.url} component={ModerationLogItem} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ModerationLogItemDeleteDialog} />
  </>
);

export default Routes;
