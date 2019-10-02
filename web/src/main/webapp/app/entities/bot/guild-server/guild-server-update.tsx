import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IGuildServerProfile } from 'app/shared/model/bot/guild-server-profile.model';
import { getEntities as getGuildServerProfiles } from 'app/entities/bot/guild-server-profile/guild-server-profile.reducer';
import { IGuildApplicationForm } from 'app/shared/model/bot/guild-application-form.model';
import { getEntities as getGuildApplicationForms } from 'app/entities/bot/guild-application-form/guild-application-form.reducer';
import { IGuildServerSettings } from 'app/shared/model/bot/guild-server-settings.model';
import { getEntities as getGuildServerSettings } from 'app/entities/bot/guild-server-settings/guild-server-settings.reducer';
import { IWelcomeMessage } from 'app/shared/model/bot/welcome-message.model';
import { getEntities as getWelcomeMessages } from 'app/entities/bot/welcome-message/welcome-message.reducer';
import { getEntity, updateEntity, createEntity, reset } from './guild-server.reducer';
import { IGuildServer } from 'app/shared/model/bot/guild-server.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IGuildServerUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IGuildServerUpdateState {
  isNew: boolean;
  guildProfileId: string;
  applicationFormId: string;
  guildSettingsId: string;
  welcomeMessageId: string;
}

export class GuildServerUpdate extends React.Component<IGuildServerUpdateProps, IGuildServerUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      guildProfileId: '0',
      applicationFormId: '0',
      guildSettingsId: '0',
      welcomeMessageId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (!this.state.isNew) {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getGuildServerProfiles();
    this.props.getGuildApplicationForms();
    this.props.getGuildServerSettings();
    this.props.getWelcomeMessages();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { guildServerEntity } = this.props;
      const entity = {
        ...guildServerEntity,
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
    this.props.history.push('/entity/guild-server');
  };

  render() {
    const {
      guildServerEntity,
      guildServerProfiles,
      guildApplicationForms,
      guildServerSettings,
      welcomeMessages,
      loading,
      updating
    } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="webApp.botGuildServer.home.createOrEditLabel">
              <Translate contentKey="webApp.botGuildServer.home.createOrEditLabel">Create or edit a GuildServer</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : guildServerEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="guild-server-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="guild-server-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="guildIdLabel" for="guild-server-guildId">
                    <Translate contentKey="webApp.botGuildServer.guildId">Guild Id</Translate>
                  </Label>
                  <AvField
                    id="guild-server-guildId"
                    type="string"
                    className="form-control"
                    name="guildId"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="guildNameLabel" for="guild-server-guildName">
                    <Translate contentKey="webApp.botGuildServer.guildName">Guild Name</Translate>
                  </Label>
                  <AvField
                    id="guild-server-guildName"
                    type="text"
                    name="guildName"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="iconLabel" for="guild-server-icon">
                    <Translate contentKey="webApp.botGuildServer.icon">Icon</Translate>
                  </Label>
                  <AvField id="guild-server-icon" type="text" name="icon" />
                </AvGroup>
                <AvGroup>
                  <Label id="ownerLabel" for="guild-server-owner">
                    <Translate contentKey="webApp.botGuildServer.owner">Owner</Translate>
                  </Label>
                  <AvField
                    id="guild-server-owner"
                    type="string"
                    className="form-control"
                    name="owner"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="serverLevelLabel" for="guild-server-serverLevel">
                    <Translate contentKey="webApp.botGuildServer.serverLevel">Server Level</Translate>
                  </Label>
                  <AvInput
                    id="guild-server-serverLevel"
                    type="select"
                    className="form-control"
                    name="serverLevel"
                    value={(!isNew && guildServerEntity.serverLevel) || 'STANDARD'}
                  >
                    <option value="STANDARD">{translate('webApp.GuildServerLevel.STANDARD')}</option>
                    <option value="PRO">{translate('webApp.GuildServerLevel.PRO')}</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="guild-server-guildProfile">
                    <Translate contentKey="webApp.botGuildServer.guildProfile">Guild Profile</Translate>
                  </Label>
                  <AvInput id="guild-server-guildProfile" type="select" className="form-control" name="guildProfile.id">
                    <option value="" key="0" />
                    {guildServerProfiles
                      ? guildServerProfiles.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="guild-server-applicationForm">
                    <Translate contentKey="webApp.botGuildServer.applicationForm">Application Form</Translate>
                  </Label>
                  <AvInput id="guild-server-applicationForm" type="select" className="form-control" name="applicationForm.id">
                    <option value="" key="0" />
                    {guildApplicationForms
                      ? guildApplicationForms.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="guild-server-guildSettings">
                    <Translate contentKey="webApp.botGuildServer.guildSettings">Guild Settings</Translate>
                  </Label>
                  <AvInput id="guild-server-guildSettings" type="select" className="form-control" name="guildSettings.id">
                    <option value="" key="0" />
                    {guildServerSettings
                      ? guildServerSettings.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="guild-server-welcomeMessage">
                    <Translate contentKey="webApp.botGuildServer.welcomeMessage">Welcome Message</Translate>
                  </Label>
                  <AvInput id="guild-server-welcomeMessage" type="select" className="form-control" name="welcomeMessage.id">
                    <option value="" key="0" />
                    {welcomeMessages
                      ? welcomeMessages.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/guild-server" replace color="info">
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
  guildServerProfiles: storeState.guildServerProfile.entities,
  guildApplicationForms: storeState.guildApplicationForm.entities,
  guildServerSettings: storeState.guildServerSettings.entities,
  welcomeMessages: storeState.welcomeMessage.entities,
  guildServerEntity: storeState.guildServer.entity,
  loading: storeState.guildServer.loading,
  updating: storeState.guildServer.updating,
  updateSuccess: storeState.guildServer.updateSuccess
});

const mapDispatchToProps = {
  getGuildServerProfiles,
  getGuildApplicationForms,
  getGuildServerSettings,
  getWelcomeMessages,
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
)(GuildServerUpdate);
