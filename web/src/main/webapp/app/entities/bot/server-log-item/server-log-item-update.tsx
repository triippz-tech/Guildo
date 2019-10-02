import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IGuildServer } from 'app/shared/model/bot/guild-server.model';
import { getEntities as getGuildServers } from 'app/entities/bot/guild-server/guild-server.reducer';
import { getEntity, updateEntity, createEntity, reset } from './server-log-item.reducer';
import { IServerLogItem } from 'app/shared/model/bot/server-log-item.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IServerLogItemUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IServerLogItemUpdateState {
  isNew: boolean;
  serverItemGuildServerId: string;
}

export class ServerLogItemUpdate extends React.Component<IServerLogItemUpdateProps, IServerLogItemUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      serverItemGuildServerId: '0',
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

    this.props.getGuildServers();
  }

  saveEntity = (event, errors, values) => {
    values.time = convertDateTimeToServer(values.time);

    if (errors.length === 0) {
      const { serverLogItemEntity } = this.props;
      const entity = {
        ...serverLogItemEntity,
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
    this.props.history.push('/entity/server-log-item');
  };

  render() {
    const { serverLogItemEntity, guildServers, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="webApp.botServerLogItem.home.createOrEditLabel">
              <Translate contentKey="webApp.botServerLogItem.home.createOrEditLabel">Create or edit a ServerLogItem</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : serverLogItemEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="server-log-item-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="server-log-item-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="activityLabel" for="server-log-item-activity">
                    <Translate contentKey="webApp.botServerLogItem.activity">Activity</Translate>
                  </Label>
                  <AvInput
                    id="server-log-item-activity"
                    type="select"
                    className="form-control"
                    name="activity"
                    value={(!isNew && serverLogItemEntity.activity) || 'SERVER_JOIN'}
                  >
                    <option value="SERVER_JOIN">{translate('webApp.Activity.SERVER_JOIN')}</option>
                    <option value="SERVER_QUIT">{translate('webApp.Activity.SERVER_QUIT')}</option>
                    <option value="CHANNEL_JOIN">{translate('webApp.Activity.CHANNEL_JOIN')}</option>
                    <option value="CHANNEL_LEAVE">{translate('webApp.Activity.CHANNEL_LEAVE')}</option>
                    <option value="NAME_CHANGE">{translate('webApp.Activity.NAME_CHANGE')}</option>
                    <option value="AVATAR_CHANGE">{translate('webApp.Activity.AVATAR_CHANGE')}</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="channelIdLabel" for="server-log-item-channelId">
                    <Translate contentKey="webApp.botServerLogItem.channelId">Channel Id</Translate>
                  </Label>
                  <AvField
                    id="server-log-item-channelId"
                    type="string"
                    className="form-control"
                    name="channelId"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="channelNameLabel" for="server-log-item-channelName">
                    <Translate contentKey="webApp.botServerLogItem.channelName">Channel Name</Translate>
                  </Label>
                  <AvField
                    id="server-log-item-channelName"
                    type="text"
                    name="channelName"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="timeLabel" for="server-log-item-time">
                    <Translate contentKey="webApp.botServerLogItem.time">Time</Translate>
                  </Label>
                  <AvInput
                    id="server-log-item-time"
                    type="datetime-local"
                    className="form-control"
                    name="time"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.serverLogItemEntity.time)}
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="userIdLabel" for="server-log-item-userId">
                    <Translate contentKey="webApp.botServerLogItem.userId">User Id</Translate>
                  </Label>
                  <AvField
                    id="server-log-item-userId"
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
                  <Label id="userNameLabel" for="server-log-item-userName">
                    <Translate contentKey="webApp.botServerLogItem.userName">User Name</Translate>
                  </Label>
                  <AvField
                    id="server-log-item-userName"
                    type="text"
                    name="userName"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="guildIdLabel" for="server-log-item-guildId">
                    <Translate contentKey="webApp.botServerLogItem.guildId">Guild Id</Translate>
                  </Label>
                  <AvField
                    id="server-log-item-guildId"
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
                  <Label for="server-log-item-serverItemGuildServer">
                    <Translate contentKey="webApp.botServerLogItem.serverItemGuildServer">Server Item Guild Server</Translate>
                  </Label>
                  <AvInput
                    id="server-log-item-serverItemGuildServer"
                    type="select"
                    className="form-control"
                    name="serverItemGuildServer.id"
                  >
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
                <Button tag={Link} id="cancel-save" to="/entity/server-log-item" replace color="info">
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
  guildServers: storeState.guildServer.entities,
  serverLogItemEntity: storeState.serverLogItem.entity,
  loading: storeState.serverLogItem.loading,
  updating: storeState.serverLogItem.updating,
  updateSuccess: storeState.serverLogItem.updateSuccess
});

const mapDispatchToProps = {
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
)(ServerLogItemUpdate);
