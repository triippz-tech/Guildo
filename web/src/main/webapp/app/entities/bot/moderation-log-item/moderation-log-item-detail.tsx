import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './moderation-log-item.reducer';
import { IModerationLogItem } from 'app/shared/model/bot/moderation-log-item.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IModerationLogItemDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ModerationLogItemDetail extends React.Component<IModerationLogItemDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { moderationLogItemEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="webApp.botModerationLogItem.detail.title">ModerationLogItem</Translate> [
            <b>{moderationLogItemEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="channelId">
                <Translate contentKey="webApp.botModerationLogItem.channelId">Channel Id</Translate>
              </span>
            </dt>
            <dd>{moderationLogItemEntity.channelId}</dd>
            <dt>
              <span id="channelName">
                <Translate contentKey="webApp.botModerationLogItem.channelName">Channel Name</Translate>
              </span>
            </dt>
            <dd>{moderationLogItemEntity.channelName}</dd>
            <dt>
              <span id="issuedById">
                <Translate contentKey="webApp.botModerationLogItem.issuedById">Issued By Id</Translate>
              </span>
            </dt>
            <dd>{moderationLogItemEntity.issuedById}</dd>
            <dt>
              <span id="issuedByName">
                <Translate contentKey="webApp.botModerationLogItem.issuedByName">Issued By Name</Translate>
              </span>
            </dt>
            <dd>{moderationLogItemEntity.issuedByName}</dd>
            <dt>
              <span id="issuedToId">
                <Translate contentKey="webApp.botModerationLogItem.issuedToId">Issued To Id</Translate>
              </span>
            </dt>
            <dd>{moderationLogItemEntity.issuedToId}</dd>
            <dt>
              <span id="issuedToName">
                <Translate contentKey="webApp.botModerationLogItem.issuedToName">Issued To Name</Translate>
              </span>
            </dt>
            <dd>{moderationLogItemEntity.issuedToName}</dd>
            <dt>
              <span id="reason">
                <Translate contentKey="webApp.botModerationLogItem.reason">Reason</Translate>
              </span>
            </dt>
            <dd>{moderationLogItemEntity.reason}</dd>
            <dt>
              <span id="time">
                <Translate contentKey="webApp.botModerationLogItem.time">Time</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={moderationLogItemEntity.time} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="moderationAction">
                <Translate contentKey="webApp.botModerationLogItem.moderationAction">Moderation Action</Translate>
              </span>
            </dt>
            <dd>{moderationLogItemEntity.moderationAction}</dd>
            <dt>
              <span id="guildId">
                <Translate contentKey="webApp.botModerationLogItem.guildId">Guild Id</Translate>
              </span>
            </dt>
            <dd>{moderationLogItemEntity.guildId}</dd>
            <dt>
              <Translate contentKey="webApp.botModerationLogItem.modItemGuildServer">Mod Item Guild Server</Translate>
            </dt>
            <dd>{moderationLogItemEntity.modItemGuildServer ? moderationLogItemEntity.modItemGuildServer.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/moderation-log-item" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/moderation-log-item/${moderationLogItemEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ moderationLogItem }: IRootState) => ({
  moderationLogItemEntity: moderationLogItem.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ModerationLogItemDetail);
