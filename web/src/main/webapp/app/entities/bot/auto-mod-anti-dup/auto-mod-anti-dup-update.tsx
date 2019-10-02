import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './auto-mod-anti-dup.reducer';
import { IAutoModAntiDup } from 'app/shared/model/bot/auto-mod-anti-dup.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAutoModAntiDupUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IAutoModAntiDupUpdateState {
  isNew: boolean;
}

export class AutoModAntiDupUpdate extends React.Component<IAutoModAntiDupUpdateProps, IAutoModAntiDupUpdateState> {
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
      const { autoModAntiDupEntity } = this.props;
      const entity = {
        ...autoModAntiDupEntity,
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
    this.props.history.push('/entity/auto-mod-anti-dup');
  };

  render() {
    const { autoModAntiDupEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="webApp.botAutoModAntiDup.home.createOrEditLabel">
              <Translate contentKey="webApp.botAutoModAntiDup.home.createOrEditLabel">Create or edit a AutoModAntiDup</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : autoModAntiDupEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="auto-mod-anti-dup-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="auto-mod-anti-dup-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="deleteThresholdLabel" for="auto-mod-anti-dup-deleteThreshold">
                    <Translate contentKey="webApp.botAutoModAntiDup.deleteThreshold">Delete Threshold</Translate>
                  </Label>
                  <AvField
                    id="auto-mod-anti-dup-deleteThreshold"
                    type="string"
                    className="form-control"
                    name="deleteThreshold"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="dupsToPunishLabel" for="auto-mod-anti-dup-dupsToPunish">
                    <Translate contentKey="webApp.botAutoModAntiDup.dupsToPunish">Dups To Punish</Translate>
                  </Label>
                  <AvField
                    id="auto-mod-anti-dup-dupsToPunish"
                    type="string"
                    className="form-control"
                    name="dupsToPunish"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/auto-mod-anti-dup" replace color="info">
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
  autoModAntiDupEntity: storeState.autoModAntiDup.entity,
  loading: storeState.autoModAntiDup.loading,
  updating: storeState.autoModAntiDup.updating,
  updateSuccess: storeState.autoModAntiDup.updateSuccess
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
)(AutoModAntiDupUpdate);
