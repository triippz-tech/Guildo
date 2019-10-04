import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './user-strike.reducer';
import { IUserStrike } from 'app/shared/model/bot/user-strike.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IUserStrikeDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class UserStrikeDetail extends React.Component<IUserStrikeDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { userStrikeEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="webApp.botUserStrike.detail.title">UserStrike</Translate> [<b>{userStrikeEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="count">
                <Translate contentKey="webApp.botUserStrike.count">Count</Translate>
              </span>
            </dt>
            <dd>{userStrikeEntity.count}</dd>
            <dt>
              <span id="userId">
                <Translate contentKey="webApp.botUserStrike.userId">User Id</Translate>
              </span>
            </dt>
            <dd>{userStrikeEntity.userId}</dd>
            <dt>
              <span id="guildId">
                <Translate contentKey="webApp.botUserStrike.guildId">Guild Id</Translate>
              </span>
            </dt>
            <dd>{userStrikeEntity.guildId}</dd>
            <dt>
              <Translate contentKey="webApp.botUserStrike.discordUser">Discord User</Translate>
            </dt>
            <dd>{userStrikeEntity.discordUser ? userStrikeEntity.discordUser.userName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/user-strike" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/user-strike/${userStrikeEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ userStrike }: IRootState) => ({
  userStrikeEntity: userStrike.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(UserStrikeDetail);
