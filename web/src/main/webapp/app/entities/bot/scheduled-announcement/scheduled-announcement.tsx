import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { byteSize, Translate, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './scheduled-announcement.reducer';
import { IScheduledAnnouncement } from 'app/shared/model/bot/scheduled-announcement.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IScheduledAnnouncementProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class ScheduledAnnouncement extends React.Component<IScheduledAnnouncementProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { scheduledAnnouncementList, match } = this.props;
    return (
      <div>
        <h2 id="scheduled-announcement-heading">
          <Translate contentKey="webApp.botScheduledAnnouncement.home.title">Scheduled Announcements</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="webApp.botScheduledAnnouncement.home.createLabel">Create a new Scheduled Announcement</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {scheduledAnnouncementList && scheduledAnnouncementList.length > 0 ? (
            <Table responsive aria-describedby="scheduled-announcement-heading">
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botScheduledAnnouncement.annoucementTitle">Annoucement Title</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botScheduledAnnouncement.annoucementImgUrl">Annoucement Img Url</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botScheduledAnnouncement.annoucementMessage">Annoucement Message</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botScheduledAnnouncement.annoucementFire">Annoucement Fire</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botScheduledAnnouncement.guildId">Guild Id</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botScheduledAnnouncement.annouceGuild">Annouce Guild</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {scheduledAnnouncementList.map((scheduledAnnouncement, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${scheduledAnnouncement.id}`} color="link" size="sm">
                        {scheduledAnnouncement.id}
                      </Button>
                    </td>
                    <td>{scheduledAnnouncement.annoucementTitle}</td>
                    <td>{scheduledAnnouncement.annoucementImgUrl}</td>
                    <td>{scheduledAnnouncement.annoucementMessage}</td>
                    <td>
                      <TextFormat type="date" value={scheduledAnnouncement.annoucementFire} format={APP_DATE_FORMAT} />
                    </td>
                    <td>{scheduledAnnouncement.guildId}</td>
                    <td>
                      {scheduledAnnouncement.annouceGuild ? (
                        <Link to={`guild-server/${scheduledAnnouncement.annouceGuild.id}`}>{scheduledAnnouncement.annouceGuild.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${scheduledAnnouncement.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${scheduledAnnouncement.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${scheduledAnnouncement.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.delete">Delete</Translate>
                          </span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            <div className="alert alert-warning">
              <Translate contentKey="webApp.botScheduledAnnouncement.home.notFound">No Scheduled Announcements found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ scheduledAnnouncement }: IRootState) => ({
  scheduledAnnouncementList: scheduledAnnouncement.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ScheduledAnnouncement);
