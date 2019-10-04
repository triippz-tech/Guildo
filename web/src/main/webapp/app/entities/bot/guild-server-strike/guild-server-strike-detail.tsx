import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './guild-server-strike.reducer';
import { IGuildServerStrike } from 'app/shared/model/bot/guild-server-strike.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGuildServerStrikeDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class GuildServerStrikeDetail extends React.Component<IGuildServerStrikeDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { guildServerStrikeEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="webApp.botGuildServerStrike.detail.title">GuildServerStrike</Translate> [
            <b>{guildServerStrikeEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="count">
                <Translate contentKey="webApp.botGuildServerStrike.count">Count</Translate>
              </span>
            </dt>
            <dd>{guildServerStrikeEntity.count}</dd>
            <dt>
              <span id="userId">
                <Translate contentKey="webApp.botGuildServerStrike.userId">User Id</Translate>
              </span>
            </dt>
            <dd>{guildServerStrikeEntity.userId}</dd>
            <dt>
              <span id="guildId">
                <Translate contentKey="webApp.botGuildServerStrike.guildId">Guild Id</Translate>
              </span>
            </dt>
            <dd>{guildServerStrikeEntity.guildId}</dd>
            <dt>
              <Translate contentKey="webApp.botGuildServerStrike.discordUser">Discord User</Translate>
            </dt>
            <dd>{guildServerStrikeEntity.discordUser ? guildServerStrikeEntity.discordUser.userName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/guild-server-strike" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/guild-server-strike/${guildServerStrikeEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ guildServerStrike }: IRootState) => ({
  guildServerStrikeEntity: guildServerStrike.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(GuildServerStrikeDetail);
