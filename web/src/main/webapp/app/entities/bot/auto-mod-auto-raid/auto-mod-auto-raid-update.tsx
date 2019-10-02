import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './auto-mod-auto-raid.reducer';
import { IAutoModAutoRaid } from 'app/shared/model/bot/auto-mod-auto-raid.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAutoModAutoRaidUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IAutoModAutoRaidUpdateState {
  isNew: boolean;
}

export class AutoModAutoRaidUpdate extends React.Component<IAutoModAutoRaidUpdateProps, IAutoModAutoRaidUpdateState> {
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
      const { autoModAutoRaidEntity } = this.props;
      const entity = {
        ...autoModAutoRaidEntity,
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
    this.props.history.push('/entity/auto-mod-auto-raid');
  };

  render() {
    const { autoModAutoRaidEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="webApp.botAutoModAutoRaid.home.createOrEditLabel">
              <Translate contentKey="webApp.botAutoModAutoRaid.home.createOrEditLabel">Create or edit a AutoModAutoRaid</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : autoModAutoRaidEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="auto-mod-auto-raid-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="auto-mod-auto-raid-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="autoRaidEnabledLabel" check>
                    <AvInput id="auto-mod-auto-raid-autoRaidEnabled" type="checkbox" className="form-control" name="autoRaidEnabled" />
                    <Translate contentKey="webApp.botAutoModAutoRaid.autoRaidEnabled">Auto Raid Enabled</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="autoRaidTimeThresholdLabel" for="auto-mod-auto-raid-autoRaidTimeThreshold">
                    <Translate contentKey="webApp.botAutoModAutoRaid.autoRaidTimeThreshold">Auto Raid Time Threshold</Translate>
                  </Label>
                  <AvField
                    id="auto-mod-auto-raid-autoRaidTimeThreshold"
                    type="string"
                    className="form-control"
                    name="autoRaidTimeThreshold"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/auto-mod-auto-raid" replace color="info">
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
  autoModAutoRaidEntity: storeState.autoModAutoRaid.entity,
  loading: storeState.autoModAutoRaid.loading,
  updating: storeState.autoModAutoRaid.updating,
  updateSuccess: storeState.autoModAutoRaid.updateSuccess
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
)(AutoModAutoRaidUpdate);
