import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './scheduled-announcement.reducer';
import { IScheduledAnnouncement } from 'app/shared/model/bot/scheduled-announcement.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IScheduledAnnouncementDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ScheduledAnnouncementDetail extends React.Component<IScheduledAnnouncementDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { scheduledAnnouncementEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="webApp.botScheduledAnnouncement.detail.title">ScheduledAnnouncement</Translate> [
            <b>{scheduledAnnouncementEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="annoucementTitle">
                <Translate contentKey="webApp.botScheduledAnnouncement.annoucementTitle">Annoucement Title</Translate>
              </span>
            </dt>
            <dd>{scheduledAnnouncementEntity.annoucementTitle}</dd>
            <dt>
              <span id="annoucementImgUrl">
                <Translate contentKey="webApp.botScheduledAnnouncement.annoucementImgUrl">Annoucement Img Url</Translate>
              </span>
            </dt>
            <dd>{scheduledAnnouncementEntity.annoucementImgUrl}</dd>
            <dt>
              <span id="annoucementMessage">
                <Translate contentKey="webApp.botScheduledAnnouncement.annoucementMessage">Annoucement Message</Translate>
              </span>
            </dt>
            <dd>{scheduledAnnouncementEntity.annoucementMessage}</dd>
            <dt>
              <span id="annoucementFire">
                <Translate contentKey="webApp.botScheduledAnnouncement.annoucementFire">Annoucement Fire</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={scheduledAnnouncementEntity.annoucementFire} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="guildId">
                <Translate contentKey="webApp.botScheduledAnnouncement.guildId">Guild Id</Translate>
              </span>
            </dt>
            <dd>{scheduledAnnouncementEntity.guildId}</dd>
            <dt>
              <Translate contentKey="webApp.botScheduledAnnouncement.annouceGuild">Annouce Guild</Translate>
            </dt>
            <dd>{scheduledAnnouncementEntity.annouceGuild ? scheduledAnnouncementEntity.annouceGuild.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/scheduled-announcement" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/scheduled-announcement/${scheduledAnnouncementEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ scheduledAnnouncement }: IRootState) => ({
  scheduledAnnouncementEntity: scheduledAnnouncement.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ScheduledAnnouncementDetail);
