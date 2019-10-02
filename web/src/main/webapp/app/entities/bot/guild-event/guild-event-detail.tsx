import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './guild-event.reducer';
import { IGuildEvent } from 'app/shared/model/bot/guild-event.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGuildEventDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class GuildEventDetail extends React.Component<IGuildEventDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { guildEventEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="webApp.botGuildEvent.detail.title">GuildEvent</Translate> [<b>{guildEventEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="eventName">
                <Translate contentKey="webApp.botGuildEvent.eventName">Event Name</Translate>
              </span>
            </dt>
            <dd>{guildEventEntity.eventName}</dd>
            <dt>
              <span id="eventImageUrl">
                <Translate contentKey="webApp.botGuildEvent.eventImageUrl">Event Image Url</Translate>
              </span>
            </dt>
            <dd>{guildEventEntity.eventImageUrl}</dd>
            <dt>
              <span id="eventMessage">
                <Translate contentKey="webApp.botGuildEvent.eventMessage">Event Message</Translate>
              </span>
            </dt>
            <dd>{guildEventEntity.eventMessage}</dd>
            <dt>
              <span id="eventStart">
                <Translate contentKey="webApp.botGuildEvent.eventStart">Event Start</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={guildEventEntity.eventStart} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="guildId">
                <Translate contentKey="webApp.botGuildEvent.guildId">Guild Id</Translate>
              </span>
            </dt>
            <dd>{guildEventEntity.guildId}</dd>
            <dt>
              <Translate contentKey="webApp.botGuildEvent.eventGuild">Event Guild</Translate>
            </dt>
            <dd>{guildEventEntity.eventGuild ? guildEventEntity.eventGuild.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/guild-event" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/guild-event/${guildEventEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ guildEvent }: IRootState) => ({
  guildEventEntity: guildEvent.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(GuildEventDetail);
