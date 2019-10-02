import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IGuildServer } from 'app/shared/model/bot/guild-server.model';
import { getEntities as getGuildServers } from 'app/entities/bot/guild-server/guild-server.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './scheduled-announcement.reducer';
import { IScheduledAnnouncement } from 'app/shared/model/bot/scheduled-announcement.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IScheduledAnnouncementUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IScheduledAnnouncementUpdateState {
  isNew: boolean;
  annouceGuildId: string;
}

export class ScheduledAnnouncementUpdate extends React.Component<IScheduledAnnouncementUpdateProps, IScheduledAnnouncementUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      annouceGuildId: '0',
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

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    values.annoucementFire = convertDateTimeToServer(values.annoucementFire);

    if (errors.length === 0) {
      const { scheduledAnnouncementEntity } = this.props;
      const entity = {
        ...scheduledAnnouncementEntity,
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
    this.props.history.push('/entity/scheduled-announcement');
  };

  render() {
    const { scheduledAnnouncementEntity, guildServers, loading, updating } = this.props;
    const { isNew } = this.state;

    const { annoucementMessage } = scheduledAnnouncementEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="webApp.botScheduledAnnouncement.home.createOrEditLabel">
              <Translate contentKey="webApp.botScheduledAnnouncement.home.createOrEditLabel">
                Create or edit a ScheduledAnnouncement
              </Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : scheduledAnnouncementEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="scheduled-announcement-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="scheduled-announcement-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="annoucementTitleLabel" for="scheduled-announcement-annoucementTitle">
                    <Translate contentKey="webApp.botScheduledAnnouncement.annoucementTitle">Annoucement Title</Translate>
                  </Label>
                  <AvField
                    id="scheduled-announcement-annoucementTitle"
                    type="text"
                    name="annoucementTitle"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      maxLength: { value: 250, errorMessage: translate('entity.validation.maxlength', { max: 250 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="annoucementImgUrlLabel" for="scheduled-announcement-annoucementImgUrl">
                    <Translate contentKey="webApp.botScheduledAnnouncement.annoucementImgUrl">Annoucement Img Url</Translate>
                  </Label>
                  <AvField
                    id="scheduled-announcement-annoucementImgUrl"
                    type="text"
                    name="annoucementImgUrl"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      maxLength: { value: 250, errorMessage: translate('entity.validation.maxlength', { max: 250 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="annoucementMessageLabel" for="scheduled-announcement-annoucementMessage">
                    <Translate contentKey="webApp.botScheduledAnnouncement.annoucementMessage">Annoucement Message</Translate>
                  </Label>
                  <AvInput
                    id="scheduled-announcement-annoucementMessage"
                    type="textarea"
                    name="annoucementMessage"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="annoucementFireLabel" for="scheduled-announcement-annoucementFire">
                    <Translate contentKey="webApp.botScheduledAnnouncement.annoucementFire">Annoucement Fire</Translate>
                  </Label>
                  <AvInput
                    id="scheduled-announcement-annoucementFire"
                    type="datetime-local"
                    className="form-control"
                    name="annoucementFire"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.scheduledAnnouncementEntity.annoucementFire)}
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="guildIdLabel" for="scheduled-announcement-guildId">
                    <Translate contentKey="webApp.botScheduledAnnouncement.guildId">Guild Id</Translate>
                  </Label>
                  <AvField
                    id="scheduled-announcement-guildId"
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
                  <Label for="scheduled-announcement-annouceGuild">
                    <Translate contentKey="webApp.botScheduledAnnouncement.annouceGuild">Annouce Guild</Translate>
                  </Label>
                  <AvInput id="scheduled-announcement-annouceGuild" type="select" className="form-control" name="annouceGuild.id">
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
                <Button tag={Link} id="cancel-save" to="/entity/scheduled-announcement" replace color="info">
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
  scheduledAnnouncementEntity: storeState.scheduledAnnouncement.entity,
  loading: storeState.scheduledAnnouncement.loading,
  updating: storeState.scheduledAnnouncement.updating,
  updateSuccess: storeState.scheduledAnnouncement.updateSuccess
});

const mapDispatchToProps = {
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
)(ScheduledAnnouncementUpdate);
