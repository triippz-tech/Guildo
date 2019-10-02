import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './temp-ban.reducer';
import { ITempBan } from 'app/shared/model/bot/temp-ban.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITempBanDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class TempBanDetail extends React.Component<ITempBanDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { tempBanEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="webApp.botTempBan.detail.title">TempBan</Translate> [<b>{tempBanEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="reason">
                <Translate contentKey="webApp.botTempBan.reason">Reason</Translate>
              </span>
            </dt>
            <dd>{tempBanEntity.reason}</dd>
            <dt>
              <span id="endTime">
                <Translate contentKey="webApp.botTempBan.endTime">End Time</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={tempBanEntity.endTime} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="guildId">
                <Translate contentKey="webApp.botTempBan.guildId">Guild Id</Translate>
              </span>
            </dt>
            <dd>{tempBanEntity.guildId}</dd>
            <dt>
              <span id="userId">
                <Translate contentKey="webApp.botTempBan.userId">User Id</Translate>
              </span>
            </dt>
            <dd>{tempBanEntity.userId}</dd>
            <dt>
              <Translate contentKey="webApp.botTempBan.bannedUser">Banned User</Translate>
            </dt>
            <dd>{tempBanEntity.bannedUser ? tempBanEntity.bannedUser.id : ''}</dd>
            <dt>
              <Translate contentKey="webApp.botTempBan.tempBanGuildServer">Temp Ban Guild Server</Translate>
            </dt>
            <dd>{tempBanEntity.tempBanGuildServer ? tempBanEntity.tempBanGuildServer.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/temp-ban" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/temp-ban/${tempBanEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ tempBan }: IRootState) => ({
  tempBanEntity: tempBan.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TempBanDetail);
