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
import { getEntity, updateEntity, createEntity, reset } from './temp-ban.reducer';
import { ITempBan } from 'app/shared/model/bot/temp-ban.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ITempBanUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ITempBanUpdateState {
  isNew: boolean;
  bannedUserId: string;
  tempBanGuildServerId: string;
}

export class TempBanUpdate extends React.Component<ITempBanUpdateProps, ITempBanUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      bannedUserId: '0',
      tempBanGuildServerId: '0',
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
    values.endTime = convertDateTimeToServer(values.endTime);

    if (errors.length === 0) {
      const { tempBanEntity } = this.props;
      const entity = {
        ...tempBanEntity,
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
    this.props.history.push('/entity/temp-ban');
  };

  render() {
    const { tempBanEntity, discordUsers, guildServers, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="webApp.botTempBan.home.createOrEditLabel">
              <Translate contentKey="webApp.botTempBan.home.createOrEditLabel">Create or edit a TempBan</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : tempBanEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="temp-ban-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="temp-ban-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="reasonLabel" for="temp-ban-reason">
                    <Translate contentKey="webApp.botTempBan.reason">Reason</Translate>
                  </Label>
                  <AvField
                    id="temp-ban-reason"
                    type="text"
                    name="reason"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="endTimeLabel" for="temp-ban-endTime">
                    <Translate contentKey="webApp.botTempBan.endTime">End Time</Translate>
                  </Label>
                  <AvInput
                    id="temp-ban-endTime"
                    type="datetime-local"
                    className="form-control"
                    name="endTime"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.tempBanEntity.endTime)}
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="guildIdLabel" for="temp-ban-guildId">
                    <Translate contentKey="webApp.botTempBan.guildId">Guild Id</Translate>
                  </Label>
                  <AvField
                    id="temp-ban-guildId"
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
                  <Label id="userIdLabel" for="temp-ban-userId">
                    <Translate contentKey="webApp.botTempBan.userId">User Id</Translate>
                  </Label>
                  <AvField
                    id="temp-ban-userId"
                    type="string"
                    className="form-control"
                    name="userId"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="temp-ban-bannedUser">
                    <Translate contentKey="webApp.botTempBan.bannedUser">Banned User</Translate>
                  </Label>
                  <AvInput id="temp-ban-bannedUser" type="select" className="form-control" name="bannedUser.id">
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
                  <Label for="temp-ban-tempBanGuildServer">
                    <Translate contentKey="webApp.botTempBan.tempBanGuildServer">Temp Ban Guild Server</Translate>
                  </Label>
                  <AvInput id="temp-ban-tempBanGuildServer" type="select" className="form-control" name="tempBanGuildServer.id">
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
                <Button tag={Link} id="cancel-save" to="/entity/temp-ban" replace color="info">
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
  tempBanEntity: storeState.tempBan.entity,
  loading: storeState.tempBan.loading,
  updating: storeState.tempBan.updating,
  updateSuccess: storeState.tempBan.updateSuccess
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
)(TempBanUpdate);
