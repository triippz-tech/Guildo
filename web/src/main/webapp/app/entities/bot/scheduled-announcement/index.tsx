import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ScheduledAnnouncement from './scheduled-announcement';
import ScheduledAnnouncementDetail from './scheduled-announcement-detail';
import ScheduledAnnouncementUpdate from './scheduled-announcement-update';
import ScheduledAnnouncementDeleteDialog from './scheduled-announcement-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ScheduledAnnouncementUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ScheduledAnnouncementUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ScheduledAnnouncementDetail} />
      <ErrorBoundaryRoute path={match.url} component={ScheduledAnnouncement} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ScheduledAnnouncementDeleteDialog} />
  </>
);

export default Routes;
