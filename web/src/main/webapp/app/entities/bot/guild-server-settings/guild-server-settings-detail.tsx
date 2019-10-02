import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './guild-server-settings.reducer';
import { IGuildServerSettings } from 'app/shared/model/bot/guild-server-settings.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGuildServerSettingsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class GuildServerSettingsDetail extends React.Component<IGuildServerSettingsDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { guildServerSettingsEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="webApp.botGuildServerSettings.detail.title">GuildServerSettings</Translate> [
            <b>{guildServerSettingsEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="prefix">
                <Translate contentKey="webApp.botGuildServerSettings.prefix">Prefix</Translate>
              </span>
            </dt>
            <dd>{guildServerSettingsEntity.prefix}</dd>
            <dt>
              <span id="timezone">
                <Translate contentKey="webApp.botGuildServerSettings.timezone">Timezone</Translate>
              </span>
            </dt>
            <dd>{guildServerSettingsEntity.timezone}</dd>
            <dt>
              <span id="raidModeEnabled">
                <Translate contentKey="webApp.botGuildServerSettings.raidModeEnabled">Raid Mode Enabled</Translate>
              </span>
            </dt>
            <dd>{guildServerSettingsEntity.raidModeEnabled ? 'true' : 'false'}</dd>
            <dt>
              <span id="raidModeReason">
                <Translate contentKey="webApp.botGuildServerSettings.raidModeReason">Raid Mode Reason</Translate>
              </span>
            </dt>
            <dd>{guildServerSettingsEntity.raidModeReason}</dd>
            <dt>
              <span id="maxStrikes">
                <Translate contentKey="webApp.botGuildServerSettings.maxStrikes">Max Strikes</Translate>
              </span>
            </dt>
            <dd>{guildServerSettingsEntity.maxStrikes}</dd>
            <dt>
              <span id="acceptingApplications">
                <Translate contentKey="webApp.botGuildServerSettings.acceptingApplications">Accepting Applications</Translate>
              </span>
            </dt>
            <dd>{guildServerSettingsEntity.acceptingApplications ? 'true' : 'false'}</dd>
            <dt>
              <Translate contentKey="webApp.botGuildServerSettings.autoModConfig">Auto Mod Config</Translate>
            </dt>
            <dd>{guildServerSettingsEntity.autoModConfig ? guildServerSettingsEntity.autoModConfig.id : ''}</dd>
            <dt>
              <Translate contentKey="webApp.botGuildServerSettings.punishmentConfig">Punishment Config</Translate>
            </dt>
            <dd>{guildServerSettingsEntity.punishmentConfig ? guildServerSettingsEntity.punishmentConfig.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/guild-server-settings" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/guild-server-settings/${guildServerSettingsEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ guildServerSettings }: IRootState) => ({
  guildServerSettingsEntity: guildServerSettings.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(GuildServerSettingsDetail);
