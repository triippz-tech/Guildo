import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './discord-user.reducer';
import { IDiscordUser } from 'app/shared/model/bot/discord-user.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IDiscordUserDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class DiscordUserDetail extends React.Component<IDiscordUserDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { discordUserEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="webApp.botDiscordUser.detail.title">DiscordUser</Translate> [<b>{discordUserEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="userId">
                <Translate contentKey="webApp.botDiscordUser.userId">User Id</Translate>
              </span>
            </dt>
            <dd>{discordUserEntity.userId}</dd>
            <dt>
              <span id="userName">
                <Translate contentKey="webApp.botDiscordUser.userName">User Name</Translate>
              </span>
            </dt>
            <dd>{discordUserEntity.userName}</dd>
            <dt>
              <span id="icon">
                <Translate contentKey="webApp.botDiscordUser.icon">Icon</Translate>
              </span>
            </dt>
            <dd>{discordUserEntity.icon}</dd>
            <dt>
              <span id="commandsIssued">
                <Translate contentKey="webApp.botDiscordUser.commandsIssued">Commands Issued</Translate>
              </span>
            </dt>
            <dd>{discordUserEntity.commandsIssued}</dd>
            <dt>
              <span id="blacklisted">
                <Translate contentKey="webApp.botDiscordUser.blacklisted">Blacklisted</Translate>
              </span>
            </dt>
            <dd>{discordUserEntity.blacklisted ? 'true' : 'false'}</dd>
            <dt>
              <span id="userLevel">
                <Translate contentKey="webApp.botDiscordUser.userLevel">User Level</Translate>
              </span>
            </dt>
            <dd>{discordUserEntity.userLevel}</dd>
            <dt>
              <Translate contentKey="webApp.botDiscordUser.userProfile">User Profile</Translate>
            </dt>
            <dd>{discordUserEntity.userProfile ? discordUserEntity.userProfile.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/discord-user" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/discord-user/${discordUserEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ discordUser }: IRootState) => ({
  discordUserEntity: discordUser.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DiscordUserDetail);
