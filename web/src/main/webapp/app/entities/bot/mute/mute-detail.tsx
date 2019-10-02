import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './mute.reducer';
import { IMute } from 'app/shared/model/bot/mute.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMuteDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class MuteDetail extends React.Component<IMuteDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { muteEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="webApp.botMute.detail.title">Mute</Translate> [<b>{muteEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="reason">
                <Translate contentKey="webApp.botMute.reason">Reason</Translate>
              </span>
            </dt>
            <dd>{muteEntity.reason}</dd>
            <dt>
              <span id="endTime">
                <Translate contentKey="webApp.botMute.endTime">End Time</Translate>
              </span>
            </dt>
            <dd>{muteEntity.endTime}</dd>
            <dt>
              <span id="guildId">
                <Translate contentKey="webApp.botMute.guildId">Guild Id</Translate>
              </span>
            </dt>
            <dd>{muteEntity.guildId}</dd>
            <dt>
              <span id="userId">
                <Translate contentKey="webApp.botMute.userId">User Id</Translate>
              </span>
            </dt>
            <dd>{muteEntity.userId}</dd>
            <dt>
              <Translate contentKey="webApp.botMute.mutedUser">Muted User</Translate>
            </dt>
            <dd>{muteEntity.mutedUser ? muteEntity.mutedUser.id : ''}</dd>
            <dt>
              <Translate contentKey="webApp.botMute.mutedGuildServer">Muted Guild Server</Translate>
            </dt>
            <dd>{muteEntity.mutedGuildServer ? muteEntity.mutedGuildServer.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/mute" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/mute/${muteEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ mute }: IRootState) => ({
  muteEntity: mute.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(MuteDetail);
