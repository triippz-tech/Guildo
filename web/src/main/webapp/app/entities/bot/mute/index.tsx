import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Mute from './mute';
import MuteDetail from './mute-detail';
import MuteUpdate from './mute-update';
import MuteDeleteDialog from './mute-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={MuteUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={MuteUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MuteDetail} />
      <ErrorBoundaryRoute path={match.url} component={Mute} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={MuteDeleteDialog} />
  </>
);

export default Routes;
