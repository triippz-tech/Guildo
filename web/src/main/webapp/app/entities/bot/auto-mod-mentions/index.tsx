import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AutoModMentions from './auto-mod-mentions';
import AutoModMentionsDetail from './auto-mod-mentions-detail';
import AutoModMentionsUpdate from './auto-mod-mentions-update';
import AutoModMentionsDeleteDialog from './auto-mod-mentions-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AutoModMentionsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AutoModMentionsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AutoModMentionsDetail} />
      <ErrorBoundaryRoute path={match.url} component={AutoModMentions} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={AutoModMentionsDeleteDialog} />
  </>
);

export default Routes;
