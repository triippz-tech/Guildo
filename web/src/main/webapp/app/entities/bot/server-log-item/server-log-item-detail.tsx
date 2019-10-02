import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './server-log-item.reducer';
import { IServerLogItem } from 'app/shared/model/bot/server-log-item.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IServerLogItemDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ServerLogItemDetail extends React.Component<IServerLogItemDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { serverLogItemEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="webApp.botServerLogItem.detail.title">ServerLogItem</Translate> [<b>{serverLogItemEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="activity">
                <Translate contentKey="webApp.botServerLogItem.activity">Activity</Translate>
              </span>
            </dt>
            <dd>{serverLogItemEntity.activity}</dd>
            <dt>
              <span id="channelId">
                <Translate contentKey="webApp.botServerLogItem.channelId">Channel Id</Translate>
              </span>
            </dt>
            <dd>{serverLogItemEntity.channelId}</dd>
            <dt>
              <span id="channelName">
                <Translate contentKey="webApp.botServerLogItem.channelName">Channel Name</Translate>
              </span>
            </dt>
            <dd>{serverLogItemEntity.channelName}</dd>
            <dt>
              <span id="time">
                <Translate contentKey="webApp.botServerLogItem.time">Time</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={serverLogItemEntity.time} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="userId">
                <Translate contentKey="webApp.botServerLogItem.userId">User Id</Translate>
              </span>
            </dt>
            <dd>{serverLogItemEntity.userId}</dd>
            <dt>
              <span id="userName">
                <Translate contentKey="webApp.botServerLogItem.userName">User Name</Translate>
              </span>
            </dt>
            <dd>{serverLogItemEntity.userName}</dd>
            <dt>
              <span id="guildId">
                <Translate contentKey="webApp.botServerLogItem.guildId">Guild Id</Translate>
              </span>
            </dt>
            <dd>{serverLogItemEntity.guildId}</dd>
            <dt>
              <Translate contentKey="webApp.botServerLogItem.serverItemGuildServer">Server Item Guild Server</Translate>
            </dt>
            <dd>{serverLogItemEntity.serverItemGuildServer ? serverLogItemEntity.serverItemGuildServer.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/server-log-item" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/server-log-item/${serverLogItemEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ serverLogItem }: IRootState) => ({
  serverLogItemEntity: serverLogItem.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ServerLogItemDetail);
