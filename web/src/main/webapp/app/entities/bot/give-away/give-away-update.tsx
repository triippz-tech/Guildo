import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IDiscordUser } from 'app/shared/model/bot/discord-user.model';
import { getEntities as getDiscordUsers } from 'app/entities/bot/discord-user/discord-user.reducer';
import { IGuildServer } from 'app/shared/model/bot/guild-server.model';
import { getEntities as getGuildServers } from 'app/entities/bot/guild-server/guild-server.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './give-away.reducer';
import { IGiveAway } from 'app/shared/model/bot/give-away.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IGiveAwayUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IGiveAwayUpdateState {
  isNew: boolean;
  winnerId: string;
  guildGiveAwayId: string;
}

export class GiveAwayUpdate extends React.Component<IGiveAwayUpdateProps, IGiveAwayUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      winnerId: '0',
      guildGiveAwayId: '0',
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
    values.finish = convertDateTimeToServer(values.finish);

    if (errors.length === 0) {
      const { giveAwayEntity } = this.props;
      const entity = {
        ...giveAwayEntity,
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
    this.props.history.push('/entity/give-away');
  };

  render() {
    const { giveAwayEntity, discordUsers, guildServers, loading, updating } = this.props;
    const { isNew } = this.state;

    const { message } = giveAwayEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="webApp.botGiveAway.home.createOrEditLabel">
              <Translate contentKey="webApp.botGiveAway.home.createOrEditLabel">Create or edit a GiveAway</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : giveAwayEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="give-away-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="give-away-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="give-away-name">
                    <Translate contentKey="webApp.botGiveAway.name">Name</Translate>
                  </Label>
                  <AvField
                    id="give-away-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      maxLength: { value: 250, errorMessage: translate('entity.validation.maxlength', { max: 250 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="imageLabel" for="give-away-image">
                    <Translate contentKey="webApp.botGiveAway.image">Image</Translate>
                  </Label>
                  <AvField id="give-away-image" type="text" name="image" />
                </AvGroup>
                <AvGroup>
                  <Label id="messageLabel" for="give-away-message">
                    <Translate contentKey="webApp.botGiveAway.message">Message</Translate>
                  </Label>
                  <AvInput
                    id="give-away-message"
                    type="textarea"
                    name="message"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="messageIdLabel" for="give-away-messageId">
                    <Translate contentKey="webApp.botGiveAway.messageId">Message Id</Translate>
                  </Label>
                  <AvField
                    id="give-away-messageId"
                    type="string"
                    className="form-control"
                    name="messageId"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="textChannelIdLabel" for="give-away-textChannelId">
                    <Translate contentKey="webApp.botGiveAway.textChannelId">Text Channel Id</Translate>
                  </Label>
                  <AvField
                    id="give-away-textChannelId"
                    type="string"
                    className="form-control"
                    name="textChannelId"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="finishLabel" for="give-away-finish">
                    <Translate contentKey="webApp.botGiveAway.finish">Finish</Translate>
                  </Label>
                  <AvInput
                    id="give-away-finish"
                    type="datetime-local"
                    className="form-control"
                    name="finish"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.giveAwayEntity.finish)}
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="expiredLabel" check>
                    <AvInput id="give-away-expired" type="checkbox" className="form-control" name="expired" />
                    <Translate contentKey="webApp.botGiveAway.expired">Expired</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="guildIdLabel" for="give-away-guildId">
                    <Translate contentKey="webApp.botGiveAway.guildId">Guild Id</Translate>
                  </Label>
                  <AvField id="give-away-guildId" type="string" className="form-control" name="guildId" />
                </AvGroup>
                <AvGroup>
                  <Label for="give-away-winner">
                    <Translate contentKey="webApp.botGiveAway.winner">Winner</Translate>
                  </Label>
                  <AvInput id="give-away-winner" type="select" className="form-control" name="winner.id">
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
                  <Label for="give-away-guildGiveAway">
                    <Translate contentKey="webApp.botGiveAway.guildGiveAway">Guild Give Away</Translate>
                  </Label>
                  <AvInput id="give-away-guildGiveAway" type="select" className="form-control" name="guildGiveAway.id">
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
                <Button tag={Link} id="cancel-save" to="/entity/give-away" replace color="info">
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
  giveAwayEntity: storeState.giveAway.entity,
  loading: storeState.giveAway.loading,
  updating: storeState.giveAway.updating,
  updateSuccess: storeState.giveAway.updateSuccess
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
)(GiveAwayUpdate);
