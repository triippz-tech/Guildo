import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './auto-mod-ignore.reducer';
import { IAutoModIgnore } from 'app/shared/model/bot/auto-mod-ignore.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAutoModIgnoreDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class AutoModIgnoreDetail extends React.Component<IAutoModIgnoreDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { autoModIgnoreEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="webApp.botAutoModIgnore.detail.title">AutoModIgnore</Translate> [<b>{autoModIgnoreEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="roleId">
                <Translate contentKey="webApp.botAutoModIgnore.roleId">Role Id</Translate>
              </span>
            </dt>
            <dd>{autoModIgnoreEntity.roleId}</dd>
            <dt>
              <span id="channelId">
                <Translate contentKey="webApp.botAutoModIgnore.channelId">Channel Id</Translate>
              </span>
            </dt>
            <dd>{autoModIgnoreEntity.channelId}</dd>
          </dl>
          <Button tag={Link} to="/entity/auto-mod-ignore" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/auto-mod-ignore/${autoModIgnoreEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ autoModIgnore }: IRootState) => ({
  autoModIgnoreEntity: autoModIgnore.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AutoModIgnoreDetail);
