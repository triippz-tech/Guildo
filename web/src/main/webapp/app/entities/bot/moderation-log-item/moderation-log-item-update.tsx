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
import { getEntity, updateEntity, createEntity, reset } from './moderation-log-item.reducer';
import { IModerationLogItem } from 'app/shared/model/bot/moderation-log-item.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IModerationLogItemUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IModerationLogItemUpdateState {
  isNew: boolean;
  modItemGuildServerId: string;
}

export class ModerationLogItemUpdate extends React.Component<IModerationLogItemUpdateProps, IModerationLogItemUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      modItemGuildServerId: '0',
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
      const { moderationLogItemEntity } = this.props;
      const entity = {
        ...moderationLogItemEntity,
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
    this.props.history.push('/entity/moderation-log-item');
  };

  render() {
    const { moderationLogItemEntity, guildServers, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="webApp.botModerationLogItem.home.createOrEditLabel">
              <Translate contentKey="webApp.botModerationLogItem.home.createOrEditLabel">Create or edit a ModerationLogItem</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : moderationLogItemEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="moderation-log-item-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="moderation-log-item-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="channelIdLabel" for="moderation-log-item-channelId">
                    <Translate contentKey="webApp.botModerationLogItem.channelId">Channel Id</Translate>
                  </Label>
                  <AvField
                    id="moderation-log-item-channelId"
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
                  <Label id="channelNameLabel" for="moderation-log-item-channelName">
                    <Translate contentKey="webApp.botModerationLogItem.channelName">Channel Name</Translate>
                  </Label>
                  <AvField
                    id="moderation-log-item-channelName"
                    type="text"
                    name="channelName"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="issuedByIdLabel" for="moderation-log-item-issuedById">
                    <Translate contentKey="webApp.botModerationLogItem.issuedById">Issued By Id</Translate>
                  </Label>
                  <AvField
                    id="moderation-log-item-issuedById"
                    type="string"
                    className="form-control"
                    name="issuedById"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="issuedByNameLabel" for="moderation-log-item-issuedByName">
                    <Translate contentKey="webApp.botModerationLogItem.issuedByName">Issued By Name</Translate>
                  </Label>
                  <AvField
                    id="moderation-log-item-issuedByName"
                    type="text"
                    name="issuedByName"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="issuedToIdLabel" for="moderation-log-item-issuedToId">
                    <Translate contentKey="webApp.botModerationLogItem.issuedToId">Issued To Id</Translate>
                  </Label>
                  <AvField
                    id="moderation-log-item-issuedToId"
                    type="string"
                    className="form-control"
                    name="issuedToId"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="issuedToNameLabel" for="moderation-log-item-issuedToName">
                    <Translate contentKey="webApp.botModerationLogItem.issuedToName">Issued To Name</Translate>
                  </Label>
                  <AvField
                    id="moderation-log-item-issuedToName"
                    type="text"
                    name="issuedToName"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="reasonLabel" for="moderation-log-item-reason">
                    <Translate contentKey="webApp.botModerationLogItem.reason">Reason</Translate>
                  </Label>
                  <AvField
                    id="moderation-log-item-reason"
                    type="text"
                    name="reason"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="timeLabel" for="moderation-log-item-time">
                    <Translate contentKey="webApp.botModerationLogItem.time">Time</Translate>
                  </Label>
                  <AvInput
                    id="moderation-log-item-time"
                    type="datetime-local"
                    className="form-control"
                    name="time"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.moderationLogItemEntity.time)}
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="moderationActionLabel" for="moderation-log-item-moderationAction">
                    <Translate contentKey="webApp.botModerationLogItem.moderationAction">Moderation Action</Translate>
                  </Label>
                  <AvInput
                    id="moderation-log-item-moderationAction"
                    type="select"
                    className="form-control"
                    name="moderationAction"
                    value={(!isNew && moderationLogItemEntity.moderationAction) || 'NONE'}
                  >
                    <option value="NONE">{translate('webApp.PunishmentType.NONE')}</option>
                    <option value="KICK">{translate('webApp.PunishmentType.KICK')}</option>
                    <option value="BAN">{translate('webApp.PunishmentType.BAN')}</option>
                    <option value="TEMP_BAN">{translate('webApp.PunishmentType.TEMP_BAN')}</option>
                    <option value="MUTE">{translate('webApp.PunishmentType.MUTE')}</option>
                    <option value="TEMP_MUTE">{translate('webApp.PunishmentType.TEMP_MUTE')}</option>
                    <option value="UN_BAN">{translate('webApp.PunishmentType.UN_BAN')}</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="guildIdLabel" for="moderation-log-item-guildId">
                    <Translate contentKey="webApp.botModerationLogItem.guildId">Guild Id</Translate>
                  </Label>
                  <AvField id="moderation-log-item-guildId" type="string" className="form-control" name="guildId" />
                </AvGroup>
                <AvGroup>
                  <Label for="moderation-log-item-modItemGuildServer">
                    <Translate contentKey="webApp.botModerationLogItem.modItemGuildServer">Mod Item Guild Server</Translate>
                  </Label>
                  <AvInput id="moderation-log-item-modItemGuildServer" type="select" className="form-control" name="modItemGuildServer.id">
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
                <Button tag={Link} id="cancel-save" to="/entity/moderation-log-item" replace color="info">
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
  moderationLogItemEntity: storeState.moderationLogItem.entity,
  loading: storeState.moderationLogItem.loading,
  updating: storeState.moderationLogItem.updating,
  updateSuccess: storeState.moderationLogItem.updateSuccess
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
)(ModerationLogItemUpdate);
