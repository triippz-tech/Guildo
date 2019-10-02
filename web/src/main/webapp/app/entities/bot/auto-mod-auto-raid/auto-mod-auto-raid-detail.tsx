import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './auto-mod-auto-raid.reducer';
import { IAutoModAutoRaid } from 'app/shared/model/bot/auto-mod-auto-raid.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAutoModAutoRaidDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class AutoModAutoRaidDetail extends React.Component<IAutoModAutoRaidDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { autoModAutoRaidEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="webApp.botAutoModAutoRaid.detail.title">AutoModAutoRaid</Translate> [<b>{autoModAutoRaidEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="autoRaidEnabled">
                <Translate contentKey="webApp.botAutoModAutoRaid.autoRaidEnabled">Auto Raid Enabled</Translate>
              </span>
            </dt>
            <dd>{autoModAutoRaidEntity.autoRaidEnabled ? 'true' : 'false'}</dd>
            <dt>
              <span id="autoRaidTimeThreshold">
                <Translate contentKey="webApp.botAutoModAutoRaid.autoRaidTimeThreshold">Auto Raid Time Threshold</Translate>
              </span>
            </dt>
            <dd>{autoModAutoRaidEntity.autoRaidTimeThreshold}</dd>
          </dl>
          <Button tag={Link} to="/entity/auto-mod-auto-raid" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/auto-mod-auto-raid/${autoModAutoRaidEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ autoModAutoRaid }: IRootState) => ({
  autoModAutoRaidEntity: autoModAutoRaid.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AutoModAutoRaidDetail);
