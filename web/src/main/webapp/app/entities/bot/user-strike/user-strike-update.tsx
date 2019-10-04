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
import { getEntity, updateEntity, createEntity, reset } from './user-strike.reducer';
import { IUserStrike } from 'app/shared/model/bot/user-strike.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IUserStrikeUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IUserStrikeUpdateState {
  isNew: boolean;
  discordUserId: string;
}

export class UserStrikeUpdate extends React.Component<IUserStrikeUpdateProps, IUserStrikeUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      discordUserId: '0',
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

    this.props.getDiscordUsers();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { userStrikeEntity } = this.props;
      const entity = {
        ...userStrikeEntity,
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
    this.props.history.push('/entity/user-strike');
  };

  render() {
    const { userStrikeEntity, discordUsers, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="webApp.botUserStrike.home.createOrEditLabel">
              <Translate contentKey="webApp.botUserStrike.home.createOrEditLabel">Create or edit a UserStrike</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : userStrikeEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="user-strike-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="user-strike-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="countLabel" for="user-strike-count">
                    <Translate contentKey="webApp.botUserStrike.count">Count</Translate>
                  </Label>
                  <AvField
                    id="user-strike-count"
                    type="string"
                    className="form-control"
                    name="count"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      min: { value: 0, errorMessage: translate('entity.validation.min', { min: 0 }) },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="userIdLabel" for="user-strike-userId">
                    <Translate contentKey="webApp.botUserStrike.userId">User Id</Translate>
                  </Label>
                  <AvField
                    id="user-strike-userId"
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
                  <Label id="guildIdLabel" for="user-strike-guildId">
                    <Translate contentKey="webApp.botUserStrike.guildId">Guild Id</Translate>
                  </Label>
                  <AvField
                    id="user-strike-guildId"
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
                  <Label for="user-strike-discordUser">
                    <Translate contentKey="webApp.botUserStrike.discordUser">Discord User</Translate>
                  </Label>
                  <AvInput id="user-strike-discordUser" type="select" className="form-control" name="discordUser.id">
                    <option value="" key="0" />
                    {discordUsers
                      ? discordUsers.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.userName}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/user-strike" replace color="info">
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
  userStrikeEntity: storeState.userStrike.entity,
  loading: storeState.userStrike.loading,
  updating: storeState.userStrike.updating,
  updateSuccess: storeState.userStrike.updateSuccess
});

const mapDispatchToProps = {
  getDiscordUsers,
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
)(UserStrikeUpdate);
