import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './auto-mod-ignore.reducer';
import { IAutoModIgnore } from 'app/shared/model/bot/auto-mod-ignore.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAutoModIgnoreUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IAutoModIgnoreUpdateState {
  isNew: boolean;
}

export class AutoModIgnoreUpdate extends React.Component<IAutoModIgnoreUpdateProps, IAutoModIgnoreUpdateState> {
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
      const { autoModIgnoreEntity } = this.props;
      const entity = {
        ...autoModIgnoreEntity,
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
    this.props.history.push('/entity/auto-mod-ignore');
  };

  render() {
    const { autoModIgnoreEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="webApp.botAutoModIgnore.home.createOrEditLabel">
              <Translate contentKey="webApp.botAutoModIgnore.home.createOrEditLabel">Create or edit a AutoModIgnore</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : autoModIgnoreEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="auto-mod-ignore-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="auto-mod-ignore-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="roleIdLabel" for="auto-mod-ignore-roleId">
                    <Translate contentKey="webApp.botAutoModIgnore.roleId">Role Id</Translate>
                  </Label>
                  <AvField
                    id="auto-mod-ignore-roleId"
                    type="string"
                    className="form-control"
                    name="roleId"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="channelIdLabel" for="auto-mod-ignore-channelId">
                    <Translate contentKey="webApp.botAutoModIgnore.channelId">Channel Id</Translate>
                  </Label>
                  <AvField
                    id="auto-mod-ignore-channelId"
                    type="string"
                    className="form-control"
                    name="channelId"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/auto-mod-ignore" replace color="info">
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
  autoModIgnoreEntity: storeState.autoModIgnore.entity,
  loading: storeState.autoModIgnore.loading,
  updating: storeState.autoModIgnore.updating,
  updateSuccess: storeState.autoModIgnore.updateSuccess
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
)(AutoModIgnoreUpdate);
