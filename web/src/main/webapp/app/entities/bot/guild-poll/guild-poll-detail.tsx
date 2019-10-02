import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './guild-poll.reducer';
import { IGuildPoll } from 'app/shared/model/bot/guild-poll.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGuildPollDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class GuildPollDetail extends React.Component<IGuildPollDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { guildPollEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="webApp.botGuildPoll.detail.title">GuildPoll</Translate> [<b>{guildPollEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="pollName">
                <Translate contentKey="webApp.botGuildPoll.pollName">Poll Name</Translate>
              </span>
            </dt>
            <dd>{guildPollEntity.pollName}</dd>
            <dt>
              <span id="description">
                <Translate contentKey="webApp.botGuildPoll.description">Description</Translate>
              </span>
            </dt>
            <dd>{guildPollEntity.description}</dd>
            <dt>
              <span id="textChannelId">
                <Translate contentKey="webApp.botGuildPoll.textChannelId">Text Channel Id</Translate>
              </span>
            </dt>
            <dd>{guildPollEntity.textChannelId}</dd>
            <dt>
              <span id="finishTime">
                <Translate contentKey="webApp.botGuildPoll.finishTime">Finish Time</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={guildPollEntity.finishTime} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="completed">
                <Translate contentKey="webApp.botGuildPoll.completed">Completed</Translate>
              </span>
            </dt>
            <dd>{guildPollEntity.completed ? 'true' : 'false'}</dd>
            <dt>
              <span id="guildId">
                <Translate contentKey="webApp.botGuildPoll.guildId">Guild Id</Translate>
              </span>
            </dt>
            <dd>{guildPollEntity.guildId}</dd>
            <dt>
              <Translate contentKey="webApp.botGuildPoll.pollItems">Poll Items</Translate>
            </dt>
            <dd>{guildPollEntity.pollItems ? guildPollEntity.pollItems.id : ''}</dd>
            <dt>
              <Translate contentKey="webApp.botGuildPoll.pollServer">Poll Server</Translate>
            </dt>
            <dd>{guildPollEntity.pollServer ? guildPollEntity.pollServer.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/guild-poll" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/guild-poll/${guildPollEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ guildPoll }: IRootState) => ({
  guildPollEntity: guildPoll.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(GuildPollDetail);
