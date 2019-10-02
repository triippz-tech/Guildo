import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './guild-server-profile.reducer';
import { IGuildServerProfile } from 'app/shared/model/bot/guild-server-profile.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IGuildServerProfileUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IGuildServerProfileUpdateState {
  isNew: boolean;
}

export class GuildServerProfileUpdate extends React.Component<IGuildServerProfileUpdateProps, IGuildServerProfileUpdateState> {
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
      const { guildServerProfileEntity } = this.props;
      const entity = {
        ...guildServerProfileEntity,
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
    this.props.history.push('/entity/guild-server-profile');
  };

  render() {
    const { guildServerProfileEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="webApp.botGuildServerProfile.home.createOrEditLabel">
              <Translate contentKey="webApp.botGuildServerProfile.home.createOrEditLabel">Create or edit a GuildServerProfile</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : guildServerProfileEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="guild-server-profile-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="guild-server-profile-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="guildTypeLabel" for="guild-server-profile-guildType">
                    <Translate contentKey="webApp.botGuildServerProfile.guildType">Guild Type</Translate>
                  </Label>
                  <AvInput
                    id="guild-server-profile-guildType"
                    type="select"
                    className="form-control"
                    name="guildType"
                    value={(!isNew && guildServerProfileEntity.guildType) || 'CASUAL'}
                  >
                    <option value="CASUAL">{translate('webApp.GuildType.CASUAL')}</option>
                    <option value="SEMI_PRO">{translate('webApp.GuildType.SEMI_PRO')}</option>
                    <option value="PRO">{translate('webApp.GuildType.PRO')}</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="playStyleLabel" for="guild-server-profile-playStyle">
                    <Translate contentKey="webApp.botGuildServerProfile.playStyle">Play Style</Translate>
                  </Label>
                  <AvInput
                    id="guild-server-profile-playStyle"
                    type="select"
                    className="form-control"
                    name="playStyle"
                    value={(!isNew && guildServerProfileEntity.playStyle) || 'RP'}
                  >
                    <option value="RP">{translate('webApp.GuildPlayStyle.RP')}</option>
                    <option value="PVP">{translate('webApp.GuildPlayStyle.PVP')}</option>
                    <option value="PVE">{translate('webApp.GuildPlayStyle.PVE')}</option>
                    <option value="WPVP">{translate('webApp.GuildPlayStyle.WPVP')}</option>
                    <option value="RAIDING">{translate('webApp.GuildPlayStyle.RAIDING')}</option>
                    <option value="CHILL">{translate('webApp.GuildPlayStyle.CHILL')}</option>
                    <option value="ANY">{translate('webApp.GuildPlayStyle.ANY')}</option>
                    <option value="OTHER">{translate('webApp.GuildPlayStyle.OTHER')}</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="guild-server-profile-description">
                    <Translate contentKey="webApp.botGuildServerProfile.description">Description</Translate>
                  </Label>
                  <AvField id="guild-server-profile-description" type="text" name="description" />
                </AvGroup>
                <AvGroup>
                  <Label id="websiteLabel" for="guild-server-profile-website">
                    <Translate contentKey="webApp.botGuildServerProfile.website">Website</Translate>
                  </Label>
                  <AvField id="guild-server-profile-website" type="text" name="website" />
                </AvGroup>
                <AvGroup>
                  <Label id="discordUrlLabel" for="guild-server-profile-discordUrl">
                    <Translate contentKey="webApp.botGuildServerProfile.discordUrl">Discord Url</Translate>
                  </Label>
                  <AvField id="guild-server-profile-discordUrl" type="text" name="discordUrl" />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/guild-server-profile" replace color="info">
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
  guildServerProfileEntity: storeState.guildServerProfile.entity,
  loading: storeState.guildServerProfile.loading,
  updating: storeState.guildServerProfile.updating,
  updateSuccess: storeState.guildServerProfile.updateSuccess
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
)(GuildServerProfileUpdate);
