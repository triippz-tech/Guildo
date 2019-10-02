import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './welcome-message.reducer';
import { IWelcomeMessage } from 'app/shared/model/bot/welcome-message.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IWelcomeMessageUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IWelcomeMessageUpdateState {
  isNew: boolean;
}

export class WelcomeMessageUpdate extends React.Component<IWelcomeMessageUpdateProps, IWelcomeMessageUpdateState> {
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
      const { welcomeMessageEntity } = this.props;
      const entity = {
        ...welcomeMessageEntity,
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
    this.props.history.push('/entity/welcome-message');
  };

  render() {
    const { welcomeMessageEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="webApp.botWelcomeMessage.home.createOrEditLabel">
              <Translate contentKey="webApp.botWelcomeMessage.home.createOrEditLabel">Create or edit a WelcomeMessage</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : welcomeMessageEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="welcome-message-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="welcome-message-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="welcome-message-name">
                    <Translate contentKey="webApp.botWelcomeMessage.name">Name</Translate>
                  </Label>
                  <AvField
                    id="welcome-message-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="messageTitleLabel" for="welcome-message-messageTitle">
                    <Translate contentKey="webApp.botWelcomeMessage.messageTitle">Message Title</Translate>
                  </Label>
                  <AvField
                    id="welcome-message-messageTitle"
                    type="text"
                    name="messageTitle"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="bodyLabel" for="welcome-message-body">
                    <Translate contentKey="webApp.botWelcomeMessage.body">Body</Translate>
                  </Label>
                  <AvField
                    id="welcome-message-body"
                    type="text"
                    name="body"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="footerLabel" for="welcome-message-footer">
                    <Translate contentKey="webApp.botWelcomeMessage.footer">Footer</Translate>
                  </Label>
                  <AvField
                    id="welcome-message-footer"
                    type="text"
                    name="footer"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="logoUrlLabel" for="welcome-message-logoUrl">
                    <Translate contentKey="webApp.botWelcomeMessage.logoUrl">Logo Url</Translate>
                  </Label>
                  <AvField id="welcome-message-logoUrl" type="text" name="logoUrl" />
                </AvGroup>
                <AvGroup>
                  <Label id="guildIdLabel" for="welcome-message-guildId">
                    <Translate contentKey="webApp.botWelcomeMessage.guildId">Guild Id</Translate>
                  </Label>
                  <AvField
                    id="welcome-message-guildId"
                    type="string"
                    className="form-control"
                    name="guildId"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/welcome-message" replace color="info">
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
  welcomeMessageEntity: storeState.welcomeMessage.entity,
  loading: storeState.welcomeMessage.loading,
  updating: storeState.welcomeMessage.updating,
  updateSuccess: storeState.welcomeMessage.updateSuccess
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
)(WelcomeMessageUpdate);
