import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IAutoModeration } from 'app/shared/model/bot/auto-moderation.model';
import { getEntities as getAutoModerations } from 'app/entities/bot/auto-moderation/auto-moderation.reducer';
import { IPunishment } from 'app/shared/model/bot/punishment.model';
import { getEntities as getPunishments } from 'app/entities/bot/punishment/punishment.reducer';
import { getEntity, updateEntity, createEntity, reset } from './guild-server-settings.reducer';
import { IGuildServerSettings } from 'app/shared/model/bot/guild-server-settings.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IGuildServerSettingsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IGuildServerSettingsUpdateState {
  isNew: boolean;
  autoModConfigId: string;
  punishmentConfigId: string;
}

export class GuildServerSettingsUpdate extends React.Component<IGuildServerSettingsUpdateProps, IGuildServerSettingsUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      autoModConfigId: '0',
      punishmentConfigId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getAutoModerations();
    this.props.getPunishments();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { guildServerSettingsEntity } = this.props;
      const entity = {
        ...guildServerSettingsEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/guild-server-settings');
  };

  render() {
    const { guildServerSettingsEntity, autoModerations, punishments, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="webApp.botGuildServerSettings.home.createOrEditLabel">
              <Translate contentKey="webApp.botGuildServerSettings.home.createOrEditLabel">Create or edit a GuildServerSettings</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : guildServerSettingsEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="guild-server-settings-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="guild-server-settings-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="prefixLabel" for="guild-server-settings-prefix">
                    <Translate contentKey="webApp.botGuildServerSettings.prefix">Prefix</Translate>
                  </Label>
                  <AvField
                    id="guild-server-settings-prefix"
                    type="text"
                    name="prefix"
                    validate={{
                      maxLength: { value: 40, errorMessage: translate('entity.validation.maxlength', { max: 40 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="timezoneLabel" for="guild-server-settings-timezone">
                    <Translate contentKey="webApp.botGuildServerSettings.timezone">Timezone</Translate>
                  </Label>
                  <AvField
                    id="guild-server-settings-timezone"
                    type="text"
                    name="timezone"
                    validate={{
                      maxLength: { value: 32, errorMessage: translate('entity.validation.maxlength', { max: 32 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="raidModeEnabledLabel" check>
                    <AvInput id="guild-server-settings-raidModeEnabled" type="checkbox" className="form-control" name="raidModeEnabled" />
                    <Translate contentKey="webApp.botGuildServerSettings.raidModeEnabled">Raid Mode Enabled</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="raidModeReasonLabel" for="guild-server-settings-raidModeReason">
                    <Translate contentKey="webApp.botGuildServerSettings.raidModeReason">Raid Mode Reason</Translate>
                  </Label>
                  <AvField
                    id="guild-server-settings-raidModeReason"
                    type="text"
                    name="raidModeReason"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="maxStrikesLabel" for="guild-server-settings-maxStrikes">
                    <Translate contentKey="webApp.botGuildServerSettings.maxStrikes">Max Strikes</Translate>
                  </Label>
                  <AvField
                    id="guild-server-settings-maxStrikes"
                    type="string"
                    className="form-control"
                    name="maxStrikes"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="acceptingApplicationsLabel" check>
                    <AvInput
                      id="guild-server-settings-acceptingApplications"
                      type="checkbox"
                      className="form-control"
                      name="acceptingApplications"
                    />
                    <Translate contentKey="webApp.botGuildServerSettings.acceptingApplications">Accepting Applications</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label for="guild-server-settings-autoModConfig">
                    <Translate contentKey="webApp.botGuildServerSettings.autoModConfig">Auto Mod Config</Translate>
                  </Label>
                  <AvInput id="guild-server-settings-autoModConfig" type="select" className="form-control" name="autoModConfig.id">
                    <option value="" key="0" />
                    {autoModerations
                      ? autoModerations.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="guild-server-settings-punishmentConfig">
                    <Translate contentKey="webApp.botGuildServerSettings.punishmentConfig">Punishment Config</Translate>
                  </Label>
                  <AvInput id="guild-server-settings-punishmentConfig" type="select" className="form-control" name="punishmentConfig.id">
                    <option value="" key="0" />
                    {punishments
                      ? punishments.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/guild-server-settings" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  autoModerations: storeState.autoModeration.entities,
  punishments: storeState.punishment.entities,
  guildServerSettingsEntity: storeState.guildServerSettings.entity,
  loading: storeState.guildServerSettings.loading,
  updating: storeState.guildServerSettings.updating,
  updateSuccess: storeState.guildServerSettings.updateSuccess
});

const mapDispatchToProps = {
  getAutoModerations,
  getPunishments,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(GuildServerSettingsUpdate);
