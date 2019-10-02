import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IGuildPollItem } from 'app/shared/model/bot/guild-poll-item.model';
import { getEntities as getGuildPollItems } from 'app/entities/bot/guild-poll-item/guild-poll-item.reducer';
import { IGuildServer } from 'app/shared/model/bot/guild-server.model';
import { getEntities as getGuildServers } from 'app/entities/bot/guild-server/guild-server.reducer';
import { getEntity, updateEntity, createEntity, reset } from './guild-poll.reducer';
import { IGuildPoll } from 'app/shared/model/bot/guild-poll.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IGuildPollUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IGuildPollUpdateState {
  isNew: boolean;
  pollItemsId: string;
  pollServerId: string;
}

export class GuildPollUpdate extends React.Component<IGuildPollUpdateProps, IGuildPollUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      pollItemsId: '0',
      pollServerId: '0',
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

    this.props.getGuildPollItems();
    this.props.getGuildServers();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { guildPollEntity } = this.props;
      const entity = {
        ...guildPollEntity,
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
    this.props.history.push('/entity/guild-poll');
  };

  render() {
    const { guildPollEntity, guildPollItems, guildServers, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="webApp.botGuildPoll.home.createOrEditLabel">
              <Translate contentKey="webApp.botGuildPoll.home.createOrEditLabel">Create or edit a GuildPoll</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : guildPollEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="guild-poll-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="guild-poll-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="pollNameLabel" for="guild-poll-pollName">
                    <Translate contentKey="webApp.botGuildPoll.pollName">Poll Name</Translate>
                  </Label>
                  <AvField
                    id="guild-poll-pollName"
                    type="text"
                    name="pollName"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="guild-poll-description">
                    <Translate contentKey="webApp.botGuildPoll.description">Description</Translate>
                  </Label>
                  <AvField
                    id="guild-poll-description"
                    type="text"
                    name="description"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="textChannelIdLabel" for="guild-poll-textChannelId">
                    <Translate contentKey="webApp.botGuildPoll.textChannelId">Text Channel Id</Translate>
                  </Label>
                  <AvField
                    id="guild-poll-textChannelId"
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
                  <Label id="finishTimeLabel" for="guild-poll-finishTime">
                    <Translate contentKey="webApp.botGuildPoll.finishTime">Finish Time</Translate>
                  </Label>
                  <AvField
                    id="guild-poll-finishTime"
                    type="date"
                    className="form-control"
                    name="finishTime"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="completedLabel" check>
                    <AvInput id="guild-poll-completed" type="checkbox" className="form-control" name="completed" />
                    <Translate contentKey="webApp.botGuildPoll.completed">Completed</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="guildIdLabel" for="guild-poll-guildId">
                    <Translate contentKey="webApp.botGuildPoll.guildId">Guild Id</Translate>
                  </Label>
                  <AvField id="guild-poll-guildId" type="string" className="form-control" name="guildId" />
                </AvGroup>
                <AvGroup>
                  <Label for="guild-poll-pollItems">
                    <Translate contentKey="webApp.botGuildPoll.pollItems">Poll Items</Translate>
                  </Label>
                  <AvInput id="guild-poll-pollItems" type="select" className="form-control" name="pollItems.id">
                    <option value="" key="0" />
                    {guildPollItems
                      ? guildPollItems.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="guild-poll-pollServer">
                    <Translate contentKey="webApp.botGuildPoll.pollServer">Poll Server</Translate>
                  </Label>
                  <AvInput id="guild-poll-pollServer" type="select" className="form-control" name="pollServer.id">
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
                <Button tag={Link} id="cancel-save" to="/entity/guild-poll" replace color="info">
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
  guildPollItems: storeState.guildPollItem.entities,
  guildServers: storeState.guildServer.entities,
  guildPollEntity: storeState.guildPoll.entity,
  loading: storeState.guildPoll.loading,
  updating: storeState.guildPoll.updating,
  updateSuccess: storeState.guildPoll.updateSuccess
});

const mapDispatchToProps = {
  getGuildPollItems,
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
)(GuildPollUpdate);
