import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './guild-server.reducer';
import { IGuildServer } from 'app/shared/model/bot/guild-server.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGuildServerDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class GuildServerDetail extends React.Component<IGuildServerDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { guildServerEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="webApp.botGuildServer.detail.title">GuildServer</Translate> [<b>{guildServerEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="guildId">
                <Translate contentKey="webApp.botGuildServer.guildId">Guild Id</Translate>
              </span>
            </dt>
            <dd>{guildServerEntity.guildId}</dd>
            <dt>
              <span id="guildName">
                <Translate contentKey="webApp.botGuildServer.guildName">Guild Name</Translate>
              </span>
            </dt>
            <dd>{guildServerEntity.guildName}</dd>
            <dt>
              <span id="icon">
                <Translate contentKey="webApp.botGuildServer.icon">Icon</Translate>
              </span>
            </dt>
            <dd>{guildServerEntity.icon}</dd>
            <dt>
              <span id="owner">
                <Translate contentKey="webApp.botGuildServer.owner">Owner</Translate>
              </span>
            </dt>
            <dd>{guildServerEntity.owner}</dd>
            <dt>
              <span id="serverLevel">
                <Translate contentKey="webApp.botGuildServer.serverLevel">Server Level</Translate>
              </span>
            </dt>
            <dd>{guildServerEntity.serverLevel}</dd>
            <dt>
              <Translate contentKey="webApp.botGuildServer.guildProfile">Guild Profile</Translate>
            </dt>
            <dd>{guildServerEntity.guildProfile ? guildServerEntity.guildProfile.id : ''}</dd>
            <dt>
              <Translate contentKey="webApp.botGuildServer.applicationForm">Application Form</Translate>
            </dt>
            <dd>{guildServerEntity.applicationForm ? guildServerEntity.applicationForm.id : ''}</dd>
            <dt>
              <Translate contentKey="webApp.botGuildServer.guildSettings">Guild Settings</Translate>
            </dt>
            <dd>{guildServerEntity.guildSettings ? guildServerEntity.guildSettings.id : ''}</dd>
            <dt>
              <Translate contentKey="webApp.botGuildServer.welcomeMessage">Welcome Message</Translate>
            </dt>
            <dd>{guildServerEntity.welcomeMessage ? guildServerEntity.welcomeMessage.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/guild-server" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/guild-server/${guildServerEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ guildServer }: IRootState) => ({
  guildServerEntity: guildServer.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(GuildServerDetail);
