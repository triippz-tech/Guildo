import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IDiscordUserProfile } from 'app/shared/model/bot/discord-user-profile.model';
import { getEntities as getDiscordUserProfiles } from 'app/entities/bot/discord-user-profile/discord-user-profile.reducer';
import { getEntity, updateEntity, createEntity, reset } from './discord-user.reducer';
import { IDiscordUser } from 'app/shared/model/bot/discord-user.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IDiscordUserUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IDiscordUserUpdateState {
  isNew: boolean;
  userProfileId: string;
}

export class DiscordUserUpdate extends React.Component<IDiscordUserUpdateProps, IDiscordUserUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      userProfileId: '0',
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

    this.props.getDiscordUserProfiles();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { discordUserEntity } = this.props;
      const entity = {
        ...discordUserEntity,
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
    this.props.history.push('/entity/discord-user');
  };

  render() {
    const { discordUserEntity, discordUserProfiles, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="webApp.botDiscordUser.home.createOrEditLabel">
              <Translate contentKey="webApp.botDiscordUser.home.createOrEditLabel">Create or edit a DiscordUser</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : discordUserEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="discord-user-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="discord-user-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="userIdLabel" for="discord-user-userId">
                    <Translate contentKey="webApp.botDiscordUser.userId">User Id</Translate>
                  </Label>
                  <AvField
                    id="discord-user-userId"
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
                  <Label id="userNameLabel" for="discord-user-userName">
                    <Translate contentKey="webApp.botDiscordUser.userName">User Name</Translate>
                  </Label>
                  <AvField
                    id="discord-user-userName"
                    type="text"
                    name="userName"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="iconLabel" for="discord-user-icon">
                    <Translate contentKey="webApp.botDiscordUser.icon">Icon</Translate>
                  </Label>
                  <AvField id="discord-user-icon" type="text" name="icon" />
                </AvGroup>
                <AvGroup>
                  <Label id="commandsIssuedLabel" for="discord-user-commandsIssued">
                    <Translate contentKey="webApp.botDiscordUser.commandsIssued">Commands Issued</Translate>
                  </Label>
                  <AvField
                    id="discord-user-commandsIssued"
                    type="string"
                    className="form-control"
                    name="commandsIssued"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="blacklistedLabel" check>
                    <AvInput id="discord-user-blacklisted" type="checkbox" className="form-control" name="blacklisted" />
                    <Translate contentKey="webApp.botDiscordUser.blacklisted">Blacklisted</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="userLevelLabel" for="discord-user-userLevel">
                    <Translate contentKey="webApp.botDiscordUser.userLevel">User Level</Translate>
                  </Label>
                  <AvInput
                    id="discord-user-userLevel"
                    type="select"
                    className="form-control"
                    name="userLevel"
                    value={(!isNew && discordUserEntity.userLevel) || 'STANDARD'}
                  >
                    <option value="STANDARD">{translate('webApp.DiscordUserLevel.STANDARD')}</option>
                    <option value="SUPPORTER">{translate('webApp.DiscordUserLevel.SUPPORTER')}</option>
                    <option value="OWNER">{translate('webApp.DiscordUserLevel.OWNER')}</option>
                    <option value="DEVELOPER">{translate('webApp.DiscordUserLevel.DEVELOPER')}</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="discord-user-userProfile">
                    <Translate contentKey="webApp.botDiscordUser.userProfile">User Profile</Translate>
                  </Label>
                  <AvInput id="discord-user-userProfile" type="select" className="form-control" name="userProfile.id">
                    <option value="" key="0" />
                    {discordUserProfiles
                      ? discordUserProfiles.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/discord-user" replace color="info">
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
  discordUserProfiles: storeState.discordUserProfile.entities,
  discordUserEntity: storeState.discordUser.entity,
  loading: storeState.discordUser.loading,
  updating: storeState.discordUser.updating,
  updateSuccess: storeState.discordUser.updateSuccess
});

const mapDispatchToProps = {
  getDiscordUserProfiles,
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
)(DiscordUserUpdate);
