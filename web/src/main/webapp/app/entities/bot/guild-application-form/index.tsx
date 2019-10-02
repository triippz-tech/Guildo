import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import GuildApplicationForm from './guild-application-form';
import GuildApplicationFormDetail from './guild-application-form-detail';
import GuildApplicationFormUpdate from './guild-application-form-update';
import GuildApplicationFormDeleteDialog from './guild-application-form-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GuildApplicationFormUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GuildApplicationFormUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GuildApplicationFormDetail} />
      <ErrorBoundaryRoute path={match.url} component={GuildApplicationForm} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={GuildApplicationFormDeleteDialog} />
  </>
);

export default Routes;
