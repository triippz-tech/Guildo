import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './discord-user-profile.reducer';
import { IDiscordUserProfile } from 'app/shared/model/bot/discord-user-profile.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IDiscordUserProfileUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IDiscordUserProfileUpdateState {
  isNew: boolean;
}

export class DiscordUserProfileUpdate extends React.Component<IDiscordUserProfileUpdateProps, IDiscordUserProfileUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
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
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { discordUserProfileEntity } = this.props;
      const entity = {
        ...discordUserProfileEntity,
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
    this.props.history.push('/entity/discord-user-profile');
  };

  render() {
    const { discordUserProfileEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="webApp.botDiscordUserProfile.home.createOrEditLabel">
              <Translate contentKey="webApp.botDiscordUserProfile.home.createOrEditLabel">Create or edit a DiscordUserProfile</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : discordUserProfileEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="discord-user-profile-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="discord-user-profile-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="favoriteGameLabel" for="discord-user-profile-favoriteGame">
                    <Translate contentKey="webApp.botDiscordUserProfile.favoriteGame">Favorite Game</Translate>
                  </Label>
                  <AvField id="discord-user-profile-favoriteGame" type="text" name="favoriteGame" />
                </AvGroup>
                <AvGroup>
                  <Label id="profilePhotoLabel" for="discord-user-profile-profilePhoto">
                    <Translate contentKey="webApp.botDiscordUserProfile.profilePhoto">Profile Photo</Translate>
                  </Label>
                  <AvField id="discord-user-profile-profilePhoto" type="text" name="profilePhoto" />
                </AvGroup>
                <AvGroup>
                  <Label id="twitterUrlLabel" for="discord-user-profile-twitterUrl">
                    <Translate contentKey="webApp.botDiscordUserProfile.twitterUrl">Twitter Url</Translate>
                  </Label>
                  <AvField id="discord-user-profile-twitterUrl" type="text" name="twitterUrl" />
                </AvGroup>
                <AvGroup>
                  <Label id="twitchUrlLabel" for="discord-user-profile-twitchUrl">
                    <Translate contentKey="webApp.botDiscordUserProfile.twitchUrl">Twitch Url</Translate>
                  </Label>
                  <AvField id="discord-user-profile-twitchUrl" type="text" name="twitchUrl" />
                </AvGroup>
                <AvGroup>
                  <Label id="youtubeUrlLabel" for="discord-user-profile-youtubeUrl">
                    <Translate contentKey="webApp.botDiscordUserProfile.youtubeUrl">Youtube Url</Translate>
                  </Label>
                  <AvField id="discord-user-profile-youtubeUrl" type="text" name="youtubeUrl" />
                </AvGroup>
                <AvGroup>
                  <Label id="facebookUrlLabel" for="discord-user-profile-facebookUrl">
                    <Translate contentKey="webApp.botDiscordUserProfile.facebookUrl">Facebook Url</Translate>
                  </Label>
                  <AvField id="discord-user-profile-facebookUrl" type="text" name="facebookUrl" />
                </AvGroup>
                <AvGroup>
                  <Label id="hitboxUrlLabel" for="discord-user-profile-hitboxUrl">
                    <Translate contentKey="webApp.botDiscordUserProfile.hitboxUrl">Hitbox Url</Translate>
                  </Label>
                  <AvField id="discord-user-profile-hitboxUrl" type="text" name="hitboxUrl" />
                </AvGroup>
                <AvGroup>
                  <Label id="beamUrlLabel" for="discord-user-profile-beamUrl">
                    <Translate contentKey="webApp.botDiscordUserProfile.beamUrl">Beam Url</Translate>
                  </Label>
                  <AvField id="discord-user-profile-beamUrl" type="text" name="beamUrl" />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/discord-user-profile" replace color="info">
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
  discordUserProfileEntity: storeState.discordUserProfile.entity,
  loading: storeState.discordUserProfile.loading,
  updating: storeState.discordUserProfile.updating,
  updateSuccess: storeState.discordUserProfile.updateSuccess
});

const mapDispatchToProps = {
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
)(DiscordUserProfileUpdate);
