import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './guild-server-settings.reducer';
import { IGuildServerSettings } from 'app/shared/model/bot/guild-server-settings.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGuildServerSettingsProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class GuildServerSettings extends React.Component<IGuildServerSettingsProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { guildServerSettingsList, match } = this.props;
    return (
      <div>
        <h2 id="guild-server-settings-heading">
          <Translate contentKey="webApp.botGuildServerSettings.home.title">Guild Server Settings</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="webApp.botGuildServerSettings.home.createLabel">Create a new Guild Server Settings</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {guildServerSettingsList && guildServerSettingsList.length > 0 ? (
            <Table responsive aria-describedby="guild-server-settings-heading">
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildServerSettings.prefix">Prefix</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildServerSettings.timezone">Timezone</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildServerSettings.raidModeEnabled">Raid Mode Enabled</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildServerSettings.raidModeReason">Raid Mode Reason</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildServerSettings.maxStrikes">Max Strikes</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildServerSettings.acceptingApplications">Accepting Applications</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildServerSettings.autoModConfig">Auto Mod Config</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildServerSettings.punishmentConfig">Punishment Config</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {guildServerSettingsList.map((guildServerSettings, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${guildServerSettings.id}`} color="link" size="sm">
                        {guildServerSettings.id}
                      </Button>
                    </td>
                    <td>{guildServerSettings.prefix}</td>
                    <td>{guildServerSettings.timezone}</td>
                    <td>{guildServerSettings.raidModeEnabled ? 'true' : 'false'}</td>
                    <td>{guildServerSettings.raidModeReason}</td>
                    <td>{guildServerSettings.maxStrikes}</td>
                    <td>{guildServerSettings.acceptingApplications ? 'true' : 'false'}</td>
                    <td>
                      {guildServerSettings.autoModConfig ? (
                        <Link to={`auto-moderation/${guildServerSettings.autoModConfig.id}`}>{guildServerSettings.autoModConfig.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td>
                      {guildServerSettings.punishmentConfig ? (
                        <Link to={`punishment/${guildServerSettings.punishmentConfig.id}`}>{guildServerSettings.punishmentConfig.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${guildServerSettings.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${guildServerSettings.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${guildServerSettings.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.delete">Delete</Translate>
                          </span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            <div className="alert alert-warning">
              <Translate contentKey="webApp.botGuildServerSettings.home.notFound">No Guild Server Settings found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ guildServerSettings }: IRootState) => ({
  guildServerSettingsList: guildServerSettings.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(GuildServerSettings);
