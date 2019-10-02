import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './auto-mod-anti-dup.reducer';
import { IAutoModAntiDup } from 'app/shared/model/bot/auto-mod-anti-dup.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAutoModAntiDupDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class AutoModAntiDupDetail extends React.Component<IAutoModAntiDupDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { autoModAntiDupEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="webApp.botAutoModAntiDup.detail.title">AutoModAntiDup</Translate> [<b>{autoModAntiDupEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="deleteThreshold">
                <Translate contentKey="webApp.botAutoModAntiDup.deleteThreshold">Delete Threshold</Translate>
              </span>
            </dt>
            <dd>{autoModAntiDupEntity.deleteThreshold}</dd>
            <dt>
              <span id="dupsToPunish">
                <Translate contentKey="webApp.botAutoModAntiDup.dupsToPunish">Dups To Punish</Translate>
              </span>
            </dt>
            <dd>{autoModAntiDupEntity.dupsToPunish}</dd>
          </dl>
          <Button tag={Link} to="/entity/auto-mod-anti-dup" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/auto-mod-anti-dup/${autoModAntiDupEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ autoModAntiDup }: IRootState) => ({
  autoModAntiDupEntity: autoModAntiDup.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AutoModAntiDupDetail);
