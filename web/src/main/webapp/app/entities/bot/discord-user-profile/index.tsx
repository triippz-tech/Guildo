import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import DiscordUserProfile from './discord-user-profile';
import DiscordUserProfileDetail from './discord-user-profile-detail';
import DiscordUserProfileUpdate from './discord-user-profile-update';
import DiscordUserProfileDeleteDialog from './discord-user-profile-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DiscordUserProfileUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DiscordUserProfileUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DiscordUserProfileDetail} />
      <ErrorBoundaryRoute path={match.url} component={DiscordUserProfile} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={DiscordUserProfileDeleteDialog} />
  </>
);

export default Routes;
