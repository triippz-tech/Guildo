import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Punishment from './punishment';
import PunishmentDetail from './punishment-detail';
import PunishmentUpdate from './punishment-update';
import PunishmentDeleteDialog from './punishment-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PunishmentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PunishmentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PunishmentDetail} />
      <ErrorBoundaryRoute path={match.url} component={Punishment} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={PunishmentDeleteDialog} />
  </>
);

export default Routes;
