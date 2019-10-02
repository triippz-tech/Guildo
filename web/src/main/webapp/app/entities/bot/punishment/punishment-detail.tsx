import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './punishment.reducer';
import { IPunishment } from 'app/shared/model/bot/punishment.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPunishmentDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class PunishmentDetail extends React.Component<IPunishmentDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { punishmentEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="webApp.botPunishment.detail.title">Punishment</Translate> [<b>{punishmentEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="maxStrikes">
                <Translate contentKey="webApp.botPunishment.maxStrikes">Max Strikes</Translate>
              </span>
            </dt>
            <dd>{punishmentEntity.maxStrikes}</dd>
            <dt>
              <span id="action">
                <Translate contentKey="webApp.botPunishment.action">Action</Translate>
              </span>
            </dt>
            <dd>{punishmentEntity.action}</dd>
            <dt>
              <span id="punishmentDuration">
                <Translate contentKey="webApp.botPunishment.punishmentDuration">Punishment Duration</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={punishmentEntity.punishmentDuration} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="guildId">
                <Translate contentKey="webApp.botPunishment.guildId">Guild Id</Translate>
              </span>
            </dt>
            <dd>{punishmentEntity.guildId}</dd>
          </dl>
          <Button tag={Link} to="/entity/punishment" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/punishment/${punishmentEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ punishment }: IRootState) => ({
  punishmentEntity: punishment.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PunishmentDetail);
