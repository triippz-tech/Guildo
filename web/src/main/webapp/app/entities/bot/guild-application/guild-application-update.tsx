import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, openFile, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IDiscordUser } from 'app/shared/model/bot/discord-user.model';
import { getEntities as getDiscordUsers } from 'app/entities/bot/discord-user/discord-user.reducer';
import { IGuildServer } from 'app/shared/model/bot/guild-server.model';
import { getEntities as getGuildServers } from 'app/entities/bot/guild-server/guild-server.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './guild-application.reducer';
import { IGuildApplication } from 'app/shared/model/bot/guild-application.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IGuildApplicationUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IGuildApplicationUpdateState {
  isNew: boolean;
  acceptedById: string;
  appliedUserId: string;
  guildServerId: string;
}

export class GuildApplicationUpdate extends React.Component<IGuildApplicationUpdateProps, IGuildApplicationUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      acceptedById: '0',
      appliedUserId: '0',
      guildServerId: '0',
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

    this.props.getDiscordUsers();
    this.props.getGuildServers();
  }

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { guildApplicationEntity } = this.props;
      const entity = {
        ...guildApplicationEntity,
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
    this.props.history.push('/entity/guild-application');
  };

  render() {
    const { guildApplicationEntity, discordUsers, guildServers, loading, updating } = this.props;
    const { isNew } = this.state;

    const { applicationFile, applicationFileContentType } = guildApplicationEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="webApp.botGuildApplication.home.createOrEditLabel">
              <Translate contentKey="webApp.botGuildApplication.home.createOrEditLabel">Create or edit a GuildApplication</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : guildApplicationEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="guild-application-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="guild-application-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="characterNameLabel" for="guild-application-characterName">
                    <Translate contentKey="webApp.botGuildApplication.characterName">Character Name</Translate>
                  </Label>
                  <AvField
                    id="guild-application-characterName"
                    type="text"
                    name="characterName"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="characterTypeLabel" for="guild-application-characterType">
                    <Translate contentKey="webApp.botGuildApplication.characterType">Character Type</Translate>
                  </Label>
                  <AvField
                    id="guild-application-characterType"
                    type="text"
                    name="characterType"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <AvGroup>
                    <Label id="applicationFileLabel" for="applicationFile">
                      <Translate contentKey="webApp.botGuildApplication.applicationFile">Application File</Translate>
                    </Label>
                    <br />
                    {applicationFile ? (
                      <div>
                        <a onClick={openFile(applicationFileContentType, applicationFile)}>
                          <Translate contentKey="entity.action.open">Open</Translate>
                        </a>
                        <br />
                        <Row>
                          <Col md="11">
                            <span>
                              {applicationFileContentType}, {byteSize(applicationFile)}
                            </span>
                          </Col>
                          <Col md="1">
                            <Button color="danger" onClick={this.clearBlob('applicationFile')}>
                              <FontAwesomeIcon icon="times-circle" />
                            </Button>
                          </Col>
                        </Row>
                      </div>
                    ) : null}
                    <input id="file_applicationFile" type="file" onChange={this.onBlobChange(false, 'applicationFile')} />
                    <AvInput type="hidden" name="applicationFile" value={applicationFile} />
                  </AvGroup>
                </AvGroup>
                <AvGroup>
                  <Label id="statusLabel" for="guild-application-status">
                    <Translate contentKey="webApp.botGuildApplication.status">Status</Translate>
                  </Label>
                  <AvInput
                    id="guild-application-status"
                    type="select"
                    className="form-control"
                    name="status"
                    value={(!isNew && guildApplicationEntity.status) || 'ACCEPTED'}
                  >
                    <option value="ACCEPTED">{translate('webApp.ApplicationStatus.ACCEPTED')}</option>
                    <option value="PENDING">{translate('webApp.ApplicationStatus.PENDING')}</option>
                    <option value="NEW">{translate('webApp.ApplicationStatus.NEW')}</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="guild-application-acceptedBy">
                    <Translate contentKey="webApp.botGuildApplication.acceptedBy">Accepted By</Translate>
                  </Label>
                  <AvInput id="guild-application-acceptedBy" type="select" className="form-control" name="acceptedBy.id">
                    <option value="" key="0" />
                    {discordUsers
                      ? discordUsers.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="guild-application-appliedUser">
                    <Translate contentKey="webApp.botGuildApplication.appliedUser">Applied User</Translate>
                  </Label>
                  <AvInput id="guild-application-appliedUser" type="select" className="form-control" name="appliedUser.id">
                    <option value="" key="0" />
                    {discordUsers
                      ? discordUsers.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="guild-application-guildServer">
                    <Translate contentKey="webApp.botGuildApplication.guildServer">Guild Server</Translate>
                  </Label>
                  <AvInput id="guild-application-guildServer" type="select" className="form-control" name="guildServer.id">
                    <option value="" key="0" />
                    {guildServers
                      ? guildServers.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/guild-application" replace color="info">
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
  discordUsers: storeState.discordUser.entities,
  guildServers: storeState.guildServer.entities,
  guildApplicationEntity: storeState.guildApplication.entity,
  loading: storeState.guildApplication.loading,
  updating: storeState.guildApplication.updating,
  updateSuccess: storeState.guildApplication.updateSuccess
});

const mapDispatchToProps = {
  getDiscordUsers,
  getGuildServers,
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(GuildApplicationUpdate);
