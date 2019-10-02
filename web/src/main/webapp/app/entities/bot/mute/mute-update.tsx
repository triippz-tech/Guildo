import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IDiscordUser } from 'app/shared/model/bot/discord-user.model';
import { getEntities as getDiscordUsers } from 'app/entities/bot/discord-user/discord-user.reducer';
import { IGuildServer } from 'app/shared/model/bot/guild-server.model';
import { getEntities as getGuildServers } from 'app/entities/bot/guild-server/guild-server.reducer';
import { getEntity, updateEntity, createEntity, reset } from './mute.reducer';
import { IMute } from 'app/shared/model/bot/mute.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IMuteUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IMuteUpdateState {
  isNew: boolean;
  mutedUserId: string;
  mutedGuildServerId: string;
}

export class MuteUpdate extends React.Component<IMuteUpdateProps, IMuteUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      mutedUserId: '0',
      mutedGuildServerId: '0',
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

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { muteEntity } = this.props;
      const entity = {
        ...muteEntity,
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
    this.props.history.push('/entity/mute');
  };

  render() {
    const { muteEntity, discordUsers, guildServers, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="webApp.botMute.home.createOrEditLabel">
              <Translate contentKey="webApp.botMute.home.createOrEditLabel">Create or edit a Mute</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : muteEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="mute-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="mute-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="reasonLabel" for="mute-reason">
                    <Translate contentKey="webApp.botMute.reason">Reason</Translate>
                  </Label>
                  <AvField
                    id="mute-reason"
                    type="text"
                    name="reason"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="endTimeLabel" for="mute-endTime">
                    <Translate contentKey="webApp.botMute.endTime">End Time</Translate>
                  </Label>
                  <AvField
                    id="mute-endTime"
                    type="text"
                    name="endTime"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="guildIdLabel" for="mute-guildId">
                    <Translate contentKey="webApp.botMute.guildId">Guild Id</Translate>
                  </Label>
                  <AvField id="mute-guildId" type="string" className="form-control" name="guildId" />
                </AvGroup>
                <AvGroup>
                  <Label id="userIdLabel" for="mute-userId">
                    <Translate contentKey="webApp.botMute.userId">User Id</Translate>
                  </Label>
                  <AvField id="mute-userId" type="string" className="form-control" name="userId" />
                </AvGroup>
                <AvGroup>
                  <Label for="mute-mutedUser">
                    <Translate contentKey="webApp.botMute.mutedUser">Muted User</Translate>
                  </Label>
                  <AvInput id="mute-mutedUser" type="select" className="form-control" name="mutedUser.id">
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
                  <Label for="mute-mutedGuildServer">
                    <Translate contentKey="webApp.botMute.mutedGuildServer">Muted Guild Server</Translate>
                  </Label>
                  <AvInput id="mute-mutedGuildServer" type="select" className="form-control" name="mutedGuildServer.id">
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
                <Button tag={Link} id="cancel-save" to="/entity/mute" replace color="info">
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
  muteEntity: storeState.mute.entity,
  loading: storeState.mute.loading,
  updating: storeState.mute.updating,
  updateSuccess: storeState.mute.updateSuccess
});

const mapDispatchToProps = {
  getDiscordUsers,
  getGuildServers,
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
)(MuteUpdate);
